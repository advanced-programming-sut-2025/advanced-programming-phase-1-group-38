package io.github.StardewValley.controllers;

import io.github.StardewValley.Main;
import io.github.StardewValley.models.*;
import io.github.StardewValley.views.AuthView;
import io.github.StardewValley.views.LoginMenu;
import io.github.StardewValley.views.MainMenu;


public class LoginController {
    private LoginMenu view;

    public void setView(LoginMenu view) {
        this.view = view;
    }

    public Result checkSecurityAnswer(String userAnswer) {
        User user = App.getLoggedInUser();

        if (user == null)
            return new Result(false, "No user is currently logged in.");

        if (user.getSecurityQuestion() == null)
            return new Result(false, "Security question is not set.");

        String correctAnswer = user.getSecurityQuestion().getAnswer();

        if (correctAnswer.equalsIgnoreCase(userAnswer)) {
            return new Result(true, "Correct answer.");
        } else {
            return new Result(false, "Incorrect answer.");
        }
    }

    public void handleLogin() {
        if (view.getLoginButton().isPressed()) {
            String username = view.getUsernameField().getText().trim();
            String password = view.getPasswordField().getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                view.showError("Username and password are required.");
                return;
            }

            if (!SavaToJson.userExists(username)) {
                view.showError("User not found.");
                return;
            }

            if (!SavaToJson.validateLogin(username, password)) {
                view.showError("Incorrect password.");
                return;
            }

            User.setCurrentUser(SavaToJson.getUser(username));

            view.showSuccess("Login successful.");

            Main.getMain().setScreen(new MainMenu(
                new MainMenuController(),
                GameAssetManager.getGameAssetManager().getDefaultSkin())
            );
        }

        if (view.getBackButton().isPressed()) {
            Main.getMain().setScreen(new AuthView(
                new AuthController(),
                GameAssetManager.getGameAssetManager().getAuthSkin())
            );
        }


        if (view.getForgotButton().isPressed()) {
            view.promptSecurityQuestion();
        }
    }
}
