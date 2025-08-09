package io.github.StardewValley.controllers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.StardewValley.Main;
import io.github.StardewValley.models.GameAssetManager;
import io.github.StardewValley.models.LanguageManager;
import io.github.StardewValley.models.SavaToJson;
import io.github.StardewValley.models.User;
import io.github.StardewValley.views.MainMenu;
import io.github.StardewValley.views.ProfileMenu;

public class ProfileMenuController {

    private ProfileMenu view;
    private boolean wired = false,
        usernameDialogOpen = false,
        nicknameDialogOpen = false,
        emailDialogOpen    = false;

    public void setView(ProfileMenu v) {
        this.view = v;
        wireOnce();
    }

    private void wireOnce() {
        if (wired) return;
        wired = true;

        /* Change‑username */
        view.getChangeUsernameBtn().addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                showChangeUsernameDialog();
                view.updateInfoLabels();
            }
        });

        /* Change‑nickname */
        view.getChangeNicknameBtn().addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                showChangeNicknameDialog();
                view.updateInfoLabels();
            }
        });

        /* Change‑email */
        view.getChangeEmailBtn().addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                showChangeEmailDialog();
                view.updateInfoLabels();
            }
        });

        /* Change‑password */
        view.getChangePasswordBtn().addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                showChangePasswordDialog();
                view.updateInfoLabels();
            }
        });

        /* Delete account */
        view.getDeleteAccountBtn().addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                askSecurityAndDelete();
            }
        });

        /* Back */
        view.getBackButton().addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                Main.getMain().setScreen(new MainMenu(
                    new MainMenuController(),
                    GameAssetManager.getGameAssetManager().getDefaultSkin()));
            }
        });
    }

    // ------------------------------------------------------------------ //
    // Delete account (unchanged)
    // ------------------------------------------------------------------ //
    private void askSecurityAndDelete() {
        User me = User.getCurrentUser();
        if (me == null) {
            view.showError(LanguageManager.t("error.noUser"));
            return;
        }

        final TextField ans = new TextField("", view.getSkin());
        ans.setMessageText(LanguageManager.t("placeholder.answer"));

        Dialog d = new Dialog(LanguageManager.t("dialog.deleteAccountTitle"), view.getSkin()) {
            @Override protected void result(Object obj) {
                if (Boolean.TRUE.equals(obj)) {
                    if (me.getSecurityAnswer() == null || !me.getSecurityAnswer().equalsIgnoreCase(ans.getText().trim())) {
                        view.showError(LanguageManager.t("error.wrongAnswer"));
                        return;
                    }
                    SavaToJson.deleteUser(me.getUsername());
                    User.setCurrentUser(null);
                    Main.getMain().setScreen(new MainMenu(
                        new MainMenuController(),
                        GameAssetManager.getGameAssetManager().getDefaultSkin()));
                }
            }
        };

        d.text(me.getSecurityQuestion().toString());
        d.getContentTable().row();
        d.getContentTable().add(ans).width(300);
        d.button(LanguageManager.t("button.confirm"), true);
        d.button(LanguageManager.t("button.cancel"), false);
        d.key(Input.Keys.ESCAPE, false);
        d.show(view.getStage());
    }

    // ------------------------------------------------------------------ //
    // Username
    // ------------------------------------------------------------------ //
    private void showChangeUsernameDialog() {
        if (usernameDialogOpen) return;
        usernameDialogOpen = true;

        final TextField f = new TextField("", view.getSkin());
        f.setMessageText(LanguageManager.t("placeholder.newUsername"));

        Dialog d = new Dialog(LanguageManager.t("dialog.changeUsernameTitle"), view.getSkin()) {
            @Override protected void result(Object obj) {
                if (Boolean.TRUE.equals(obj)) {
                    String nu = f.getText().trim().toLowerCase();
                    if (nu.isEmpty()) {
                        view.showError(LanguageManager.t("error.emptyUsername"));
                        return;
                    }
                    if (SavaToJson.userExists(nu)) {
                        view.showError(LanguageManager.t("error.userExists"));
                        return;
                    }

                    User u = User.getCurrentUser();
                    String old = u.getUsername();
                    u.setUsername(nu);

                    SavaToJson.deleteUser(old);
                    SavaToJson.registerUser(u);
                    User.setCurrentUser(u);
                    view.showSuccess(LanguageManager.t("success.usernameChanged"));
                }
                usernameDialogOpen = false;
            }
        };

        d.text(LanguageManager.t("dialog.enterNewUsername"));
        d.getContentTable().row();
        d.getContentTable().add(f).width(300);
        d.button(LanguageManager.t("button.confirm"), true);
        d.button(LanguageManager.t("button.cancel"), false);
        d.key(Input.Keys.ESCAPE, false);
        d.show(view.getStage());
    }

    // ------------------------------------------------------------------ //
    // Nickname (NEW)
    // ------------------------------------------------------------------ //
    private void showChangeNicknameDialog() {
        if (nicknameDialogOpen) return;
        nicknameDialogOpen = true;

        final TextField f = new TextField("", view.getSkin());
        f.setMessageText(LanguageManager.t("placeholder.newNickname"));

        Dialog d = new Dialog(LanguageManager.t("dialog.changeNicknameTitle"), view.getSkin()) {
            @Override protected void result(Object obj) {
                if (Boolean.TRUE.equals(obj)) {
                    String nn = f.getText().trim();
                    if (nn.isEmpty()) {
                        view.showError(LanguageManager.t("error.emptyNickname"));
                        return;
                    }

                    User u = User.getCurrentUser();
                    u.setNickname(nn);
                    SavaToJson.save();
                    view.showSuccess(LanguageManager.t("success.nicknameChanged"));
                }
                nicknameDialogOpen = false;
            }
        };

        d.text(LanguageManager.t("dialog.enterNewNickname"));
        d.getContentTable().row();
        d.getContentTable().add(f).width(300);
        d.button(LanguageManager.t("button.confirm"), true);
        d.button(LanguageManager.t("button.cancel"), false);
        d.key(Input.Keys.ESCAPE, false);
        d.show(view.getStage());
    }

    // ------------------------------------------------------------------ //
    // Email (NEW)
    // ------------------------------------------------------------------ //
    private void showChangeEmailDialog() {
        if (emailDialogOpen) return;
        emailDialogOpen = true;

        final TextField f = new TextField("", view.getSkin());
        f.setMessageText(LanguageManager.t("placeholder.newEmail"));

        Dialog d = new Dialog(LanguageManager.t("dialog.changeEmailTitle"), view.getSkin()) {
            @Override protected void result(Object obj) {
                if (Boolean.TRUE.equals(obj)) {
                    String em = f.getText().trim().toLowerCase();
                    if (!em.matches("[\\w.+-]+@[\\w.-]+\\.[a-zA-Z]{2,}")) {
                        view.showError(LanguageManager.t("error.invalidEmail"));
                        return;
                    }
                    User u = User.getCurrentUser();
                    u.setEmail(em);
                    SavaToJson.save();
                    view.showSuccess(LanguageManager.t("success.emailChanged"));
                }
                emailDialogOpen = false;
            }
        };

        d.text(LanguageManager.t("dialog.enterNewEmail"));
        d.getContentTable().row();
        d.getContentTable().add(f).width(300);
        d.button(LanguageManager.t("button.confirm"), true);
        d.button(LanguageManager.t("button.cancel"), false);
        d.key(Input.Keys.ESCAPE, false);
        d.show(view.getStage());
    }

    // ------------------------------------------------------------------ //
    // Password (unchanged, but triggers info refresh)
    // ------------------------------------------------------------------ //
    private void showChangePasswordDialog() {
        TextField oldP = new TextField("", view.getSkin());
        TextField newP = new TextField("", view.getSkin());
        oldP.setPasswordMode(true); oldP.setPasswordCharacter('*');
        newP.setPasswordMode(true); newP.setPasswordCharacter('*');
        oldP.setMessageText(LanguageManager.t("placeholder.oldPassword"));
        newP.setMessageText(LanguageManager.t("placeholder.newPassword"));

        Dialog d = new Dialog(LanguageManager.t("dialog.changePasswordTitle"), view.getSkin()) {
            @Override protected void result(Object obj) {
                if (Boolean.TRUE.equals(obj)) {
                    if (!User.getCurrentUser().getPassword().equals(oldP.getText())) {
                        view.showError(LanguageManager.t("error.invalidPassword"));
                        return;
                    }
                    if (newP.getText().length() < 4) {
                        view.showError(LanguageManager.t("error.weakPassword"));
                        return;
                    }
                    User.getCurrentUser().setPassword(newP.getText());
                    SavaToJson.save();
                    view.showSuccess(LanguageManager.t("success.passwordChanged"));
                }
            }
        };

        d.getContentTable().add(oldP).width(300); d.getContentTable().row();
        d.getContentTable().add(newP).width(300);
        d.button(LanguageManager.t("button.confirm"), true);
        d.button(LanguageManager.t("button.cancel"), false);
        d.key(Input.Keys.ESCAPE, false);
        d.show(view.getStage());
    }
}
