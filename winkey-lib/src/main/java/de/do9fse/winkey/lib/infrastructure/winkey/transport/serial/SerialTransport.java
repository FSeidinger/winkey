package de.do9fse.winkey.lib.infrastructure.winkey.transport.serial;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.HexFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortTimeoutException;

import de.do9fse.winkey.lib.core.model.ApplicationContext;
import de.do9fse.winkey.lib.core.model.Job;
import de.do9fse.winkey.lib.core.model.ApplicationState;
import de.do9fse.winkey.lib.core.model.commands.CommandInfo;
import de.do9fse.winkey.lib.core.model.commands.Command;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommand;
import de.do9fse.winkey.lib.core.model.commands.admin.EchoTestCommand;
import de.do9fse.winkey.lib.core.model.commands.admin.SetWK1ModeCommand;
import de.do9fse.winkey.lib.core.model.commands.admin.SetWK2ModeCommand;
import de.do9fse.winkey.lib.core.model.error.WinKeyApplicationException;
import de.do9fse.winkey.lib.core.model.error.WKRuntimeException;
import de.do9fse.winkey.lib.core.model.responses.ResponseConfiguration;
import de.do9fse.winkey.lib.core.model.responses.SpeedPotValueResponse;
import de.do9fse.winkey.lib.core.model.responses.Response;
import de.do9fse.winkey.lib.core.model.responses.ResponseFactory;
import de.do9fse.winkey.lib.core.model.responses.StatusResponse;
import de.do9fse.winkey.lib.core.port.in.UnsolicitedResponseListener;
import de.do9fse.winkey.lib.core.port.out.JobQueue;
import de.do9fse.winkey.lib.core.port.out.Transport;
import de.do9fse.winkey.lib.core.model.Validations;

public class SerialTransport implements Transport {
    private static final Logger LOGGER = LoggerFactory.getLogger(SerialTransport.class);
    
    private final SerialPort serialPort;

    private final CopyOnWriteArrayList<UnsolicitedResponseListener> unsolicitedResponseListeners = new CopyOnWriteArrayList<>();

    private Thread serialPortReaderThread;
    private CompletableFuture<Void> serialPortReaderThreadResult;

    private final JobQueue jobQueue;

    private final ResponseFactory responseFactory = new ResponseFactory();

    private PushbackInputStream inputStream;
    private OutputStream outputStream;

    private volatile ApplicationContext context;

    private volatile boolean wk2StatusMode;

    private final HexFormat formatter = HexFormat.of().withPrefix("0x").withSuffix(" ");

    public SerialTransport(final ApplicationContext context, final SerialPort serialPort) {
        this.context = Validations.validateNotNull("Application context", context);
        this.jobQueue = Validations.validateNotNull("Job queue", context.getJobQueue());
        this.serialPort = Validations.validateNotNull("Serial port", serialPort);
    }

    @Override
    public void open() throws WinKeyApplicationException {
        if (context.getState() != ApplicationState.CLOSED) {
            throw new IllegalStateException("Transport is already initializing or ready");
        }
        
        this.serialPort.openPort();
        LOGGER.info("Successfully opened device {}", serialPort.getSystemPortPath());

        this.inputStream = new PushbackInputStream(serialPort.getInputStream());
        this.outputStream = serialPort.getOutputStream();

        context.transitionToState(ApplicationState.INITIALIZING);
    }

    @Override
    public void close() throws WinKeyApplicationException {
        if (context.getState() != ApplicationState.READY && context.getState() != ApplicationState.INITIALIZING) {
            throw new IllegalStateException("Transport is already closed");
        }

        if (context.getState() == ApplicationState.READY) {
            stopSerialReaderThread();
        }

        serialPort.closePort();
        context.transitionToState(ApplicationState.CLOSED);
    }

    @Override
    public void initialize(final long timeout, final TimeUnit unit) throws TimeoutException, WKRuntimeException {
        if (context.getState() != ApplicationState.INITIALIZING) {
            throw new IllegalStateException("Transport is ready or closed");
        }

        Validations.validateNotNull("Time unit", unit);

        if (timeout < 1) {
            throw new IllegalArgumentException(formatError("The timeout must be greater than zero, but was {0}", timeout));
        }

        final Instant now = Instant.now();
        final Instant responseDeadline = now.plus(timeout, unit.toChronoUnit());

        try {
            final char echoChar = 'R';
            final Command echoTestCommand = new EchoTestCommand(echoChar);

            for (int retry = 1; Instant.now().isBefore(responseDeadline); retry++) {
                this.outputStream.write(echoTestCommand.toData());

                try {
                    final int data = readData();
                    final char response = (char) data;

                    if (response == echoChar) {
                        startSerialPortReaderThread();
                        context.transitionToState(ApplicationState.READY);
                        return;
                    }
                } catch(final SerialPortTimeoutException e) {
                    LOGGER.debug("Waiting for response timed out on attempt #{}", retry);
                }
            }
        } catch(final IOException e) {
            throw new WKRuntimeException("Sending and waiting on response failed", e);
        }

        throw new TimeoutException("Failed to initialize transport");
    }

    @Override
    public void addUnsolicitedResponseListener(final UnsolicitedResponseListener listener) {
        this.unsolicitedResponseListeners.addIfAbsent(Validations.validateNotNull("listener", listener));
    }

    @Override
    public void removeUnsolicitedResponseListener(final UnsolicitedResponseListener listener) {
        this.unsolicitedResponseListeners.remove(Validations.validateNotNull("listener", listener));
    }

    private void notifyUnsolicitedResponseListeners(final Response response) {
        for (final UnsolicitedResponseListener listener : this.unsolicitedResponseListeners) {
            try {
                listener.onUnsolicitedResponse(response);
            } catch (final RuntimeException exception) {
                LOGGER.error("Unsolicited response listener failed for response {}", response, exception);
            }
        }
    }

    private void startSerialPortReaderThread() {
        this.serialPortReaderThreadResult = new CompletableFuture<>();
        this.serialPortReaderThread = Thread
            .ofVirtual()
            .name("SerialPortReader")
            .start(() -> serialPortReader(serialPortReaderThreadResult));
    }

    private void stopSerialReaderThread() {
        if (this.serialPortReaderThread != null) {
            this.serialPortReaderThread.interrupt();

            try {
                this.serialPortReaderThreadResult.get(5, TimeUnit.SECONDS);
            } catch (final InterruptedException e) {
                LOGGER.error("Stopping port reader thread failed", e);
            } catch (final ExecutionException e) {
                LOGGER.error("Error stopping port reader thread", e.getCause());
            } catch (final TimeoutException e) {
                LOGGER.warn("Stopping serial port reader thread timed out");
            }

            LOGGER.info("Successfully stopped serial port reader thread");
        }
    }
   
    @Override
    public Job submitJob(final ImmediateCommand command, final CompletableFuture<Response> jobResult) throws WinKeyApplicationException {
        return submitJob(command, jobResult, true);
    }

    public Job submitJob(final ImmediateCommand command, final CompletableFuture<Response> jobResult, final boolean doSend) throws WinKeyApplicationException {
        if (context.getState() != ApplicationState.READY) {
            throw new IllegalStateException("Transport is not ready");
        }

        final Job job = new Job(command, jobResult);

        try {
            this.jobQueue.lock();

            if (!this.jobQueue.offer(job)) {
                throw new WKRuntimeException(formatError("Failed to queue job {0}", job));
            }

            if (doSend) {
                try {
                    this.sendCommand(job);
                } catch (final WinKeyApplicationException | RuntimeException exception) {
                    this.jobQueue.pollLast();
                    throw exception;
                }
            }
        } finally {
            this.jobQueue.unlock();
        }

        return job;
    }

    private void sendCommand(final Job job) throws WinKeyApplicationException {
        final byte[] serialBytes = job.command().toData();

        try {
            outputStream.write(serialBytes);
        } catch (final IOException e) {
            throw new WKRuntimeException("Failed to send command", e);
        }

        LOGGER.debug("Successfully sent command " + job.command());
    }

    private void serialPortReader(final CompletableFuture<Void> threadResult) {
        while (true) {
            try {
                handleStatusBytes();
                processActiveJob();

                if (Thread.currentThread().isInterrupted()) {
                    threadResult.complete(null);
                    break;
                }
            } catch (final RuntimeException | IOException  e) {
                this.jobQueue.failPendingJobs(e);
                threadResult.completeExceptionally(e);
                break;
            }
        }
    }

    private void handleStatusBytes() throws IOException {
        final int data;
        try {
            data = readData();
        } catch (final SerialPortTimeoutException e) {
            return;
        }

        if (isStatusByteHandled(data) || isSpeedPotByteHandled(data) || isOutOfBandHandled(data)) {
            return;
        }

        LOGGER.debug("Unreading {}", this.formatter.formatHex(new byte[] { (byte) data }));

        // Push unused data
        this.inputStream.unread(data);
    }

    private boolean isStatusByteHandled(final int data) {
        if (!isStatusByte(data)) {
            return false;
        }

        try {
            final StatusResponse statusResponse = responseFactory.createResponse(
                new byte[] {(byte) data},
                wk2StatusMode
            );
            notifyUnsolicitedResponseListeners(statusResponse);
            LOGGER.debug("Notified unsolicited status response {}", statusResponse);
        } catch (final IllegalArgumentException exception) {
            throw new WKRuntimeException("Invalid unsolicited status response", exception);
        }

        return true;
    }

    private boolean isSpeedPotByteHandled(final int data) {
        if (!isSpeedPotByte(data)) {
            return false;
        }

        try {
            final Response speedPotResponse = responseFactory.createResponse(
                SpeedPotValueResponse.class,
                new byte[] {(byte) data}
            );
            notifyUnsolicitedResponseListeners(speedPotResponse);
            LOGGER.debug("Notified unsolicited speed pot response {}", formatter.toHexDigits(data));

        } catch (final IllegalArgumentException exception) {
            throw new WKRuntimeException("Invalid unsolicited speed-pot response", exception);
        }

        return true;
    }

    private boolean isOutOfBandHandled(final int data) {
        final Job job = this.jobQueue.peek();

        if (job == null) {
            LOGGER.debug("Out of band data handled {}", (char) data);
            return true;
        }

        return false;
    }

    private boolean isStatusByte(final int receivedByte) {
        return (receivedByte & 0xE0) == 0xC0;
    }

    private boolean isSpeedPotByte(final int receivedByte) {
        return (receivedByte & 0xC0) == 0x80;
    }

    private void processActiveJob() throws IOException {
        final Job job = this.jobQueue.peek();

        if (job == null) {
            return;
        }

        final CommandInfo commandInfo = job.command().getCommandInfo();
        final Class<Response> responseType = commandInfo.responseType();

        final ResponseConfiguration responseConfiguration = responseType.getAnnotation(ResponseConfiguration.class);
        final int expectedResponseByte = responseConfiguration.expectedResponseBytes();

        final byte[] buffer = new byte[expectedResponseByte];
        if (expectedResponseByte > 0) {
            try {
                readData(buffer);
            } catch (final SerialPortTimeoutException e) {
                return;
            }
        }

        final Response response;
        try {
            response = responseType.equals(StatusResponse.class)
                ? responseFactory.createResponse(buffer, wk2StatusMode)
                : responseFactory.createResponse(responseType, buffer);
        } catch (final IllegalArgumentException exception) {
            final String message = MessageFormat.format(
                "Failed to create response for active job {0}",
                job
            );

            throw new WKRuntimeException(message, exception);
        }
        completeActiveJob(response);
    }

    private void completeActiveJob(final Response response) {
        try {
            this.jobQueue.lock();

            final Job job = this.jobQueue.poll();
            final CompletableFuture<Response> responseHolder = job.response();

            responseHolder.complete(response);

            updateStatusMode(job.command());
        } finally {
            this.jobQueue.unlock();
        }
    }

    private void updateStatusMode(final Command command) {
        if (command instanceof SetWK2ModeCommand) {
            this.wk2StatusMode = true;
        } else if (command instanceof SetWK1ModeCommand) {
            this.wk2StatusMode = false;
        }
    }

    private int readData() throws IOException {
        final int data = this.inputStream.read();

        if (data < 0) {
            throw new WKRuntimeException("Connection to device broken");
        }

        return data;
    }

    private int readData(final byte[] buffer) throws IOException {
        final int receivedBytes = this.inputStream.read(buffer);

        if (receivedBytes < 0) {
            throw new WKRuntimeException("Connection to device broken");
        }

        if (receivedBytes < buffer.length) {
            final String message = MessageFormat.format(
                "Got only partial response. Expected {0}, received {1}",
                buffer.length,
                receivedBytes
            );

            throw new WKRuntimeException(message);
        }

        return receivedBytes;
    }

    private String formatError(final String pattern, final Object ... args) {
        return MessageFormat.format(pattern, args);
    }
}
