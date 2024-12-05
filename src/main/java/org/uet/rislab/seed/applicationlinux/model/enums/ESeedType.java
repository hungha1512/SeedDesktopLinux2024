package org.uet.rislab.seed.applicationlinux.model.enums;

public enum ESeedType {
    LONG("Hạt dài"), SHORT("Hạt thường"), ROUND("Hạt tròn");

    private String type;

    ESeedType(String type) {
        this.type = type;
    }

    /**
     * Converts a string value to the corresponding ESeedType enum constant.
     *
     * @param value the string value to convert
     * @return the corresponding ESeedType constant, or null if no match is found
     */
    public static ESeedType fromValue(String value) {
        for (ESeedType e : ESeedType.values()) {
            if (e.type.equals(value)) {
                return e;
            }
        }
        return null;
    }
}
