package io.github.StardewValley.models.enums;

public enum Gender {
    MAN,
    WOMAN,
    RATHER_NOT_SAY;

    public static Gender fromString(String str) {
        if (str == null) return null;
        return switch (str.trim().toLowerCase()) {
            case "man" -> MAN;
            case "woman" -> WOMAN;
            case "rather_not_say", "rather not say", "not_say", "prefer not to say" -> RATHER_NOT_SAY;
            default -> null;
        };
    }
}