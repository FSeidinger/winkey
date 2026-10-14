package de.do9fse.winkey.terminal;

import java.io.PrintStream;

import org.jline.shell.impl.DefaultCommandDispatcher;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;

public class ErrorHandlingCommandDispatcher extends DefaultCommandDispatcher {
    private String currentCommandLine;

    public ErrorHandlingCommandDispatcher(final Terminal terminal) {
        super(terminal);
    }

    public String getCurrentCommandLine() {
        return currentCommandLine;
    }
    
    @Override
    public Object execute(String line) throws Exception {
        try {
            this.currentCommandLine = line;
            return super.execute(line);
        } catch (final IllegalStateException | IllegalArgumentException e) {
            return handleError(e);
        }
    }

    private Object handleError(final Exception e) {
        final PrintStream stderr = session().err();
        final AttributedString attributedString = new AttributedStringBuilder()
            .style(errorStyle())
            .append(e.getMessage())
            .toAttributedString();
        stderr.println(attributedString);
        stderr.flush();

        return null;
    }
}
