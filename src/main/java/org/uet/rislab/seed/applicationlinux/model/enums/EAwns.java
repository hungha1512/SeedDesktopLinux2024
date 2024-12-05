package org.uet.rislab.seed.applicationlinux.model.enums;

public enum EAwns {
    ABSENT("Không"), PRESENT("Có");

    private String awns;

    EAwns(String awns) {
        this.awns = awns;
    }

    /**
     * Converts a string value to the corresponding EAwns enum constant.
     *
     * @param value the string value to convert
     * @return the corresponding EAwns constant, or null if no match is found
     */
    public static EAwns fromValue(String value) {
        for (EAwns e : EAwns.values()) {
            if (e.awns.equals(value)) {
                return e;
            }
        }
        return null;
    }
}
