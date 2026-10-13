package de.do9fse.winkey.terminal;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.shell.CommandSession;

import de.do9fse.winkey.lib.core.model.ApplicationState;
import de.do9fse.winkey.lib.core.model.data.TextDataItem;

public class MorseCommand extends TerminalCommand {
    public MorseCommand() {
        super("morse", "cw");
    }

    @Override
    public String description() {
        return "Starts an interactive morse shell";
    }

    @Override
    public Object execute(CommandSession session, String[] args) throws Exception {
        super.execute(session, args);

        final LineReader lineReader = LineReaderBuilder
            .builder()
            .terminal(session.terminal())
            .build();

        if (scanner.hasNext()) {
            throw new IllegalArgumentException("Error: the morse command does not support parameters");
        }

        if (context.getState() != ApplicationState.READY) {
            throw new IllegalStateException(Errors.APPLICATION_MUST_BE_READY.createErrorMessage(context));
        }

        stdout.println("=== WinKey Interactive Morse Mode ===");
        stdout.println("\n");
        stdout.println("Type text with commands (e.g., cq cq cq do9fse@W500).");
        stdout.println("Type 'exit' or 'quit' to exit the morse mode.");

        while (true) {
            final String line = lineReader.readLine("morse>");

            if (line == null || line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("quit")) {
                break;
            }

            if (line.isBlank()) {
                continue;
            }

            final TextDataItem item = new TextDataItem(line.toUpperCase());
            context.getTransport().sendDataItems(item);
        }
        
        return null;
    }
}
