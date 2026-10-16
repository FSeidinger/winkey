package de.do9fse.winkey.lib.core.model;

public record StatusV1(CommonStatus commonStatus, boolean keyDown) implements Status {
    public static StatusV1 fromData(final int status) {
        return new StatusV1(
            CommonStatus.fromData(status),
            Status.filter(status, MASK_KEYDOWN)
        );
    }
}
