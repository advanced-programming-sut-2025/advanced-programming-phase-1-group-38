package io.github.StardewValley.models;

import java.util.Locale;

public enum Language {
    ENGLISH("en"),
    FRENCH("fr"),
    GERMAN("de");

    public final Locale locale;

    Language(String code) {
        this.locale = new Locale(code);
    }

    @Override
    public String toString() {
        switch (this) {
            case ENGLISH: return "English";
            case FRENCH:  return "Français";
            case GERMAN:  return "Deutsch";
            default:      return "Unknown";
        }
    }
}
