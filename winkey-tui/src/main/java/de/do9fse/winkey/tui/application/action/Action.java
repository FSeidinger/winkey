package de.do9fse.winkey.tui.application.action;

public enum Action {
    FILE_EXIT("menu.file.exit"),
    NO_ACTION("global.NoAction");

    private String id;

    private Action(final String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }
}
