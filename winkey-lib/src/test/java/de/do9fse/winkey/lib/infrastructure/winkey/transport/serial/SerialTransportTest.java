package de.do9fse.winkey.lib.infrastructure.winkey.transport.serial;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortTimeoutException;

import de.do9fse.winkey.lib.core.model.ApplicationContext;
import de.do9fse.winkey.lib.core.model.Job;
import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.ApplicationState;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommand;
import de.do9fse.winkey.lib.core.model.commands.admin.EchoTestCommand;
import de.do9fse.winkey.lib.core.model.commands.admin.HostOpenCommand;
import de.do9fse.winkey.lib.core.model.commands.admin.SetWK1ModeCommand;
import de.do9fse.winkey.lib.core.model.commands.admin.SetWK2ModeCommand;
import de.do9fse.winkey.lib.core.model.commands.host.immediate.BackspaceCommand;
import de.do9fse.winkey.lib.core.model.commands.host.immediate.RequestStatusCommand;
import de.do9fse.winkey.lib.core.model.commands.host.buffered.NopCommand;
import de.do9fse.winkey.lib.core.model.commands.host.buffered.PttCommand;
import de.do9fse.winkey.lib.core.model.data.TextDataItem;
import de.do9fse.winkey.lib.core.model.configuration.serial.SerialConfiguration;
import de.do9fse.winkey.lib.core.model.error.WKRuntimeException;
import de.do9fse.winkey.lib.core.model.responses.SpeedPotValueResponse;
import de.do9fse.winkey.lib.core.model.responses.Response;
import de.do9fse.winkey.lib.core.model.responses.StatusResponse;
import de.do9fse.winkey.lib.core.port.out.JobQueue;
import de.do9fse.winkey.lib.infrastructure.winkey.DefaultJobQueue;

@DisplayName("Given the serial Transport")
class SerialTransportTest {
    private ApplicationContext context;

    @BeforeEach
    void setUp() {
        this.context = new ApplicationContext(ProtocolVersion.V2);
        this.context.setDeviceConfiguration(SerialConfiguration.DEFAULT);
    }

    @Nested
    @DisplayName("lifecycle")
    class Lifecycle {
        @Test
        void openMovesToInitializingWithoutSendingOrStartingReader() throws Exception {
            final SerialPort serialPort = mockSerialPort(new ByteArrayInputStream(new byte[0]), new ByteArrayOutputStream());
            final SerialTransport transport = new SerialTransport(context, serialPort);

            transport.open();

            assertEquals(ApplicationState.INITIALIZING, context.getState());
            assertNull(getField(transport, "serialPortReaderThread"));
            verify(serialPort).openPort();
        }

        @Test
        void rejectsCommandSubmissionUntilReady() throws Exception {
            final DefaultJobQueue queue = new DefaultJobQueue();
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final SerialPort serialPort = mockSerialPort(new ByteArrayInputStream(new byte[0]), outputStream);
            final SerialTransport transport = new SerialTransport(context, serialPort);
            transport.open();

            assertThrows(
                IllegalStateException.class,
                () -> transport.submitJob(new BackspaceCommand(), new CompletableFuture<>())
            );

            assertNull(queue.peek());
            assertEquals(0, outputStream.size());

            transport.close();
            assertEquals(ApplicationState.CLOSED, context.getState());
        }

        @Test
        void rejectsSendingDataItemsUntilReady() throws Exception {
            final SerialPort serialPort = mockSerialPort(new ByteArrayInputStream(new byte[0]), new ByteArrayOutputStream());
            final SerialTransport transport = new SerialTransport(context, serialPort);
            transport.open();

            assertThrows(
                IllegalStateException.class,
                () -> transport.sendDataItems(new NopCommand())
            );

            transport.close();
        }

        @Test
        void initializeRepeatsEchoUntilTheExpectedResponseAndStartsReader() throws Exception {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final SerialPort serialPort = mockSerialPort(
                new ByteArrayInputStream(new byte[] { '?', 'R' }),
                outputStream
            );
            final SerialTransport transport = new SerialTransport(context, serialPort);
            transport.open();

            transport.initialize(1, TimeUnit.SECONDS);

            final byte[] echoBytes = new EchoTestCommand('R').toData();
            final byte[] expectedWrites = new byte[echoBytes.length * 2];
            System.arraycopy(echoBytes, 0, expectedWrites, 0, echoBytes.length);
            System.arraycopy(echoBytes, 0, expectedWrites, echoBytes.length, echoBytes.length);
            assertArrayEquals(expectedWrites, outputStream.toByteArray());
            assertEquals(ApplicationState.READY, context.getState());
            assertTrue((Thread) getField(transport, "serialPortReaderThread") != null);
        }

        @Test
        void leavesTransportInitializingWhenHandshakeTimesOut() throws Exception {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final SerialPortTimeoutException timeoutException = mock(SerialPortTimeoutException.class);
            final InputStream timeoutInput = new InputStream() {
                @Override
                public int read() throws IOException {
                    try {
                        Thread.sleep(2);
                    } catch (final InterruptedException exception) {
                        Thread.currentThread().interrupt();
                        throw new IOException(exception);
                    }

                    throw timeoutException;
                }
            };
            final SerialPort serialPort = mockSerialPort(timeoutInput, outputStream);
            final SerialTransport transport = new SerialTransport(context, serialPort);
            transport.open();

            assertThrows(TimeoutException.class, () -> transport.initialize(10, TimeUnit.MILLISECONDS));

            assertEquals(ApplicationState.INITIALIZING, context.getState());
            assertTrue(outputStream.size() > 0);
            assertNull(getField(transport, "serialPortReaderThread"));
        }

        @Test
        void closeFromInitializingReturnsToClosed() throws Exception {
            final SerialPort serialPort = mockSerialPort(new ByteArrayInputStream(new byte[0]), new ByteArrayOutputStream());
            final SerialTransport transport = new SerialTransport(context, serialPort);
            transport.open();

            transport.close();

            assertEquals(ApplicationState.CLOSED, context.getState());
            verify(serialPort).closePort();
        }
    }

    @Test
    void removesOnlyTheLastQueuedJobWhenSendingFails() throws Exception {
        final DefaultJobQueue queue = new DefaultJobQueue();
        final SerialTransport transport = createReadyTransport(queue, new OutputStream() {
            @Override
            public void write(final int value) throws IOException {
                throw new IOException("Write failed");
            }
        });
        final CompletableFuture<Response> firstResult = new CompletableFuture<>();
        final Job firstJob = new Job(new BackspaceCommand(), firstResult);
        queue.offer(firstJob);

        assertThrows(
            WKRuntimeException.class,
            () -> transport.submitJob(new HostOpenCommand(), new CompletableFuture<>())
        );

        assertSame(firstJob, queue.peek());
        assertSame(firstJob, queue.poll());
        assertNull(queue.poll());
    }

    @Test
    void completesCommandsWithoutResponseImmediatelyAndDoesNotConsumeNextResponseJob() throws Exception {
        final SerialTransport transport = createReadyTransport(0x23);
        final CompletableFuture<Response> noResponse = new CompletableFuture<>();
        transport.submitJob(new BackspaceCommand(), noResponse);
        assertFalse(noResponse.isDone());
        processActiveJob(transport);
        assertInstanceOf(de.do9fse.winkey.lib.core.model.responses.EmptyResponse.class, noResponse.join());

        final CompletableFuture<Response> hostOpenResult = submit(transport, new HostOpenCommand());
        handleStatusBytes(transport);
        processActiveJob(transport);

        assertEquals(3, assertInstanceOf(
            de.do9fse.winkey.lib.core.model.responses.VersionResponse.class,
            hostOpenResult.join()
        ).majorVersion());
    }

    @Test
    void failsAllPendingJobsWhenTheReaderStops() {
        final JobQueue queue = new DefaultJobQueue();
        final CompletableFuture<Response> firstResult = new CompletableFuture<>();
        final CompletableFuture<Response> secondResult = new CompletableFuture<>();
        queue.offer(new Job(new HostOpenCommand(), firstResult));
        queue.offer(new Job(new HostOpenCommand(), secondResult));

        queue.failPendingJobs(new IllegalStateException("Reader stopped"));

        assertTrue(firstResult.isCompletedExceptionally());
        assertTrue(secondResult.isCompletedExceptionally());
    }

    @Test
    void leavesUnsolicitedStatusForLoggingAndCompletesHostOpenFromVersionByte() throws Exception {
        final SerialTransport transport = createReadyTransport(0xd7, 0x9f, 0x23);
        final List<Response> unsolicitedResponses = new ArrayList<>();
        transport.addUnsolicitedResponseListener(unsolicitedResponses::add);
        final CompletableFuture<Response> result = submit(transport, new HostOpenCommand());

        handleStatusBytes(transport);
        assertFalse(result.isDone());

        handleStatusBytes(transport);
        assertFalse(result.isDone());

        handleStatusBytes(transport);
        processActiveJob(transport);

        assertEquals(3, assertInstanceOf(
            de.do9fse.winkey.lib.core.model.responses.VersionResponse.class,
            result.join()
        ).majorVersion());
        assertEquals(2, unsolicitedResponses.size());
        assertInstanceOf(StatusResponse.class, unsolicitedResponses.get(0));
        assertInstanceOf(SpeedPotValueResponse.class, unsolicitedResponses.get(1));
    }

    @Test
    void doesNotNotifyRemovedUnsolicitedResponseListener() throws Exception {
        final SerialTransport transport = createReadyTransport(0xd7);
        final List<Response> unsolicitedResponses = new ArrayList<>();
        final de.do9fse.winkey.lib.core.port.in.UnsolicitedResponseListener listener =
            unsolicitedResponses::add;
        transport.addUnsolicitedResponseListener(listener);
        transport.removeUnsolicitedResponseListener(listener);

        handleStatusBytes(transport);

        assertTrue(unsolicitedResponses.isEmpty());
    }

    @Test
    void changesStatusModeWhenReaderProcessesModeCommands() throws Exception {
        final SerialTransport transport = createReadyTransport(0xc8, 0xc8);
        final List<Response> unsolicitedResponses = new ArrayList<>();
        transport.addUnsolicitedResponseListener(unsolicitedResponses::add);

        final CompletableFuture<Response> wk2Result = submit(transport, new SetWK2ModeCommand());
        processActiveJob(transport);
        assertInstanceOf(de.do9fse.winkey.lib.core.model.responses.EmptyResponse.class, wk2Result.join());
        handleStatusBytes(transport);

        final CompletableFuture<Response> wk1Result = submit(transport, new SetWK1ModeCommand());
        processActiveJob(transport);
        assertInstanceOf(de.do9fse.winkey.lib.core.model.responses.EmptyResponse.class, wk1Result.join());
        handleStatusBytes(transport);

        final StatusResponse wk2Status = assertInstanceOf(StatusResponse.class, unsolicitedResponses.get(0));
        assertInstanceOf(
            de.do9fse.winkey.lib.core.model.responses.StatusResponseWK2.class,
            unsolicitedResponses.get(0)
        );
        assertTrue(wk2Status.pushButtonStatus());
        assertFalse(wk2Status.keyDown());

        final StatusResponse wk1Status = assertInstanceOf(StatusResponse.class, unsolicitedResponses.get(1));
        assertInstanceOf(
            de.do9fse.winkey.lib.core.model.responses.StatusResponseWK1.class,
            unsolicitedResponses.get(1)
        );
        assertFalse(wk1Status.pushButtonStatus());
        assertTrue(wk1Status.keyDown());
    }

    @Test
    void createsActiveStatusResponseUsingTheCurrentWkMode() throws Exception {
        final SerialTransport transport = createReadyTransport(0xdf);

        final CompletableFuture<Response> wk2ModeResult = submit(transport, new SetWK2ModeCommand());
        processActiveJob(transport);
        assertInstanceOf(
            de.do9fse.winkey.lib.core.model.responses.EmptyResponse.class,
            wk2ModeResult.join()
        );

        final CompletableFuture<Response> result = submit(transport, new RequestStatusCommand());
        processActiveJob(transport);

        final StatusResponse statusResponse = assertInstanceOf(StatusResponse.class, result.join());
        assertInstanceOf(
            de.do9fse.winkey.lib.core.model.responses.StatusResponseWK2.class,
            statusResponse
        );
        assertTrue(statusResponse.pushButtonStatus());
    }

    @Test
    void sendsDataItemsInOrder() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final SerialTransport transport = createReadyTransport(
            new DefaultJobQueue(),
            outputStream
        );

        transport.sendDataItems(
            new NopCommand(),
            new TextDataItem("CQ"),
            new PttCommand(true)
        );

        assertArrayEquals(
            new byte[] { 0x1f, 'C', 'Q', 0x18, 0x01 },
            outputStream.toByteArray()
        );
    }

    private static SerialPort mockSerialPort(final InputStream inputStream, final OutputStream outputStream) throws Exception {
        final SerialPort serialPort = mock(SerialPort.class);
        when(serialPort.openPort()).thenReturn(true);
        when(serialPort.getInputStream()).thenReturn(inputStream);
        when(serialPort.getOutputStream()).thenReturn(outputStream);
        return serialPort;
    }

    private static SerialTransport createReadyTransport(final int... receivedBytes) throws Exception {
        return createReadyTransport(new DefaultJobQueue(), new ByteArrayOutputStream(), receivedBytes);
    }

    private static SerialTransport createReadyTransport(
        final DefaultJobQueue queue,
        final OutputStream outputStream,
        final int... receivedBytes
    ) throws Exception {
        final byte[] inputBytes = new byte[receivedBytes.length];
        for (int i = 0; i < receivedBytes.length; i++) {
            inputBytes[i] = (byte) receivedBytes[i];
        }

        final ApplicationContext context = new ApplicationContext(ProtocolVersion.V2);
        context.setDeviceConfiguration(SerialConfiguration.DEFAULT);
        context.transitionToState(ApplicationState.INITIALIZING);
        context.transitionToState(ApplicationState.READY);

        final SerialTransport transport = new SerialTransport(context, mock(SerialPort.class));
        setField(transport, "inputStream", new PushbackInputStream(new ByteArrayInputStream(inputBytes)));
        setField(transport, "outputStream", outputStream);
        return transport;
    }

    private static CompletableFuture<Response> submit(
        final SerialTransport transport,
        final ImmediateCommand command
    ) throws Exception {
        final CompletableFuture<Response> result = new CompletableFuture<>();
        transport.submitJob(command, result);
        return result;
    }

    private static void handleStatusBytes(final SerialTransport transport) throws Exception {
        final Method method = SerialTransport.class.getDeclaredMethod("handleStatusBytes");
        method.setAccessible(true);
        method.invoke(transport);
    }

    private static void processActiveJob(final SerialTransport transport) throws Exception {
        final Method method = SerialTransport.class.getDeclaredMethod("processActiveJob");
        method.setAccessible(true);
        method.invoke(transport);
    }

    private static void setField(
        final SerialTransport transport,
        final String fieldName,
        final Object value
    ) throws Exception {
        final Field field = SerialTransport.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(transport, value);
    }

    private static Object getField(final SerialTransport transport, final String fieldName) throws Exception {
        final Field field = SerialTransport.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(transport);
    }
}
