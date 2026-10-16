package de.do9fse.winkey.lib.core.model;

public record StatusV2(CommonStatus commonStatus) implements Status {
    public static StatusV2 fromData(final int status) {
        return new StatusV2(
            CommonStatus.fromData(status)
        );
    }
}
