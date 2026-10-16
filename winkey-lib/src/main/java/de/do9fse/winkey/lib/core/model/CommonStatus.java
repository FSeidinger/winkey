package de.do9fse.winkey.lib.core.model;

public record CommonStatus(boolean xoffFlag, boolean breakinFlag, boolean busyFlag, boolean waitFlag) implements Status {
    public static CommonStatus fromData(final int status) {
        return new CommonStatus(
            Status.filter(status, MASK_XOFF),
            Status.filter(status, MASK_BREAKIN),
            Status.filter(status, MASK_BUSY),
            Status.filter(status, MASK_WAIT)
        );
    }
}
