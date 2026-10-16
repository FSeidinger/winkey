package de.do9fse.winkey.terminal;

import org.jline.shell.CommandSession;

import de.do9fse.winkey.lib.core.model.ApplicationState;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommand;
import de.do9fse.winkey.lib.core.model.commands.admin.HostOpenCommand;
import de.do9fse.winkey.lib.core.model.configuration.serial.SerialConfiguration;
import de.do9fse.winkey.lib.core.port.out.Transport;

public class EnterHostModeCommand extends TerminalCommand {
    public EnterHostModeCommand() {
        super("enter-host-mode", "enter", "eh");
    }

    @Override
    public String description() {
        return "Switches the device to the host mode";
    }

    @Override
    public Object execute(CommandSession session, String[] args) throws Exception {
        super.execute(session, args);

        if (context.getState() == ApplicationState.HOST_MODE) {
            throw new IllegalStateException("Device is already in host mode.");
        }

        if (context.getState() != ApplicationState.STANDALONE_MODE) {
            throw new IllegalStateException(Errors.APPLICATION_MUST_BE_READY.createErrorMessage(context));
        }

        final Transport transport = context.getTransport();
        final ImmediateCommand command = new HostOpenCommand();
        final SerialConfiguration configuration = context.getDeviceConfiguration();
        CommandHelper.executeCommand(transport, command, configuration.readTimeout());

        stdout.println("Successfully entered host mode. Device reported version " + context.getDeviceVersion().getVersionString());
        
        return null;
    }
}
