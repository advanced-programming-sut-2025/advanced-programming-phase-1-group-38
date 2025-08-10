package io.github.StardewValley.controllers;

import io.github.StardewValley.Main;
import io.github.StardewValley.models.GameAssetManager;
import io.github.StardewValley.views.AuthView;
import io.github.StardewValley.views.MainMenu;
import io.github.StardewValley.views.PlayerMapView;
import io.github.StardewValley.views.ProfileMenu;

public class MainMenuController {
    private MainMenu view;

    public void setView(MainMenu view) {
        this.view = view;
    }

    public void handleButtons() {
        if (view.getProfileButton().isPressed()) {
            Main.getMain().setScreen(new ProfileMenu(new ProfileMenuController(), GameAssetManager.getGameAssetManager().getDefaultSkin()));
        }

        if (view.getPlayButton().isPressed()) {
            String[] chosenMaps = {"maps/OutdoorMap1.tmx", "maps/OutdoorMap1.tmx", "maps/OutdoorMap1.tmx", "maps/OutdoorMap1.tmx"};
            WorldController wc = new WorldController(chosenMaps);
            Main.getMain().setScreen(new PlayerMapView(wc, 0));
        }

        if (view.getLogoutButton().isPressed()) {
            Main.getMain().setScreen(new AuthView(new AuthController(), GameAssetManager.getGameAssetManager().getAuthSkin()));
        }
    }
}
