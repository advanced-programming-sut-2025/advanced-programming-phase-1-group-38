package io.github.StardewValley.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.StardewValley.controllers.AuthController;

/**
 * AuthView — Start menu with
 *   1) full-screen scenic background (menuuu.png)
 *   2) wooden frame card holding the buttons.
 *
 * Wooden frame is loaded from the skin if a drawable named "board" exists; otherwise we
 * create a NinePatchDrawable from `ui/wood-frame.png` (a 9-patch png in assets/ui).
 */
public class AuthView implements Screen {
    private Stage stage;
    private Image bgImage;

    private final Skin skin;
    private final Table table;
    private final AuthController controller;

    private final TextButton loginButton;
    private final TextButton signupButton;
    private final TextButton guestButton;

    private static final Texture BG_SCENE = new Texture(Gdx.files.internal("menu3.png"));

    public AuthView(AuthController controller, Skin skin) {
        this.controller = controller;
        this.skin       = skin;
        this.table      = new Table();

        this.loginButton  = new TextButton("Log In",        skin);
        this.signupButton = new TextButton("Sign Up",       skin);
        this.guestButton  = new TextButton("Play as Guest", skin);

        controller.setView(this);
    }

    // ---------------------------------------------------------------------
    @Override public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        //---------------- background image ----------------
        bgImage = new Image(BG_SCENE);
        bgImage.setFillParent(true);
        bgImage.setScaling(Scaling.fill);
        stage.addActor(bgImage);

        //---------------- wooden frame card ----------------
        // ---------------- wooden frame card ----------------
        table.setSize(700, 700);
        table.setPosition((Gdx.graphics.getWidth()  - table.getWidth())  / 2f,
            (Gdx.graphics.getHeight() - table.getHeight()) / 2f );

        Drawable frame;
        if (skin.has("board", Drawable.class)) {
            // اگر در اسکین drawable‌ به نام "board" دارید همان را استفاده کنید
            frame = skin.getDrawable("board");
        } else {
            // fallback بدون 9-patch
            Texture frameTex = new Texture(Gdx.files.internal("mainmenu-background.png"));
            frameTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            frame = new TextureRegionDrawable(new TextureRegion(frameTex));
            // اگر می‌خواهید تصویر تکرار شود (tile)، می‌توانید از TiledDrawable به‌جای TextureRegionDrawable استفاده کنید:
            // frame = new TiledDrawable(new TextureRegion(frameTex));
        }

        table.setBackground(frame);
        table.pad(32);

        Label subtitle = new Label("START MENU", skin, "title");
        subtitle.setAlignment(Align.center);
        subtitle.setColor(Color.WHITE);
        subtitle.setFontScale(1.1f);

        table.add(subtitle).padTop(0).padBottom(30).expandX().center().row();
        addButtonRow(loginButton);
        addButtonRow(signupButton);
        addButtonRow(guestButton);

        stage.addActor(table);

        addHover(loginButton);
        addHover(signupButton);
        addHover(guestButton);
    }

    private void addButtonRow(TextButton btn) {
        float pad = btn == guestButton ? 50f : 12f; // آخرین دکمه رو فاصله بده
        table.add(btn).width(350).height(75).padBottom(pad).row();
    }

    private void addHover(final TextButton btn) {
        btn.setTransform(true);
        final Color base = new Color(btn.getColor());
        btn.addListener(new InputListener() {
            @Override public void enter(InputEvent ev, float x, float y, int p, Actor from) {
                btn.addAction(Actions.parallel(
                    Actions.scaleTo(1.05f, 1.05f, 0.1f),
                    Actions.color(base.cpy().lerp(Color.WHITE, 0.25f), 0.1f)));
            }
            @Override public void exit(InputEvent ev, float x, float y, int p, Actor to) {
                btn.addAction(Actions.parallel(
                    Actions.scaleTo(1f, 1f, 0.1f),
                    Actions.color(base, 0.1f)));
            }
        });
    }

    // ---------------------------------------------------------------------
    @Override public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
        controller.initListeners();
    }

    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void pause()   {}
    @Override public void resume()  {}
    @Override public void hide()    {}
    @Override public void dispose() { stage.dispose(); }

    // ---------------------------------------------------------------------
    public TextButton getLoginButton()  { return loginButton; }
    public TextButton getSignupButton() { return signupButton; }
    public TextButton getGuestButton()  { return guestButton; }
}
