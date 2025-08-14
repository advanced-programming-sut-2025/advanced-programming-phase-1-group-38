// ============================================================================
// LoginMenu.java — Stylish login screen with animated card, background image,
// hover effects, and reset-password dialog with validation.
// Matches look & feel of MainMenu and ProfileMenu.
// ============================================================================
package io.github.StardewValley.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.StardewValley.controllers.LoginController;
import io.github.StardewValley.models.LanguageManager;
import io.github.StardewValley.models.SavaToJson;
import io.github.StardewValley.models.User;

import java.util.function.Consumer;

public class LoginMenu implements Screen {

    private Stage stage;
    private Image bgImage;
    private float bgOffsetX = 0f;

    private final Skin skin;
    private final LoginController controller;

    private final TextField usernameField;
    private final TextField passwordField;
    private final TextButton loginButton;
    private final TextButton backButton;
    private final TextButton forgotButton;
    private final Label messageLabel;

    private boolean dialogOpen = false;

    public LoginMenu(LoginController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;
        this.usernameField = new TextField("", skin);
        this.passwordField = new TextField("", skin);
        this.loginButton   = new TextButton("", skin);
        this.backButton    = new TextButton("", skin);
        this.forgotButton  = new TextButton("", skin);
        this.messageLabel  = new Label("", skin);
        messageLabel.setColor(Color.RED);
        messageLabel.setVisible(false);
        controller.setView(this);
    }

    @Override public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Texture bgTex = new Texture(Gdx.files.internal("menu2.png"));
        bgTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        bgImage = new Image(bgTex);
        bgImage.setFillParent(true);               // کل صفحه را پر کند
        bgImage.setScaling(Scaling.fill);          // اگر می‌خواهید تناسب حفظ شود از Scaling.fit استفاده کنید
        stage.addActor(bgImage);

        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        Table card = new Table();
        Drawable cardBg = skin.has("card-transparent", Drawable.class)
            ? skin.getDrawable("card-transparent")
            : new TextureRegionDrawable(new TextureRegion(bgTex));
        card.setBackground(cardBg);
        card.setColor(1f, 1f, 1f, 0.7f);
        card.defaults().pad(8);

        card.add(new Label(LanguageManager.t("button.login"), skin)).padBottom(20).row();
        card.add(usernameField).width(300).pad(5).row();
        card.add(passwordField).width(300).pad(5).row();
        card.add(loginButton).width(260).pad(10).row();
        card.add(forgotButton).width(260).pad(5).row();
        card.add(backButton).width(260).pad(5).row();
        card.add(messageLabel)  .padTop(10).row();

        Container<Table> container = new Container<>(card);
        container.setFillParent(true);
        container.center();
        stage.addActor(container);

        card.getColor().a = 0f;
        card.addAction(Actions.fadeIn(0.6f));

        refreshTexts();

        addHover(loginButton);
        addHover(backButton);
        addHover(forgotButton);

        loginButton.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                controller.handleLogin();
            }
        });
        backButton.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                controller.handleBack();
            }
        });
        forgotButton.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                controller.handleForgot();
            }
        });

    }

    private Texture loadBackgroundTexture() {
        if (Gdx.files.internal("mainmenu/background.png").exists())
            return new Texture(Gdx.files.internal("mainmenu/background.png"));
        Pixmap pm = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pm.setColor(0.09f, 0.09f, 0.09f, 1f);
        pm.fill();
        Texture tex = new Texture(pm);
        pm.dispose();
        return tex;
    }

    private void refreshTexts() {
        usernameField.setMessageText(LanguageManager.t("placeholder.username"));
        passwordField.setMessageText(LanguageManager.t("placeholder.password"));
        loginButton.setText(LanguageManager.t("button.login"));
        backButton.setText(LanguageManager.t("button.back"));
        forgotButton.setText(LanguageManager.t("button.forgot"));
        messageLabel.setText("");
    }

    private void addHover(final TextButton btn) {
        btn.setTransform(true);
        final Color base = new Color(btn.getColor());
        btn.addListener(new InputListener() {
            @Override public void enter(InputEvent e, float x, float y, int p, Actor a) {
                btn.addAction(Actions.parallel(
                    Actions.scaleTo(1.05f, 1.05f, 0.1f),
                    Actions.color(base.cpy().lerp(Color.WHITE, 0.3f), 0.1f)));
            }
            @Override public void exit(InputEvent e, float x, float y, int p, Actor a) {
                btn.addAction(Actions.parallel(
                    Actions.scaleTo(1f, 1f, 0.1f),
                    Actions.color(base, 0.1f)));
            }
        });
    }

    @Override public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); bgImage.setSize(w, h); }
    @Override public void pause() {} @Override public void resume() {} @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }

    public TextField getUsernameField() { return usernameField; }
    public TextField getPasswordField() { return passwordField; }
    public TextButton getLoginButton()  { return loginButton; }
    public TextButton getBackButton()   { return backButton; }
    public TextButton getForgotButton() { return forgotButton; }

    public void showError(String msg) {
        messageLabel.setText(msg);
        messageLabel.setColor(Color.RED);
        messageLabel.setVisible(true);
    }

    public void showSuccess(String msg) {
        messageLabel.setText(msg);
        messageLabel.setColor(Color.GREEN);
        messageLabel.setVisible(true);
    }

    public void promptSecurityQuestion() {
        if (dialogOpen) return;
        dialogOpen = true;

        String username = usernameField.getText().trim();
        if (!SavaToJson.userExists(username)) {
            showError(LanguageManager.t("error.userNotFound"));
            dialogOpen = false;
            return;
        }

        User user = SavaToJson.getUser(username);

        final TextField answerField = new TextField("", skin);
        answerField.setMessageText(LanguageManager.t("label.securityQuestion"));

        final TextField newPassword = new TextField("", skin);
        newPassword.setMessageText(LanguageManager.t("placeholder.newPassword"));
        newPassword.setPasswordMode(true);
        newPassword.setPasswordCharacter('*');

        Dialog dialog = new Dialog(LanguageManager.t("dialog.resetPassword.title"), skin) {
            @Override protected void result(Object object) {
                boolean confirm = Boolean.TRUE.equals(object);
                if (confirm) {
                    String userAnswer = answerField.getText().trim();
                    if (!user.securityAnswer.equalsIgnoreCase(userAnswer)) {
                        showError(LanguageManager.t("error.incorrectAnswer"));
                        return;
                    }
                    String newPass = newPassword.getText().trim();
                    if (!isStrongPassword(newPass)) {
                        showError(LanguageManager.t("error.weakPassword"));
                        return;
                    }
                    user.password = newPass;
                    SavaToJson.save();
                    showSuccess(LanguageManager.t("success.passwordReset"));
                }
                dialogOpen = false;
                this.hide();
            }
        };

        dialog.text(LanguageManager.t("label.securityQuestion") + ":\n" + user.getSecurityQuestion());
        dialog.getContentTable().row().padTop(10);
        dialog.getContentTable().add(answerField).width(280).row();
        dialog.getContentTable().add(newPassword).width(280).padTop(10).row();
        dialog.button(LanguageManager.t("button.submit"), true);
        dialog.button(LanguageManager.t("button.cancel"), false);
        dialog.key(com.badlogic.gdx.Input.Keys.ESCAPE, false);
        dialog.show(stage);
    }

    public void promptSecurityQuestion(String question, Consumer<String> onAnswer) {
        final TextField answerField = new TextField("", skin);
        Dialog d = new Dialog(LanguageManager.t("dialog.resetPassword.title"), skin) {
            @Override protected void result(Object obj) {
                if (Boolean.TRUE.equals(obj)) {
                    onAnswer.accept(answerField.getText().trim());
                }
            }
        };
        d.text(LanguageManager.t("label.securityQuestion") + ":\n" + question);
        d.getContentTable().row().padTop(10);
        d.getContentTable().add(answerField).width(280).row();
        d.button(LanguageManager.t("button.submit"), true);
        d.button(LanguageManager.t("button.cancel"), false);
        d.key(com.badlogic.gdx.Input.Keys.ESCAPE, false);
        d.show(stage);
    }

    public void promptNewPassword(Consumer<String> onNewPass) {
        final TextField newPassword = new TextField("", skin);
        newPassword.setPasswordMode(true);
        newPassword.setPasswordCharacter('*');

        Dialog d = new Dialog(LanguageManager.t("dialog.resetPassword.title"), skin) {
            @Override protected void result(Object obj) {
                if (Boolean.TRUE.equals(obj)) {
                    onNewPass.accept(newPassword.getText().trim());
                }
            }
        };
        d.text(LanguageManager.t("placeholder.newPassword"));
        d.getContentTable().row().padTop(10);
        d.getContentTable().add(newPassword).width(280).row();
        d.button(LanguageManager.t("button.submit"), true);
        d.button(LanguageManager.t("button.cancel"), false);
        d.key(com.badlogic.gdx.Input.Keys.ESCAPE, false);
        d.show(stage);
    }

    private boolean isStrongPassword(String password) {
        return password.length() >= 8 &&
            password.matches(".*[A-Z].*") &&
            password.matches(".*\\d.*") &&
            password.matches(".*[@#$%^&+=!()_.*].*");
    }
}
