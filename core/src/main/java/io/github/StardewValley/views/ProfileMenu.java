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
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.StardewValley.controllers.ProfileMenuController;
import io.github.StardewValley.models.LanguageManager;
import io.github.StardewValley.models.User;

public class ProfileMenu implements Screen {

    private final ProfileMenuController controller;
    private final Skin skin;
    private Stage stage;
    private Image bgImage;
    private float bgOffsetX = 0f;

    private final TextButton changeUsernameBtn;
    private final TextButton changePasswordBtn;
    private final TextButton changeNicknameBtn;
    private final TextButton changeEmailBtn;
    private final TextButton deleteAccountBtn;
    private final TextButton backButton;

    private final Label genderLabel;
    private final Label bestMoneyLabel;
    private final Label gamesPlayedLabel;
    private final Label msg;
    private final Label usernameLabel;
    private final Label nicknameLabel;
    private final Label emailLabel;

    public ProfileMenu(ProfileMenuController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;

        changeUsernameBtn = new TextButton("", skin);
        changeNicknameBtn = new TextButton("", skin);
        changeEmailBtn    = new TextButton("", skin);
        changePasswordBtn = new TextButton("", skin);
        deleteAccountBtn  = new TextButton("", skin);
        backButton        = new TextButton("", skin);

        this.usernameLabel = new Label("", skin);
        this.nicknameLabel = new Label("", skin);
        this.emailLabel    = new Label("", skin);
        genderLabel       = new Label("", skin);
        bestMoneyLabel    = new Label("", skin);
        gamesPlayedLabel  = new Label("", skin);
        msg               = new Label("", skin);
        msg.setVisible(false);

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

        Table card = new Table();
        Drawable cardBg = skin.has("card-transparent", Drawable.class)
            ? skin.getDrawable("card-transparent")
            : new TextureRegionDrawable(new TextureRegion(bgTex));
        card.setBackground(cardBg);
        card.setColor(1f, 1f, 1f, 0.7f);
        card.defaults().pad(8);

        card.add(changeUsernameBtn).width(260).height(80).row();
        card.add(changeNicknameBtn).width(260).height(80).row();
        card.add(changeEmailBtn).width(260).height(80).row();
        card.add(changePasswordBtn).width(260).height(80).row();
        card.add(deleteAccountBtn).width(260).height(80).row();
        card.add(backButton).width(260).height(80).row();

        card.add(usernameLabel).row();
        card.add(nicknameLabel).row();
        card.add(emailLabel).row();

        card.add(genderLabel).padTop(10).row();
        card.add(bestMoneyLabel).row();
        card.add(gamesPlayedLabel).row();
        card.add(msg).padTop(10);

        Container<Table> container = new Container<>(card);
        container.setFillParent(true);
        container.center();
        stage.addActor(container);

        card.getColor().a = 0f;
        card.addAction(Actions.fadeIn(0.6f));

        refreshTexts();

        addHover(changeUsernameBtn);
        addHover(changeNicknameBtn);
        addHover(changeEmailBtn);
        addHover(changePasswordBtn);
        addHover(deleteAccountBtn);
        addHover(backButton);
    }

    private Texture loadBackgroundTexture() {
        if (Gdx.files.internal("mainmenu/background.png").exists())
            return new Texture(Gdx.files.internal("mainmenu/background.png"));
        Pixmap pm = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pm.setColor(0.08f, 0.08f, 0.08f, 1f);
        pm.fill();
        Texture tex = new Texture(pm);
        pm.dispose();
        return tex;
    }

    private void refreshTexts() {
        changeUsernameBtn.setText(LanguageManager.t("button.changeUsername"));
        changeNicknameBtn.setText(LanguageManager.t("button.changeNickname"));
        changeEmailBtn   .setText(LanguageManager.t("button.changeEmail"));
        changePasswordBtn.setText(LanguageManager.t("button.changePassword"));
        deleteAccountBtn .setText(LanguageManager.t("button.deleteAccount"));
        backButton       .setText(LanguageManager.t("button.back"));
        updateInfoLabels();
    }

    public void updateInfoLabels() {
        User u = User.getCurrentUser();
        if (u == null) return;
        genderLabel.setText(LanguageManager.t("label.gender") + ": " + u.getGender());
        bestMoneyLabel.setText(LanguageManager.t("label.bestMoney") + ": " + u.getHighestGold());
        gamesPlayedLabel.setText(LanguageManager.t("label.gamesPlayed") + ": " + u.getTotalGamesPlayed());
        usernameLabel.setText(LanguageManager.t("label.username") + ": " + u.getUsername());
        nicknameLabel.setText(LanguageManager.t("label.nickname") + ": " + u.getNickname());
        emailLabel.setText(LanguageManager.t("label.email") + ": " + u.getEmail());
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
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }

    public Skin getSkin()               { return skin; }
    public Stage getStage()            { return stage; }
    public TextButton getChangeUsernameBtn() { return changeUsernameBtn; }
    public TextButton getChangeNicknameBtn() { return changeNicknameBtn; }
    public TextButton getChangeEmailBtn()    { return changeEmailBtn; }
    public TextButton getChangePasswordBtn() { return changePasswordBtn; }
    public TextButton getDeleteAccountBtn()  { return deleteAccountBtn; }
    public TextButton getBackButton()        { return backButton; }
    public Label getGenderLabel()      { return genderLabel; }
    public Label getBestMoneyLabel()   { return bestMoneyLabel; }
    public Label getGamesPlayedLabel() { return gamesPlayedLabel; }
    public ProfileMenuController getController() { return controller; }
    public void showError(String s)   { msg.setColor(Color.RED); msg.setText(s); msg.setVisible(true); }
    public void showSuccess(String s) { msg.setColor(Color.GREEN); msg.setText(s); msg.setVisible(true); }
}
