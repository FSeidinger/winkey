package de.do9fse.winkey.lib.core.model;

public record WinKeyConfiguration(boolean sendTextAsUppercase) { 
    public static final WinKeyConfiguration  DEFAULT = new WinKeyConfiguration(true);
}
