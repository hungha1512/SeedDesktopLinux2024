package org.uet.rislab.seed.applicationlinux.model.enums;

public enum EColor {
    YELLOW("Vàng"), BROWN("Nâu"), RED("Đỏ"), PURPLE("Tím"), BLACK("Đen");

    private String color;

    EColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    /**
     * Converts a string value to the corresponding EColor enum constant.
     *
     * @param value the string value to convert
     * @return the corresponding EColor constant, or null if no match is found
     */
    public static EColor fromValue(String value) {
        for (EColor e : EColor.values()) {
            if (e.color.equals(value)) {
                return e;
            }
        }
        return null;
    }
}
