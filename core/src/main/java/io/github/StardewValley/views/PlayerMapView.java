package io.github.StardewValley.views;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.StardewValley.controllers.GameController;
import io.github.StardewValley.models.*;

import static io.github.StardewValley.controllers.GameController.TILE_SIZE;


public class PlayerMapView implements Screen {
    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;
    private Viewport viewport;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private GameController controller;
    private SpriteBatch batch;

    private int mapWidth;
    private int mapHeight;

    private Sprite clockPointer;
    private Texture pointerTexture;

    private Texture clockBgTexture;
    private Sprite clockBgSprite;

    private Texture dayNightClockTexture;
    private Sprite dayNightClockSprite;

    private Texture weatherTexture;
    private Sprite weatherSprite;

    private BitmapFont font;

    private Tile[][] tiles;

    private InventoryRenderer inventoryRenderer;
    private InventoryMenuView inventoryMenuView;
    private boolean menuWasVisible = false;

    private CookingMenuView cookingMenuView;

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private boolean debugDraw = true;          // press F3 to toggle



    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.zoom = 0.3f;
        viewport = new FitViewport(1280, 720, camera);
        viewport.apply();

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, 800, 550);

        String mapPath = "maps/OutdoorMap1.tmx";
        GameAssetManager.getGameAssetManager().loadMap(mapPath);
        map = GameAssetManager.getGameAssetManager().getMap(mapPath);
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        int tileSize = 16;
        mapWidth = map.getProperties().get("width", Integer.class) * tileSize;
        mapHeight = map.getProperties().get("height", Integer.class) * tileSize;

        batch = new SpriteBatch();
        controller = new GameController(mapPath);
        controller.setCamera(camera);
        camera.update();

        inventoryRenderer = controller.getPlayer().getInventoryRenderer();
        inventoryMenuView = new InventoryMenuView(
            controller.getPlayer().getInventory(),
            controller.getPlayer().getFridgeInventory(),
            controller
        );

        cookingMenuView = new CookingMenuView(
            controller.getPlayer().getInventory(),
            controller.getPlayer().getFridgeInventory(),
            controller.getPlayer().getAllCookingRecipes()
        );

        Gdx.input.setInputProcessor(new InventoryScrollHandler(inventoryRenderer, inventoryMenuView, cookingMenuView));


        pointerTexture = new Texture(Gdx.files.internal("pointer.png"));
        clockPointer = new Sprite(pointerTexture);
        clockPointer.setOrigin(0, clockPointer.getHeight() / 2f);
        clockPointer.setScale(1.25f);

        clockBgTexture = new Texture(Gdx.files.internal("clock.png"));
        clockBgSprite = new Sprite(clockBgTexture);
        clockBgSprite.setPosition(20, 625);
        clockBgSprite.setScale(1.25f);

        dayNightClockTexture = new Texture(Gdx.files.internal("dayNight.png"));
        dayNightClockSprite = new Sprite(dayNightClockTexture);
        dayNightClockSprite.setScale(1.25f);


        WeatherType weatherType = controller.getWeather().getWeatherType();
        weatherTexture = new Texture(Gdx.files.internal(weatherType.getIconPath()));
        weatherSprite = new Sprite(weatherTexture);
        weatherSprite.setScale(0.03f);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("pixelFont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 11;
        font = generator.generateFont(parameter);
        generator.dispose();

        tiles = controller.getTiles();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) debugDraw = !debugDraw;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            boolean willOpen = !inventoryMenuView.isVisible();
            inventoryMenuView.toggle();

            if (willOpen) {
                // 🔁 Menu is opening → copy hotbar state to menu
                int selected = inventoryRenderer.getSelectedIndex();
                inventoryMenuView.setSelectedIndex(selected);
            } else {
                // 🔁 Menu is closing → copy menu state to hotbar
                int selected = inventoryMenuView.getSelectedIndex();
                inventoryRenderer.setSelectedIndex(selected);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.C) && controller.isPlayerInHouse()) {
            cookingMenuView.toggle();
        }

        boolean menuVisible = inventoryMenuView.isVisible();

        if (menuWasVisible && !menuVisible) {
            int selected = inventoryMenuView.getSelectedIndex();
            inventoryRenderer.setSelectedIndex(selected);
        }

        menuWasVisible = menuVisible;

        if (!isMenuOpen()) {
            controller.update(delta);
        }


        TiledMap newMap = controller.getMap();
        if (newMap != map) {
            this.map = newMap;
            this.mapRenderer.setMap(newMap);

            int tileSize = 16;
            mapWidth = map.getProperties().get("width", Integer.class) * tileSize;
            mapHeight = map.getProperties().get("height", Integer.class) * tileSize;
        }

        updateCamera();

        Gdx.gl.glClearColor(0.74f, 0.91f, 0.95f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                Tile tile = tiles[x][y];
                if (tile.hasCrop()) {
                    String spritePath = tile.getCrop().getCurrentSpritePath();
                    Texture texture = GameAssetManager.getGameAssetManager().getTexture(spritePath);
                    float drawX = tile.getWorldX();
                    float drawY = tile.getWorldY();
                    batch.draw(texture, drawX + TILE_SIZE / 2f - texture.getWidth() / 2f, drawY + 3);
                }
            }
        }

        // ---------- DEBUG OVERLAYS (world space) ----------
        if (debugDraw) {
            Vector3 w = camera.unproject(
                new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            int tx = (int)(w.x / TILE_SIZE);
            int ty = (int)(w.y / TILE_SIZE);

            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

            // 1. yellow tile under mouse
            shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.rect(tx * TILE_SIZE,
                ty * TILE_SIZE,
                TILE_SIZE,
                TILE_SIZE);

            // 2. red harvest radius
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(w.x, w.y, GameController.HARVEST_RANGE);

            // 3. green boxes around every grown crop
            shapeRenderer.setColor(Color.GREEN);
            for (int x = 0; x < tiles.length; x++) {
                for (int y = 0; y < tiles[0].length; y++) {
                    Tile t = tiles[x][y];
                    if (t.hasCrop()
                        && t.getCrop().isFullyGrown()
                        && !t.getCrop().isHarvested()) {

                        shapeRenderer.rect(t.getWorldX(),
                            t.getWorldY(),
                            TILE_SIZE,
                            TILE_SIZE);
                    }
                }
            }
            shapeRenderer.end();
        }


        controller.render(batch);
        batch.end();

        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();

        int hour = controller.getGameTime().getHour();
        float angle = 90 - ((hour - 9) * (180f / 13f));// 9am–10pm mapped to 180°

        clockPointer.setRotation(angle);
        float offsetX = 11f;  // move right
        float offsetY = 13f; // move slightly down
        clockPointer.setPosition(
            clockBgSprite.getX() + clockBgSprite.getWidth() / 2f - clockPointer.getWidth() / 2f + offsetX,
            clockBgSprite.getY() + clockBgSprite.getHeight() / 2f - clockPointer.getHeight() / 2f + offsetY
        );

        float iconX = -18.75f;  // move right
        float iconY = 13f; // move slightly down
        weatherSprite.setPosition(
            clockBgSprite.getX() + clockBgSprite.getWidth() / 2f - weatherSprite.getWidth() / 2f + iconX,
            clockBgSprite.getY() + clockBgSprite.getHeight() / 2f - weatherSprite.getHeight() / 2f + iconY
        );

        float dayNightIconX = 12f;  // move right
        float dayNightIconY = 13f; // move slightly down
        dayNightClockSprite.setPosition(
            clockBgSprite.getX() + clockBgSprite.getWidth() / 2f - dayNightClockSprite.getWidth() / 2f + dayNightIconX,
            clockBgSprite.getY() + clockBgSprite.getHeight() / 2f - dayNightClockSprite.getHeight() / 2f + dayNightIconY
        );

        clockBgSprite.draw(batch);
        weatherSprite.draw(batch);
        dayNightClockSprite.draw(batch);
        clockPointer.draw(batch);
        font.draw(batch, controller.getGameTime().getFormattedDayAndHour(), clockBgSprite.getX() + clockBgSprite.getWidth() / 2f - 35f, clockBgSprite.getY() + clockBgSprite.getHeight() / 2f - 16.5f); // Adjust position if needed

        inventoryRenderer.setInputEnabled(!isMenuOpen());
        inventoryRenderer.render(batch);

        if (inventoryMenuView.isVisible()) {
            inventoryMenuView.render(batch);
        }

        if (cookingMenuView.isVisible()) {
            cookingMenuView.render(batch);
        }

        batch.end();
    }

    private void updateCamera() {
        float viewWidth = camera.viewportWidth * camera.zoom;
        float viewHeight = camera.viewportHeight * camera.zoom;

        float camX = controller.getPlayerX();
        float camY = controller.getPlayerY();

        float halfViewW = viewWidth / 2f;
        float halfViewH = viewHeight / 2f;

        if (mapWidth > viewWidth) {
            camX = MathUtils.clamp(camX, halfViewW, mapWidth - halfViewW);
        } else {
            camX = mapWidth / 2f;
        }

        if (mapHeight > viewHeight) {
            camY = MathUtils.clamp(camY, halfViewH, mapHeight - halfViewH);
        } else {
            camY = mapHeight / 2f;
        }

        camera.position.set(camX, camY, 0);
        camera.update();
    }

    private boolean isMenuOpen() {
        return (inventoryMenuView != null && inventoryMenuView.isVisible()) ||
            (cookingMenuView != null && cookingMenuView.isVisible());
    }


    @Override public void resize(int width, int height) {
        viewport.update(width, height);
        uiCamera.setToOrtho(false, width, height);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        controller.getPlayer().dispose();
        controller.dispose();
        batch.dispose();
        mapRenderer.dispose();
        map.dispose();
        pointerTexture.dispose();
        clockBgTexture.dispose();
        weatherTexture.dispose();
        shapeRenderer.dispose();
    }
}
