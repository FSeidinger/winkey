package de.do9fse.winkey.lib.core.model.data;

public record TextDataItem(String value) implements DataItem {
    @Override
    public byte[] toData() {
        return value.getBytes();
    }
}
