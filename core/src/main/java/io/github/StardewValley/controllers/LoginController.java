package io.github.StardewValley.controllers;

import io.github.StardewValley.Main;
import io.github.StardewValley.models.*;
import io.github.StardewValley.views.AuthView;
import io.github.StardewValley.views.LoginMenu;
import io.github.StardewValley.views.MainMenu;

public class LoginController {
    private LoginMenu view;

    public void setView(LoginMenu view) { this.view = view; }

    // Call this when “Login” is clicked (ideally wire a ClickListener in LoginMenu like you did in RegisterView)
    public void handleLogin() {
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
        if (!SavaToJson.verifyLogin(username, password)) {
            view.showError("Incorrect password.");
            return;
        }

        // Load a fresh copy from disk and set as current
        User.setCurrentUser(SavaToJson.getUser(username));
        view.showSuccess("Login successful.");

        Main.getMain().setScreen(new MainMenu(
            new MainMenuController(),
            GameAssetManager.getGameAssetManager().getDefaultSkin())
        );
    }

    // “Forgot password?” flow
    public void handleForgot() {
        String username = view.getUsernameField().getText().trim();
        if (username.isEmpty()) {
            view.showError("Enter your username first.");
            return;
        }
        if (!SavaToJson.userExists(username)) {
            view.showError("User not found.");
            return;
        }

        String question = SavaToJson.getSecurityQuestion(username);
        if (question == null) {
            view.showError("No security question set for this user.");
            return;
        }

        // Ask UI to show the question and collect the answer (your view already has a dialog)
        view.promptSecurityQuestion(question, userAnswer -> {
            boolean ok = SavaToJson.verifySecurityAnswer(username, userAnswer);
            if (!ok) {
                view.showError("Incorrect answer.");
                return;
            }
            // At this point you can allow password reset UI
            view.showSuccess("Answer verified. You can reset your password now.");
            view.promptNewPassword(newPass -> {
                if (newPass == null || newPass.isEmpty()) {
                    view.showError("Password cannot be empty.");
                    return;
                }
                if (!newPass.matches("(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!()_.*]).{8,}")) {
                    view.showError("Password must be 8+ chars, 1 uppercase, 1 number, 1 symbol.");
                    return;
                }
                // Persist new password
                SavaToJson.updatePassword(username, newPass);
                view.showSuccess("Password updated. Please log in.");
            });
        });
    }

    public void handleBack() {
        Main.getMain().setScreen(new AuthView(
            new AuthController(),
            GameAssetManager.getGameAssetManager().getAuthSkin())
        );
    }
}
