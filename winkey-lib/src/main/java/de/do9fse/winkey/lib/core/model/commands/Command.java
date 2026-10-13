package de.do9fse.winkey.lib.core.model.commands;

import de.do9fse.winkey.lib.core.port.out.DataOutputSerializer;

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
public interface Command  extends DataOutputSerializer { }
