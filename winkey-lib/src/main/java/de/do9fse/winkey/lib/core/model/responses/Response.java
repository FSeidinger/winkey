package de.do9fse.winkey.lib.core.model.responses;

public interface Response {
    static int toUnsignedInt(final byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("Bytes must not be null or empty");
        }

        return Byte.toUnsignedInt(bytes[0]);
    }

     static char toChar(final byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("Bytes must not be null or empty");
        }

        return (char) bytes[0];
    }


    static int[] toUnsignedInts(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        
        final int[] unsignedInts = new int[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            unsignedInts[i] = Byte.toUnsignedInt(bytes[i]);
        }
        
        return unsignedInts;
    }
}
