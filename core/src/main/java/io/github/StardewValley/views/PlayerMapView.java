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
import io.github.StardewValley.Main;
import io.github.StardewValley.controllers.GameController;
import io.github.StardewValley.controllers.WorldController;
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
    private CraftingMenuView craftingMenuView;
    private JournalOverlay journalOverlay = new JournalOverlay();
    private ShopView shopView;
    private SellMenuView sellMenuView;
    private ControlsOverlay controlsOverlay = new ControlsOverlay();

    private WorldController worldController;
    private int playerIndex;

    private PlayerChatOverlay chatOverlay;

    private String activeSpeech = null;
    private float speechTimer = 0f;
    private NpcCharacter speakingNpc = null;

    private Texture px;  // load in show()
    private BitmapFont speechFont;

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

    private NpcQuestPopupView npcQuestView;
    private com.badlogic.gdx.graphics.g2d.Animation<com.badlogic.gdx.graphics.g2d.TextureRegion> talkReadyAnim;
    private float talkReadyTime = 0f;
    private Texture dialogueBoxTex;
    private final Vector3 tmpProject = new Vector3();

    private GiftPopupView giftPopupView;
    private Texture notifTex;
    private Sprite  notifSprite;

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private boolean debugDraw = false;          // press F3 to toggle

    private enum Panel { NONE, INVENTORY, CRAFTING, SHOP, SELL, COOKING, JOURNAL, CONTROLS, QUEST, GIFT, CHAT }
    private Panel activePanel = Panel.NONE;

    public PlayerMapView(WorldController worldController, int playerIndex) {
        this.worldController = worldController;
        this.playerIndex = playerIndex;
        this.controller = worldController.getAllControllers()[playerIndex];
    }


    @Override
    public void show() {

        camera = new OrthographicCamera();
        camera.zoom = 0.3f;
        viewport = new FitViewport(1280, 720, camera);
        viewport.apply();

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, 800, 550);

        map = controller.getMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        int tileSize = 16;
        mapWidth = map.getProperties().get("width", Integer.class) * tileSize;
        mapHeight = map.getProperties().get("height", Integer.class) * tileSize;

        batch = new SpriteBatch();
        controller.setCamera(camera);
        controller.setWorldController(worldController);   // ← add this
        camera.update();

        inventoryRenderer = controller.getPlayer().getInventoryRenderer();
        inventoryMenuView = new InventoryMenuView(
            controller.getPlayer().getInventory(),
            controller.getPlayer().getFridgeInventory(),
            controller
        );

        craftingMenuView = new CraftingMenuView(
                controller.getPlayer().getInventory(),
                SimpleRecipeBook.getBasicRecipes()

        );

        inventoryMenuView.setOnOpenSellMenu(() -> Gdx.app.postRunnable(() -> {
            if (activePanel == Panel.INVENTORY) {
                if (inventoryMenuView.isVisible()) inventoryMenuView.toggle(); // close current
                if (!sellMenuView.isVisible()) sellMenuView.toggle();          // open target
                activePanel = Panel.SELL;
            }
        }));

        cookingMenuView = new CookingMenuView(
            controller.getPlayer().getInventory(),
            controller.getPlayer().getFridgeInventory(),
            controller.getPlayer().getAllCookingRecipes()
        );

        Gdx.input.setInputProcessor(new InventoryScrollHandler(inventoryRenderer, inventoryMenuView, cookingMenuView));

        shopView = new ShopView(controller.getPlayer().getInventory(), ShopCatalog.basicGeneralStore(), controller.getPlayer().getGameEconomy());
        sellMenuView = new SellMenuView(controller.getPlayer().getInventory(), controller.getPlayer().getGameEconomy());

        npcQuestView = new NpcQuestPopupView(worldController, controller);

        chatOverlay = new PlayerChatOverlay(worldController, controller);
        giftPopupView = new GiftPopupView(controller, worldController,
            controller.getPlayer().getInventory());
        controller.setGiftPopup(giftPopupView);

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

        notifTex = new Texture(Gdx.files.internal("gift.png")); // pick any 32x32 bell icon
        notifSprite = new Sprite(notifTex);
        notifSprite.setSize(32f, 32f);

        talkReadyAnim = GameAssetManager.getGameAssetManager().getFrameAnimation(
            "ui/dialogue_ready",   // cache key (any unique string)
            "dialogue/",           // base path; method appends 1-N + ".png"
            7,                     // frameCount
            0.10f,                 // frameDuration (sec)
            true                   // looping
        );

        speechFont = GameAssetManager.getGameAssetManager().getSmallFont();
        dialogueBoxTex = GameAssetManager.getGameAssetManager().getTexture("dialogue/dialogue_box.png");

    }

    @Override
    public void render(float delta) {
        if (!uiOpen() && Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            worldController.endTurnAndAdvanceIfRoundDone();
            int newIndex = worldController.getCurrentPlayerIndex();
            Main.getMain().setScreen(new PlayerMapView(worldController, newIndex));
            return; // avoid rendering the old player after switching
        }

        if (!uiOpen() && Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            GameController[] ctrls = worldController.getAllControllers();

            java.util.List<PlayerWorldSlice> slices = new java.util.ArrayList<>(ctrls.length);
            for (GameController gc : ctrls) {
                final String homePath = gc.getHomeMapPath(); // ← each player’s outdoor TMX
                final com.badlogic.gdx.maps.tiled.TiledMap quadMap =
                    io.github.StardewValley.models.GameAssetManager.getGameAssetManager().loadFreshMap(homePath);

                slices.add(new PlayerWorldSlice() {
                    private final com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer renderer =
                        new com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer(quadMap, 1f);

                    @Override public com.badlogic.gdx.maps.tiled.TiledMap getMap() { return quadMap; }

                    @Override public com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer getRenderer() {
                        return renderer;
                    }

                    @Override public int getMapWidthTiles()  {
                        return quadMap.getProperties().get("width", Integer.class);
                    }
                    @Override public int getMapHeightTiles() {
                        return quadMap.getProperties().get("height", Integer.class);
                    }

                    @Override public String getMapId() {
                        return homePath; // for logging; WorldView doesn’t rely on this anymore
                    }

                    @Override
                    public void renderEntities(com.badlogic.gdx.graphics.g2d.SpriteBatch batch, boolean drawPlayer) {
                        // crops/items from the player’s game state
                        io.github.StardewValley.models.Tile[][] tiles = gc.getTiles();
                        for (int x = 0; x < tiles.length; x++) {
                            for (int y = 0; y < tiles[0].length; y++) {
                                io.github.StardewValley.models.Tile t = tiles[x][y];
                                if (t.hasCrop()) {
                                    String path = t.getCrop().getCurrentSpritePath();
                                    com.badlogic.gdx.graphics.Texture tex =
                                        io.github.StardewValley.models.GameAssetManager.getGameAssetManager().getTexture(path);
                                    float drawX = t.getWorldX(), drawY = t.getWorldY();
                                    batch.draw(tex,
                                        drawX + io.github.StardewValley.controllers.GameController.TILE_SIZE/2f - tex.getWidth()/2f,
                                        drawY + 3);
                                }
                            }
                        }

                        // draw the player *only if* we’re supposed to (i.e., not in NPC map)
                        if (drawPlayer) {
                            gc.render(batch);
                        }
                    }
                });
            }

            Main.getMain().setScreen(new WorldView(this, slices, worldController.getNpcSlice(), worldController));
            return;
        }

        if (!uiOpen() && Gdx.input.isKeyJustPressed(Input.Keys.R)) debugDraw = !debugDraw;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            togglePanel(Panel.INVENTORY, true,
                () -> { // open
                    if (!inventoryMenuView.isVisible()) inventoryMenuView.toggle();
                    int selected = inventoryRenderer.getSelectedIndex();
                    inventoryMenuView.setSelectedIndex(selected);
                },
                () -> { // close
                    if (inventoryMenuView.isVisible()) inventoryMenuView.toggle();
                    int selected = inventoryMenuView.getSelectedIndex();
                    inventoryRenderer.setSelectedIndex(selected);
                }
            );
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            togglePanel(Panel.CRAFTING, true,
                () -> { if (!craftingMenuView.isVisible()) craftingMenuView.toggle(); },
                () -> { if (craftingMenuView.isVisible())  craftingMenuView.toggle(); }
            );
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            togglePanel(Panel.CONTROLS, true,
                () -> { if (!controlsOverlay.isVisible()) controlsOverlay.toggle(); },
                () -> { if (controlsOverlay.isVisible())  controlsOverlay.toggle(); }
            );
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            togglePanel(Panel.JOURNAL, true,
                () -> { if (!journalOverlay.isVisible()) journalOverlay.toggle(); },
                () -> { if (journalOverlay.isVisible())  journalOverlay.toggle(); }
            );
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            togglePanel(Panel.SHOP, true,
                () -> { if (!shopView.isVisible()) shopView.toggle(); },
                () -> { if (shopView.isVisible())  shopView.toggle(); }
            );
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            togglePanel(Panel.COOKING, controller.isPlayerInHouse(),
                () -> { if (!cookingMenuView.isVisible()) cookingMenuView.toggle(); },
                () -> { if (cookingMenuView.isVisible())  cookingMenuView.toggle(); }
            );
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            togglePanel(Panel.QUEST, true,
                () -> { // open
                    var npc = worldController.npc().closestOn(
                        controller.getCurrentMapPath(), controller.getPlayerX(), controller.getPlayerY(), 32f
                    );
                    if (npc != null) npcQuestView.open(npc);
                },
                () -> npcQuestView.close()
            );
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            togglePanel(Panel.GIFT, true,
                () -> {
                    var npc = worldController.npc().closestOn(
                        controller.getCurrentMapPath(), controller.getPlayerX(), controller.getPlayerY(), 32f
                    );
                    if (npc != null && !giftPopupView.isVisible()) giftPopupView.open(npc);
                },
                () -> giftPopupView.close()
            );
        }

        if (!uiOpen() && Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            var npc = worldController.npc().closestOn(
                controller.getCurrentMapPath(), controller.getPlayerX(), controller.getPlayerY(), 32f
            );
            if (npc != null && worldController.npc().hasDialogue(controller, npc, controller.getGameTime())) {
                String line = worldController.npc().pickDialogue(controller, npc, controller.getGameTime());
                if (line != null) { activeSpeech = line; speechTimer = 3f; speakingNpc = npc; }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
            togglePanel(Panel.CHAT, true,
                () -> { if (!chatOverlay.isVisible()) chatOverlay.toggle();
                    String myId = worldController.playerIdOf(controller);
                    worldController.clearUnread(myId);
                },
                () -> { if (chatOverlay.isVisible()) chatOverlay.toggle(); }
            );
        }

        boolean menuVisible = inventoryMenuView.isVisible();

        if (menuWasVisible && !menuVisible) {
            int selected = inventoryMenuView.getSelectedIndex();
            inventoryRenderer.setSelectedIndex(selected);
        }

        menuWasVisible = menuVisible;

        if (!uiOpen()) {
            worldController.updateAll(delta);
            controller.updateFloatingIcons(delta);
        }

        TiledMap newMap = controller.getMap();
        if (newMap != map) {
            this.map = newMap;
            this.mapRenderer.setMap(newMap);

            int tileSize = 16;
            mapWidth = map.getProperties().get("width", Integer.class) * tileSize;
            mapHeight = map.getProperties().get("height", Integer.class) * tileSize;

            tiles = controller.getTiles();
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

        worldController.npc().renderOn(batch, controller.getCurrentMapPath());

        talkReadyTime += delta; // make sure this happens only once per frame
        for (var n : worldController.npc().getNpcsOn(controller.getCurrentMapPath())) {
            // hide while this npc is speaking
            if (activeSpeech != null && speakingNpc == n) continue;
            if (!worldController.npc().hasDialogue(controller, n, controller.getGameTime())) continue;

            var fr = n.getCurrentFrame();
            float nx = n.getX(), ny = n.getY();
            float w  = (fr != null) ? fr.getRegionWidth()  : 16f;
            float h  = (fr != null) ? fr.getRegionHeight() : 16f;

            float size = 12f;                 // world units (so it scales with zoom)
            float ix = nx + 0.7f + w * 0.5f - size * 0.5f;
            float iy = ny + h + 4f;           // just above the head

            var frame = talkReadyAnim.getKeyFrame(talkReadyTime, true);
            batch.draw(frame, ix, iy, size, size);
        }

        renderOtherPlayers(batch);
        controller.render(batch);

        for (var e : controller.getFloatingIcons()) {
            float t = com.badlogic.gdx.math.MathUtils.clamp(e.age / e.life, 0f, 1f);
            float alpha = 1f - t;
            float yOff  = e.rise * t;

            com.badlogic.gdx.graphics.Texture tex =
                io.github.StardewValley.models.GameAssetManager.getGameAssetManager().getTexture(e.texKey);

            batch.setColor(1f, 1f, 1f, alpha);
            // center on x; rise over time
            batch.draw(tex, e.x - e.size * 0.5f, e.y + yOff, e.size, e.size);
        }
        batch.setColor(com.badlogic.gdx.graphics.Color.WHITE);

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
// ── Energy bar (UI) ─────────────────────────────────────
        float barW = 140f, barH = 12f;
        float ex = clockBgSprite.getX() + clockBgSprite.getWidth() + 20f;
        float ey = clockBgSprite.getY() + 8f;

// چون می‌خوایم با ShapeRenderer بکشیم، اول Batch رو ببندیم
        batch.end();

        shapeRenderer.setProjectionMatrix(uiCamera.combined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

// پس‌زمینه
        shapeRenderer.setColor(0.15f, 0.15f, 0.2f, 1f);
        shapeRenderer.rect(ex, ey, barW, barH);

// مقدار
        float p = controller.getPlayer().getEnergy01();
// یه رنگ ثابت بزن که گرادیان نشه
        shapeRenderer.setColor(0.12f, 0.75f, 0.25f, 1f);
        shapeRenderer.rect(ex, ey, barW * p, barH);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

// برگرد به Batch برای متن‌ها و بقیه‌ی UI
        // برگرد به Batch برای متن‌ها و بقیه‌ی UI
        Gdx.gl.glEnable(GL20.GL_BLEND);   // ← مهم
        batch.begin();
        batch.setColor(1f, 1f, 1f, 1f);   // هر احتیاطی برای رنگ‌های قبلی
        BitmapFont smallFont = GameAssetManager.getGameAssetManager().getSmallFont();
        smallFont.draw(batch, "Energy: " + controller.getPlayer().getEnergy() + "/" +
                        controller.getPlayer().getMaxEnergy(),
                ex, ey + barH + 12);
        inventoryRenderer.setInputEnabled(!isMenuOpen());
        inventoryRenderer.render(batch);

        if (inventoryMenuView.isVisible()) {
            inventoryMenuView.render(batch);
        }

        if (cookingMenuView.isVisible()) {
            cookingMenuView.render(batch);
        }

        // ... after cooking/crafting renders, before batch.end()
        if (journalOverlay.isVisible()) {
            // اگه Big/Small font داری:
            BitmapFont big   = GameAssetManager.getGameAssetManager().getBigFont();
            BitmapFont small = GameAssetManager.getGameAssetManager().getSmallFont();
            journalOverlay.render(batch, big, small, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        if (controlsOverlay.isVisible() && !isMenuOpen()) {
            BitmapFont big   = GameAssetManager.getGameAssetManager().getBigFont();
            BitmapFont small = GameAssetManager.getGameAssetManager().getSmallFont();
            controlsOverlay.render(batch, big, small,
                    uiCamera.viewportWidth, uiCamera.viewportHeight);
        }

        if (craftingMenuView.isVisible()) {
            craftingMenuView.render(batch);
        }

        if (sellMenuView.isVisible())      sellMenuView.render(batch);

        if (shopView.isVisible())          shopView.render(batch);

        BitmapFont small = GameAssetManager.getGameAssetManager().getSmallFont();
        small.draw(batch, "Gold: "+ controller.getPlayer().getGameEconomy().getGold(), clockBgSprite.getX()+clockBgSprite.getWidth()+12, clockBgSprite.getY()+18);

        npcQuestView.render(batch);

        // --- Speech bubble (UI space) ---
        if (activeSpeech != null && speakingNpc != null) {
            speechTimer -= delta;

            if (speechTimer <= 0f) {
                // just clear, do NOT return here
                activeSpeech = null;
                speakingNpc  = null;
            } else {
                var fr = speakingNpc.getCurrentFrame();
                float nx = speakingNpc.getX(), ny = speakingNpc.getY();
                float w  = (fr != null) ? fr.getRegionWidth()  : 16f;
                float h  = (fr != null) ? fr.getRegionHeight() : 16f;

                tmpProject.set(nx + w * 0.5f, ny + h, 0f);
                viewport.project(tmpProject);

                float bw = 200f, bh = 64f;
                float bx = tmpProject.x - bw * 0.5f;
                float by = tmpProject.y + 14f;

                bx = MathUtils.clamp(bx, 4f, uiCamera.viewportWidth  - bw - 1f);
                by = MathUtils.clamp(by, 4f, uiCamera.viewportHeight - bh - 4f);

                batch.draw(dialogueBoxTex, bx, by, bw, bh);

                float pad = 21f;
                speechFont.setColor(Color.WHITE);
                speechFont.draw(batch, activeSpeech, bx + pad, by + bh - pad,
                    bw - pad * 2f, com.badlogic.gdx.utils.Align.left, true);
            }
        }

        // ----- Notifications bell (UI) -----
        String myId = worldController.playerIdOf(controller);
        int unread = worldController.getUnreadCount(myId);

// place it near the clock
        float bellX = clockBgSprite.getX() + clockBgSprite.getWidth() + 180f;
        float bellY = clockBgSprite.getY() + 20;
        notifSprite.setPosition(bellX, bellY);
        notifSprite.draw(batch);

// badge
        if (unread > 0) {
            // simple red circle + count
            // (no ShapeRenderer here; just text – or draw a small red dot texture if you have one)
            BitmapFont sf = GameAssetManager.getGameAssetManager().getSmallFont();
            sf.setColor(1, 0.2f, 0.2f, 1);              // red-ish
            sf.draw(batch, String.valueOf(unread), bellX + 22f, bellY + 26f);
            sf.setColor(1,1,1,1);
        }

        if (Gdx.input.isButtonJustPressed(com.badlogic.gdx.Input.Buttons.LEFT)) {
            float mx = Gdx.input.getX(), my = Gdx.graphics.getHeight() - Gdx.input.getY();
            float bw = notifSprite.getWidth() * notifSprite.getScaleX();
            float bh = notifSprite.getHeight() * notifSprite.getScaleY();
            if (mx >= bellX && mx <= bellX + bw && my >= bellY && my <= bellY + bh) {
                togglePanel(Panel.CHAT, true,
                    () -> { if (!chatOverlay.isVisible()) chatOverlay.toggle();
                        worldController.clearUnread(myId);
                    },
                    () -> { if (chatOverlay.isVisible()) chatOverlay.toggle(); }
                );
            }
        }


        if (chatOverlay != null && chatOverlay.isVisible()) {
            chatOverlay.render(batch);
        }

        if (giftPopupView != null && giftPopupView.isVisible()) {
            giftPopupView.render(batch);
        }

        batch.end();

        syncActivePanel();
    }

    private boolean uiOpen() { return activePanel != Panel.NONE; }

    private void togglePanel(Panel p, boolean canOpen, Runnable open, Runnable close) {
        if (activePanel == Panel.NONE) {
            if (canOpen) { open.run(); activePanel = p; }
        } else if (activePanel == p) {
            close.run(); activePanel = Panel.NONE;
        } // if some *other* panel is open, do nothing
    }

    private void syncActivePanel() {
        switch (activePanel) {
            case INVENTORY -> { if (!inventoryMenuView.isVisible()) activePanel = Panel.NONE; }
            case CRAFTING   -> { if (!craftingMenuView.isVisible())  activePanel = Panel.NONE; }
            case SHOP       -> { if (!shopView.isVisible())           activePanel = Panel.NONE; }
            case SELL       -> { if (!sellMenuView.isVisible())       activePanel = Panel.NONE; }
            case COOKING    -> { if (!cookingMenuView.isVisible())    activePanel = Panel.NONE; }
            case JOURNAL    -> { if (!journalOverlay.isVisible())     activePanel = Panel.NONE; }
            case CONTROLS   -> { if (!controlsOverlay.isVisible())    activePanel = Panel.NONE; }
            case QUEST      -> { if (!npcQuestView.isVisible())       activePanel = Panel.NONE; }
            case GIFT       -> { if (!giftPopupView.isVisible())      activePanel = Panel.NONE; }
            case CHAT       -> { if (chatOverlay == null || !chatOverlay.isVisible()) activePanel = Panel.NONE; }
            default -> {}
        }
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

    private boolean isMenuOpen() { return uiOpen(); }

    private void renderOtherPlayers(SpriteBatch batch) {
        if (worldController == null) return;

        TiledMap thisMap = controller.getMap();
        java.util.List<Player> others = new java.util.ArrayList<>();

        for (GameController gc : worldController.getAllControllers()) {
            if (gc == controller) continue;           // skip self
            if (gc.getMap() != thisMap) continue;     // only same *instance* of TiledMap
            others.add(gc.getPlayer());
        }

        // y-sort so lower sprites render in front
        others.sort((a, b) -> Float.compare(a.getY(), b.getY()));

        for (Player p : others) {
            p.render(batch);
        }
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
        if (notifTex != null) notifTex.dispose();
    }
}
