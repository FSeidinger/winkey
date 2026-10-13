package de.do9fse.winkey.lib.core.model.commands;

/**
 * The base interface for all commands
 *
 * <p>
 * The commands are grouped into Admin, Host and Other commands. For each
 * of this groups there is an interface extending this base interface.
 * <p>
 *
 * <p>
 * A command itself is a Java record implementing its group interface.
 * </p>
 *
 * <p>
 * A valid command is expected to have a valid
 * {@link de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration CommandConfiguration}
 * annotation.
 * </p>
 *
 * @param <R> The type of response expected for this command or
 *     {@link de.do9fse.winkey.lib.core.model.responses.EmptyResponse EmptyResponse} if no response is expected.
 */
public interface Command {
    /**
     * The payload bytes used to transmit this command via WinKey protocol to
     * a WinKey device
     *
     * <p>
     * Commands are expected to override this method to deliver the payload
     * bytes.
     * <P>
     *
     * <P>
     * In the case of grouped commands, e.g.
     * {@link de.do9fse.winkey.lib.core.model.commands.AdminCommand AdminCommand},
     * the {@link #getPayloadBytes()} method only delivers the admin prefix
     * byte. In this example the byte 0x00.
     *
     * @return The payload bytes of this command
     */
    byte[] getPayloadBytes();

    /**
     * The fully assembled payload
     *
     * @return Fully assembled payload
     */
    default byte[] toProtocolBytes() {
        return getPayloadBytes();
    }
}
