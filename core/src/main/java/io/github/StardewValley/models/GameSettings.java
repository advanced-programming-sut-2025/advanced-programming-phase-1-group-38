package io.github.StardewValley.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class GameSettings {

    /* ---------- صدایی ---------- */
    public float musicVolume = 1f;           // ۰ تا ۱
    public String currentTrack = "Track 1";
    public boolean sfxEnabled   = true;

    /* ---------- گیم‌پلی ---------- */
    public boolean autoReload   = false;

    /* ---------- جلوهٔ تصویر ---------- */
    public boolean grayscale    = false;

    /* ---------- زبان ---------- */
    public Language language    = Language.ENGLISH;

    /* ---------- کلیدها ---------- */
    public int keyMoveUp    = Input.Keys.W;
    public int keyMoveDown  = Input.Keys.S;
    public int keyMoveLeft  = Input.Keys.A;
    public int keyMoveRight = Input.Keys.D;

    /* ---------- ذخیره/بارگیری ---------- */
    private static final String FILE = "settings.json";
    private static GameSettings instance;

    public static GameSettings get() {
        if (instance == null) load();
        return instance;
    }

    public static void load() {
        FileHandle file = Gdx.files.local(FILE);
        if (file.exists()) {
            instance = new Json().fromJson(GameSettings.class, file);
        } else {
            instance = new GameSettings();   // مقادیر پیش‌فرض
            save();
        }
    }

    public static void save() {
        new Json().toJson(get(), Gdx.files.local(FILE));
    }
}
