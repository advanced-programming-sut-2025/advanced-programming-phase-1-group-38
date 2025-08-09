// ============================================================================
//  MainMenu.java  —  Stylish main menu with graceful fallback if bg image
//  is missing. (Update fixes File-not-found crash by checking existence.)
// ============================================================================
package io.github.StardewValley.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.StardewValley.controllers.MainMenuController;

public class MainMenu implements Screen {

    // ------------------------------------------------------------------ //
    // Constants / Assets
    // ------------------------------------------------------------------ //
    private static final float BG_SCROLL_SPEED = 20f;          // px/sec
    private static final String BG_PATH = "mainmenu/background.png"; // put png here

    // ------------------------------------------------------------------ //
    // Fields
    // ------------------------------------------------------------------ //
    private final MainMenuController controller;
    private final Skin skin;

    private Stage stage;
    private Image bgImage;
    private float bgOffsetX = 0f;

    private Table card; // glass card containing UI
    private final TextButton profileButton;
    private final TextButton playButton;
    private final TextButton logoutButton;

    public MainMenu(MainMenuController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;

        profileButton = new TextButton("Profile", getMenuStyle());
        playButton    = new TextButton("Play",    getMenuStyle());
        logoutButton  = new TextButton("Logout",  getMenuStyle());

        controller.setView(this);
    }

    /* If style "menu" is defined in skin use it, else default */
    private TextButton.TextButtonStyle getMenuStyle() {
        return skin.has("menu", TextButton.TextButtonStyle.class)
            ? skin.get("menu", TextButton.TextButtonStyle.class)
            : skin.get(TextButton.TextButtonStyle.class);
    }

    // ------------------------------------------------------------------ //
    // Screen Lifecycle
    // ------------------------------------------------------------------ //
    @Override public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        /* 1. Parallax background (with safe fallback) */
        Texture bgTex = new Texture(Gdx.files.internal("menu2.png"));
        bgTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        bgImage = new Image(bgTex);
        bgImage.setFillParent(true);               // کل صفحه را پر کند
        bgImage.setScaling(Scaling.fill);          // اگر می‌خواهید تناسب حفظ شود از Scaling.fit استفاده کنید
        stage.addActor(bgImage);

        /* 2. Glass-card container */
        card = new Table();
        Drawable cardBg = skin.has("card-transparent", Drawable.class)
            ? skin.getDrawable("card-transparent")
            : new TextureRegionDrawable(new TextureRegion(bgTex)); // fallback
        card.setBackground(cardBg);
        card.setColor(1f,1f,1f,0.7f);
        card.defaults().pad(10);
        card.center();

        // Title label
        Label title = new Label("Main Menu",
            skin.has("title", Label.LabelStyle.class)
                ? skin.get("title", Label.LabelStyle.class)
                : skin.get(Label.LabelStyle.class));
        title.setColor(Color.SKY);

        card.add(title).padBottom(18).row();
        card.add(profileButton).width(260).height(80).row();
        card.add(playButton)   .width(260).height(80).row();
        card.add(logoutButton) .width(260).height(80).row();

        Container<Table> centered = new Container<>(card);
        centered.setFillParent(true);
        centered.center();
        stage.addActor(centered);

        card.getColor().a = 0f;
        card.addAction(Actions.fadeIn(0.6f));

        addHover(profileButton);
        addHover(playButton);
        addHover(logoutButton);
    }

    /** Safely loads background texture, returns 1×1 dark placeholder if missing. */
    private Texture loadBackgroundTexture() {
        FileHandle fh = Gdx.files.internal(BG_PATH);
        if (fh.exists()) {
            return new Texture(fh);
        }
        // Placeholder: 1×1 pixel dark gray so game won’t crash
        Pixmap pm = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pm.setColor(0.07f,0.07f,0.07f,1f); pm.fill();
        Texture tex = new Texture(pm);
        pm.dispose();
        return tex;
    }

    /** Hover effect: 5% scale & brighten. */
    private void addHover(final TextButton btn) {
        btn.setTransform(true);
        final Color base = new Color(btn.getColor());
        btn.addListener(new InputListener() {
            @Override public void enter(InputEvent ev, float x, float y, int p, Actor from) {
                btn.addAction(Actions.parallel(
                    Actions.scaleTo(1.05f,1.05f,0.1f),
                    Actions.color(base.cpy().lerp(Color.WHITE,0.3f),0.1f)));
            }
            @Override public void exit(InputEvent ev, float x, float y, int p, Actor to) {
                btn.addAction(Actions.parallel(
                    Actions.scaleTo(1f,1f,0.1f),
                    Actions.color(base,0.1f)));
            }
        });
    }

    @Override public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        controller.handleButtons();
    }

    @Override public void resize(int w,int h){
        stage.getViewport().update(w,h,true);
        bgImage.setSize(w,h);
    }
    @Override public void pause(){} @Override public void resume(){}
    @Override public void hide(){} @Override public void dispose(){ stage.dispose(); }

    public TextButton getProfileButton(){return profileButton;}
    public TextButton getPlayButton(){return playButton;}
    public TextButton getLogoutButton(){return logoutButton;}
}
