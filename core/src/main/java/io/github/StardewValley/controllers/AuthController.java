package io.github.StardewValley.controllers;

import io.github.StardewValley.Main;
import io.github.StardewValley.models.GameAssetManager;
import io.github.StardewValley.views.AuthView;
import io.github.StardewValley.views.LoginMenu;
import io.github.StardewValley.views.MainMenu;
import io.github.StardewValley.views.RegisterView;

public class AuthController {
    private AuthView view;

    public void setView(AuthView view) {
        this.view = view;
    }

    public void initListeners() {
        if (view != null) {
            if (view.getLoginButton().isChecked()) {
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new LoginMenu(new LoginController(), GameAssetManager.getGameAssetManager().getDefaultSkin()));
            }

            if (view.getSignupButton().isChecked()) {
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new RegisterView(new RegisterController(), GameAssetManager.getGameAssetManager().getDefaultSkin()));
            }

            if (view.getGuestButton().isPressed()) {
//                User.setGuestUser();  // این متد رو باید تو کلاس User اضافه کنی

                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new MainMenu(new MainMenuController(), GameAssetManager.getGameAssetManager().getDefaultSkin()));
            }
        }
    }
}
