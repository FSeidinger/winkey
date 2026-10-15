package de.do9fse.winkey.terminal;

import org.jline.shell.CommandSession;

import de.do9fse.winkey.lib.core.model.ApplicationState;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommand;
import de.do9fse.winkey.lib.core.model.commands.admin.HostCloseCommand;
import de.do9fse.winkey.lib.core.model.configuration.serial.SerialConfiguration;
import de.do9fse.winkey.lib.core.port.out.Transport;

public class LeaveHostModeCommand extends TerminalCommand {
    public LeaveHostModeCommand() {
        super("leave-host-mode", "leave", "lh");
    }

    @Override
    public String description() {
        return "Leaves host mode and switches the WinKey device to standalone mode";
    }

    @Override
    public Object execute(CommandSession session, String[] args) throws Exception {
        super.execute(session, args);

        if (context.getState() == ApplicationState.CLOSED) {
            throw new IllegalStateException("Device is closed. Please call 'connect' first.");
        }

        if (context.getState() != ApplicationState.HOST_MODE) {
            throw new IllegalStateException("Device is not in host mode.");
        }

        final Transport transport = context.getTransport();
        final ImmediateCommand command = new HostCloseCommand();
        final SerialConfiguration configuration = context.getDeviceConfiguration();
        CommandHelper.executeCommand(transport, command, configuration.readTimeout());

        stdout.println("Successfully left host mode. Now in standalone mode");
        
        return null;
    }
}
