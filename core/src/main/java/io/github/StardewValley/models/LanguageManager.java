package io.github.StardewValley.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;

public class LanguageManager {
    private static Language current = Language.ENGLISH;
    private static I18NBundle bundle = loadBundle(current);

    private static I18NBundle loadBundle(Language lang) {
        return I18NBundle.createBundle(
            Gdx.files.internal("i18n/MyGame"),   // فولدر + پیشوند
            lang.locale);
    }

    public static void setLanguage(Language lang) {
        if (lang != current) {
            current = lang;
            bundle = loadBundle(lang);
        }
    }
    public static void updateLanguageFromSettings() {
        setLanguage(GameSettings.get().language);
    }
    public static Language getCurrent() { return current; }
    public static String t(String key)   { return bundle.get(key); }
}
