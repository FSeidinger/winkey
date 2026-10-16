package de.do9fse.winkey.lib.core.model;

public record ApplicationConfiguration(boolean sendTextAsUppercase) { 
    public static final ApplicationConfiguration  DEFAULT = new ApplicationConfiguration(true);
}
