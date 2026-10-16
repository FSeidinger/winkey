package de.do9fse.winkey.lib.core.model;

public record StatusPushButton(boolean pb1Status, boolean pb2Status, boolean pb3Status, boolean pb4Status) implements Status {
    public static StatusPushButton fromData(final int status) {
        return new StatusPushButton(
            Status.filter(status, MASK_PB1),
            Status.filter(status, MASK_PB2),
            Status.filter(status, MASK_PB3),
            Status.filter(status, MASK_PB4)
        );
    }
}
