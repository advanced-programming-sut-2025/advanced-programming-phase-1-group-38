// ============================================================================
// RegisterView.java — Enhanced registration form UI with glass card design,
// animated title, parallax background, and hover-friendly buttons.
// Harmonized with AuthView, LoginMenu, and ProfileMenu visuals.
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
import io.github.StardewValley.controllers.RegisterController;
import io.github.StardewValley.models.LanguageManager;

public class RegisterView implements Screen {

    private Stage stage;
    private Image bgImage;
    private float bgOffsetX = 0f;

    private final Skin skin;
    private Table table;
    private final RegisterController controller;

    private final TextField usernameField, nicknameField, emailField;
    private final TextField passwordField, confirmPasswordField;
    private final TextButton generatePasswordButton, registerButton, backButton;
    private final Label errorLabel;
    private final SelectBox<String> genderSelect;

    private final SelectBox<String> securityQuestionSelect;
    private final TextField answerField, confirmAnswerField;

    public RegisterView(RegisterController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;
        this.table = new Table();

        this.usernameField = new TextField("", skin);
        this.nicknameField = new TextField("", skin);
        this.emailField = new TextField("", skin);
        this.passwordField = new TextField("", skin);
        this.confirmPasswordField = new TextField("", skin);
        this.generatePasswordButton = new TextButton(LanguageManager.t("button.generatePassword"), skin);
        this.registerButton = new TextButton(LanguageManager.t("button.register"), skin);
        this.backButton = new TextButton(LanguageManager.t("button.back"), skin);

        this.errorLabel = new Label("", skin);
        this.errorLabel.setColor(Color.RED);
        this.errorLabel.setVisible(false);

        this.genderSelect = new SelectBox<>(skin);
        genderSelect.setItems(
            LanguageManager.t("gender.male"),
            LanguageManager.t("gender.female"),
            LanguageManager.t("gender.other")
        );

        this.securityQuestionSelect = new SelectBox<>(skin);
        this.securityQuestionSelect.setItems(
            LanguageManager.t("security.pet"),
            LanguageManager.t("security.movie"),
            LanguageManager.t("security.maiden")
        );
        this.answerField = new TextField("", skin);
        this.confirmAnswerField = new TextField("", skin);

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


        table.setFillParent(false);
        table.setSize(700, 680);
        table.setPosition(
            (Gdx.graphics.getWidth()  - table.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - table.getHeight()) / 2f);

        table.setColor(1f, 1f, 1f, 0.7f);
        table.defaults().pad(6);

        Label title = new Label(LanguageManager.t("title.register"), skin);
        title.addAction(Actions.forever(Actions.sequence(
            Actions.color(Color.SCARLET, 1f),
            Actions.color(Color.SALMON, 1f))));

        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        confirmPasswordField.setPasswordMode(true);
        confirmPasswordField.setPasswordCharacter('*');

        usernameField.setMessageText(LanguageManager.t("placeholder.username"));
        nicknameField.setMessageText(LanguageManager.t("placeholder.nickname"));
        emailField.setMessageText(LanguageManager.t("placeholder.email"));
        passwordField.setMessageText(LanguageManager.t("placeholder.password"));
        confirmPasswordField.setMessageText(LanguageManager.t("placeholder.confirm_password"));
        answerField.setMessageText(LanguageManager.t("placeholder.answer"));
        confirmAnswerField.setMessageText(LanguageManager.t("placeholder.confirm_answer"));

        table.add(title).padBottom(15).row();
        table.add(usernameField).width(300).row();
        table.add(nicknameField).width(300).row();
        table.add(emailField).width(300).row();
        table.add(passwordField).width(300).row();
        table.add(confirmPasswordField).width(300).row();
        table.add(genderSelect).width(300).row();
        table.add(generatePasswordButton).width(260).height(70).padTop(4).row();
        table.add(registerButton).width(260).height(70).padTop(15).row();
        table.add(backButton).width(260).height(70).padTop(8).row();
        table.add(errorLabel).padTop(10);

        stage.addActor(table);

        table.getColor().a = 0f;
        table.addAction(Actions.fadeIn(0.6f));

        addHover(registerButton);
        addHover(backButton);
        addHover(generatePasswordButton);
        generatePasswordButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String randomPassword = generateStrongPassword();
                passwordField.setText(randomPassword);
                confirmPasswordField.setText(randomPassword);

                Dialog dialog = new Dialog("Generated Password", skin);

                Table content = dialog.getContentTable();
                content.add(new Label("Your random password is:", skin)).padBottom(8).row();
                content.add(new Label(randomPassword, skin)).padBottom(12).row();

                TextButton copyButton = new TextButton("📋 Copy", skin);
                copyButton.addListener(new ClickListener() {
                    @Override public void clicked(InputEvent ev, float x, float y) {
                        Gdx.app.getClipboard().setContents(randomPassword);
                        copyButton.setText("✅ Copied");
                    }
                });

                dialog.button(copyButton);
                dialog.button("OK");
                dialog.show(stage);
            }
        });

        registerButton.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                controller.handleRegister();   // now runs exactly once on click
            }
        });
        backButton.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                controller.handleBack();
            }
        });

    }

    private void addHover(final TextButton btn) {
        btn.setTransform(true);
        final Color base = new Color(btn.getColor());
        btn.addListener(new InputListener() {
            @Override public void enter(InputEvent ev, float x, float y, int p, Actor from) {
                btn.addAction(Actions.parallel(
                    Actions.scaleTo(1.05f, 1.05f, 0.1f),
                    Actions.color(base.cpy().lerp(Color.WHITE, 0.3f), 0.1f)));
            }
            @Override public void exit(InputEvent ev, float x, float y, int p, Actor to) {
                btn.addAction(Actions.parallel(
                    Actions.scaleTo(1f, 1f, 0.1f),
                    Actions.color(base, 0.1f)));
            }
        });
    }



    public void showSecurityDialog(Runnable onComplete) {
        Dialog dialog = new Dialog("Security Question", skin) {
            @Override protected void result(Object obj) {
                if ((Boolean) obj) {
                    if (!answerField.getText().equals(confirmAnswerField.getText())) {
                        showError("Answers do not match.");
                        return;
                    }
                    this.hide();
                    onComplete.run();
                }
            }
        };

        securityQuestionSelect.setSelectedIndex(0);
        answerField.setText("");
        confirmAnswerField.setText("");

        Table content = dialog.getContentTable();
        content.add(new Label("Choose a security question:", skin)).padBottom(6).row();
        content.add(securityQuestionSelect).width(280).padBottom(6).row();
        content.add(answerField).width(280).padBottom(6).row();
        content.add(confirmAnswerField).width(280).padBottom(6).row();

        dialog.button("Confirm", true);
        dialog.button("Cancel", false);
        dialog.key(com.badlogic.gdx.Input.Keys.ESCAPE, false);
        dialog.show(stage);
    }

    private String generateStrongPassword() {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String symbols = "@#$%^&+=!()_*";
        String all = upper + lower + digits + symbols;

        StringBuilder sb = new StringBuilder();
        sb.append(upper.charAt((int) (Math.random() * upper.length())));
        sb.append(lower.charAt((int) (Math.random() * lower.length())));
        sb.append(digits.charAt((int) (Math.random() * digits.length())));
        sb.append(symbols.charAt((int) (Math.random() * symbols.length())));

        for (int i = 4; i < 12; i++) {
            sb.append(all.charAt((int) (Math.random() * all.length())));
        }

        return sb.toString();
    }

    @Override public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        bgImage.setSize(width, height);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }

    public TextField getUsernameField() { return usernameField; }
    public TextField getNicknameField() { return nicknameField; }
    public TextField getEmailField()    { return emailField; }
    public TextField getPasswordField() { return passwordField; }
    public TextField getConfirmPasswordField() { return confirmPasswordField; }
    public TextButton getGeneratePasswordButton() { return generatePasswordButton; }
    public TextButton getRegisterButton() { return registerButton; }
    public TextButton getBackButton()     { return backButton; }
    public String getSelectedGender()     { return genderSelect.getSelected(); }
    public String getSelectedQuestion()   { return securityQuestionSelect.getSelected(); }
    public TextField getAnswerField()     { return answerField; }
    public TextField getConfirmAnswerField() { return confirmAnswerField; }

    public void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setColor(Color.RED);
        errorLabel.setVisible(true);
    }

    public void showSuccess(String message) {
        errorLabel.setText(message);
        errorLabel.setColor(Color.GREEN);
        errorLabel.setVisible(true);

        usernameField.setText("");
        nicknameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }
}
