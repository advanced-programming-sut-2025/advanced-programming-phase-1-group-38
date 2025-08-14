package io.github.StardewValley.controllers;

import io.github.StardewValley.Main;
import io.github.StardewValley.models.GameAssetManager;
import io.github.StardewValley.models.SavaToJson;
import io.github.StardewValley.models.User;
import io.github.StardewValley.models.HashUtil;
import io.github.StardewValley.models.enums.Gender;
import io.github.StardewValley.views.AuthView;
import io.github.StardewValley.views.MainMenu;
import io.github.StardewValley.views.RegisterView;

public class RegisterController {
    private RegisterView view;

    public void setView(RegisterView view) {
        this.view = view;
    }

    public void handleRegister() {
        String username = view.getUsernameField().getText().trim();
        String nickname = view.getNicknameField().getText().trim();
        String email    = view.getEmailField().getText().trim();
        String password = view.getPasswordField().getText().trim();
        String confirm  = view.getConfirmPasswordField().getText().trim();

        // map localized gender safely:
        Gender gender = mapSelectedGender(view.getSelectedGender());

        if (username.isEmpty() || password.isEmpty()) { view.showError("Username and password are required."); return; }
        if (!isValidEmail(email)) { view.showError("Invalid email format."); return; }
        if (!password.equals(confirm)) { view.showError("Passwords do not match."); return; }
        if (SavaToJson.userExists(username)) { view.showError("Username already exists."); return; }
        if (!isStrongPassword(password)) { view.showError("Password must be 8+ chars, 1 uppercase, 1 number, 1 symbol."); return; }

        String hashedPassword = HashUtil.sha256(password);
        User user = new User(username, hashedPassword, nickname, email, gender);
        user.setHighestGold(0);
        user.setTotalGamesPlayed(0);

        view.showSecurityDialog(() -> {
            String question = view.getSelectedQuestion().trim();
            String answer   = view.getAnswerField().getText().trim();
            user.setSecurityQuestion(question, answer);

            try {
                SavaToJson.registerUser(user);
                User.setCurrentUser(user);
                view.showSuccess("Registered successfully!");
                Main.getMain().setScreen(new MainMenu(new MainMenuController(), GameAssetManager.getGameAssetManager().getDefaultSkin()));
            } catch (Exception ex) {
                view.showError("Failed to save user: " + ex.getMessage());
            }
        });
    }

    public void handleBack() {
        Main.getMain().setScreen(new AuthView(new AuthController(), GameAssetManager.getGameAssetManager().getAuthSkin()));
    }

    private Gender mapSelectedGender(String uiValue) {
        if (uiValue == null) return Gender.RATHER_NOT_SAY;
        String v = uiValue.trim().toLowerCase(java.util.Locale.ROOT);

        // whole-word match
        if (v.matches(".*\\bfemale\\b.*") || v.matches(".*\\bwoman\\b.*")) return Gender.WOMAN;
        if (v.matches(".*\\bmale\\b.*")   || v.matches(".*\\bman\\b.*"))   return Gender.MAN;
        return Gender.RATHER_NOT_SAY;
    }


    private boolean isValidEmail(String email) {

        String regex = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z]{2,})+$";

        return email.matches(regex);
    }

    private boolean isStrongPassword(String password) {
        return password.length() >= 8 &&
            password.matches(".*[A-Z].*") &&
            password.matches(".*\\d.*") &&
            password.matches(".*[@#$%^&+=!()_.*].*");
    }
}
