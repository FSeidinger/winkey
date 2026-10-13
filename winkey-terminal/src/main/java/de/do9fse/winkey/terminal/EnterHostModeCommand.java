package de.do9fse.winkey.terminal;

import org.jline.shell.CommandSession;

import de.do9fse.winkey.lib.core.model.WinKeyState;
import de.do9fse.winkey.lib.core.model.commands.WinKeyImmediateCommand;
import de.do9fse.winkey.lib.core.model.commands.admin.HostOpenCommand;
import de.do9fse.winkey.lib.core.model.configuration.serial.DeviceConfiguration;
import de.do9fse.winkey.lib.core.port.out.WinKeyTransport;

public class EnterHostModeCommand extends TerminalCommand {
    public EnterHostModeCommand() {
        super("enter-host-mode", "enter", "eh");
    }

    @Override
    public String description() {
        return "Switches the WinKey device to the host mode";
    }

    @Override
    public Object execute(CommandSession session, String[] args) throws Exception {
        super.execute(session, args);

        if (context.getState() != WinKeyState.READY) {
            throw new IllegalStateException("Device is closed. Please call 'connect' first.");
        }

        final WinKeyTransport transport = context.getTransport();
        final WinKeyImmediateCommand command = new HostOpenCommand();
        final DeviceConfiguration configuration = context.getDeviceConfiguration();
        CommandHelper.executeCommand(transport, command, configuration.readTimeout());

        stdout.println("Successfully entered host mode");
        
        return null;
    }
}
