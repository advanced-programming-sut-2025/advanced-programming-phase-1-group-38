package io.github.StardewValley.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.StardewValley.Main;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main (String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // macOS helper
        createApplication();
    }

    private static Lwjgl3Application createApplication () {
        return new Lwjgl3Application(new Main(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration () {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("Stardew Valley");
        cfg.useVsync(true);
        cfg.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        cfg.setWindowedMode(1280, 720);
        cfg.setWindowIcon("libgdx128.png", "libgdx64.png",
            "libgdx32.png",  "libgdx16.png");
        return cfg;
    }
}
