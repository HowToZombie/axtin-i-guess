package org.axtin.util;

public class ArrayToMessage {
    private String string = "";

    public String getString() {
        return string;
    }

    public ArrayToMessage(String[] parts) {
        this.formatStrings(parts);
    }

    @Deprecated
    private void formatStrings(String[] parts) {
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            builder.append(part).append(" ");
        }
        this.string = builder.toString();
    }

    public static String formatStringsStatic(String[] parts) {
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            builder.append(part).append(" ");
        }
        return builder.toString();
    }
}
