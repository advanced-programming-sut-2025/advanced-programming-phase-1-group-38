package io.github.StardewValley.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.HashMap;
import java.util.Map;

public class GameAssetManager {
    private static GameAssetManager instance;
    private final Skin defaultSkin;
    private final Skin authSkin;
    private final Map<String, Animation<TextureRegion>> animationCache = new HashMap<>();
    private final Map<String, Texture> textureCache = new HashMap<>();

    private final AssetManager assetManager;
    private final Map<Integer, BitmapFont> fonts = new HashMap<>();


    private GameAssetManager() {
        // Load ready skin (for rest of menus)
        defaultSkin = new Skin(Gdx.files.internal("LibGdx-Skin-main/NzSkin.json"));
        Texture upTexture = new Texture(Gdx.files.internal("button-up.png"));
        Texture downTexture = new Texture(Gdx.files.internal("button-down.png"));

        TextureRegionDrawable upDrawable = new TextureRegionDrawable(new TextureRegion(upTexture));
        TextureRegionDrawable downDrawable = new TextureRegionDrawable(new TextureRegion(downTexture));
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = upDrawable;
        buttonStyle.down = downDrawable;
        // Custom skin for AuthView
        authSkin = new Skin();

        // Generate bitmap font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("pixelFont-7-8x14-sproutLands.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = 22;
        param.color = Color.WHITE;
        BitmapFont font = generator.generateFont(param);
        generator.dispose();

        // Required for Label
        authSkin.add("default-font", font);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        authSkin.add("default", labelStyle);

        Label.LabelStyle titleLabelStyle = new Label.LabelStyle();
        titleLabelStyle.font = font;
        titleLabelStyle.fontColor = Color.GOLD;
        authSkin.add("title", titleLabelStyle);

        // Optional: If using buttons with default style
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        authSkin.add("default", buttonStyle); // for buttons too

        assetManager = new AssetManager();
        assetManager.setLoader(TiledMap.class,
            new TmxMapLoader(new InternalFileHandleResolver()));

        createFont(11);
        createFont(16);
        createFont(14);
        assetManager.load("black.png", Texture.class);
        assetManager.finishLoadingAsset("black.png");

    }

    private void createFont(int size) {
        FreeTypeFontGenerator gen =
            new FreeTypeFontGenerator(Gdx.files.internal("pixelFont.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter p =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.size = size;
        p.borderWidth = 1;
        p.borderColor = Color.BLACK;

        BitmapFont font = gen.generateFont(p);
        fonts.put(size, font);          // ← store by size key
        gen.dispose();
    }

    public BitmapFont getFont(int size) {
        return fonts.get(size);
    }

    public BitmapFont getSmallFont() {
        return fonts.get(11);
    }

    public BitmapFont getBigFont() {
        return fonts.get(16);
    }

    public BitmapFont getErrorFont() {
        return fonts.get(14);
    }


    public static GameAssetManager getGameAssetManager() {
        if (instance == null)
            instance = new GameAssetManager();
        return instance;
    }

    public void loadMap(String mapPath) {
        if (!assetManager.isLoaded(mapPath, TiledMap.class)) {
            assetManager.load(mapPath, TiledMap.class);
            assetManager.finishLoadingAsset(mapPath);
        }
    }

    public TiledMap getMap(String mapPath) {
        return assetManager.isLoaded(mapPath, TiledMap.class)
            ? assetManager.get(mapPath, TiledMap.class)
            : null;
    }

    public TiledMap loadFreshMap(String mapPath) {
        TmxMapLoader loader = new TmxMapLoader(new InternalFileHandleResolver());
        return loader.load(mapPath); // never null if the file exists and dependencies resolve
    }

    public Texture getTexture(String path) {
        return textureCache.computeIfAbsent(path,
            p -> new Texture(Gdx.files.internal(p)));
    }

    public Texture getBlackPixel() {
        return assetManager.get("black.png", Texture.class);
    }


    public Animation<TextureRegion> getFridgeOpenAnimation() {
        return getFrameAnimation("fridge_open", "fridge/fridge", 5, 0.1f, false);
    }

    public Animation<TextureRegion> getFrameAnimation(
        String key,
        String basePath,
        int frameCount,
        float frameDuration,
        boolean looping
    ) {
        if (animationCache.containsKey(key)) return animationCache.get(key);

        TextureRegion[] frames = new TextureRegion[frameCount];

        for (int i = 0; i < frameCount; i++) {
            String path = basePath + (i + 1) + ".png";
            Texture tex = getTexture(path);
            frames[i] = new TextureRegion(tex);
        }

        Animation<TextureRegion> anim = new Animation<>(frameDuration, frames);
        anim.setPlayMode(looping ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL);
        animationCache.put(key, anim);
        return anim;
    }


    public TextureRegion getFridgeFrame(int index) {
        if (index < 1 || index > 5) throw new IllegalArgumentException("Fridge frame must be 1–5");
        Texture tex = getTexture("fridge/fridge" + index + ".png");
        return new TextureRegion(tex);
    }

    public Skin getDefaultSkin() {
        return defaultSkin;
    }

    public Skin getAuthSkin() {
        return authSkin;
    }
}
