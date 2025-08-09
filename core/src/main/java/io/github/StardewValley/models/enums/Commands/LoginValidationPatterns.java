package io.github.StardewValley.models.enums.Commands;

import java.util.regex.Pattern;

public enum LoginValidationPatterns {
    VALID_USERNAME("^[a-zA-Z0-9_]{3,20}$"),
    VALID_PASSWORD("^[a-zA-Z0-9?<>,\"';:\\\\/|\\[\\] {}+=)(*&^%\\$#!]+$"),
    VALID_EMAIL("^(?!.*\\.\\.)[A-Za-z0-9](?:[A-Za-z0-9._-]*[A-Za-z0-9])?@(?:[A-Za-z0-9]" +
            "(?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z]{2,}$");

    private final Pattern pattern;

    LoginValidationPatterns(String regex) {
        this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }

    public boolean matches(String input) {
        return pattern.matcher(input).matches();
    }
}