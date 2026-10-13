package de.do9fse.winkey.terminal;

import org.jline.shell.CommandSession;

import de.do9fse.winkey.lib.core.model.WinKeyState;
import de.do9fse.winkey.lib.core.model.commands.WinKeyImmediateCommand;
import de.do9fse.winkey.lib.core.model.commands.admin.HostCloseCommand;
import de.do9fse.winkey.lib.core.model.configuration.serial.DeviceConfiguration;
import de.do9fse.winkey.lib.core.port.out.WinKeyTransport;

public class LeaveHostModeCommand extends TerminalCommand {
    public LeaveHostModeCommand() {
        super("leave-host-mode", "leave", "lh");
    }

    @Override
    public String description() {
        return "Switches the WinKey device to host mode";
    }

    @Override
    public Object execute(CommandSession session, String[] args) throws Exception {
        super.execute(session, args);

        if (context.getState() != WinKeyState.READY) {
            throw new IllegalStateException("Device is closed. Please call 'connect' first.");
        }

        final WinKeyTransport transport = context.getTransport();
        final WinKeyImmediateCommand command = new HostCloseCommand();
        final DeviceConfiguration configuration = context.getDeviceConfiguration();
        CommandHelper.executeCommand(transport, command, configuration.readTimeout());

        stdout.println("Successfully left host mode. Now in standalone mode");
        
        return null;
    }
}
