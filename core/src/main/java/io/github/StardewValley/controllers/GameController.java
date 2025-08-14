package io.github.StardewValley.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import io.github.StardewValley.models.*;
import io.github.StardewValley.models.Artisan.MachineInstance;
import io.github.StardewValley.models.Artisan.MachineType;
import io.github.StardewValley.models.enums.Skill;
import io.github.StardewValley.models.farming.Tree;
import io.github.StardewValley.models.farming.TreeSaplingType;
import io.github.StardewValley.models.farming.TreeType;
import io.github.StardewValley.views.GiftPopupView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameController {
    public static final int TILE_SIZE = 16;
    public static final float HARVEST_RANGE = 10f;
    // Energy costs
    private static final int EC_TILL = 2;
    private static final int EC_UNTILL = 1;
    private static final int EC_PLANT = 1;
    private static final int EC_HARVEST = 1;
    private static final int EC_WATER = 1;
    private static final int TILLED_TILE_ID = 89;
    private final List<MachineInstance> machines = new ArrayList<>();
    private final Player player;
    private final List<Door> doors = new ArrayList<>();
    private final List<RectangleMapObject> roofClickZones = new ArrayList<>();
    private final String homeMapPath;
    private final GameTime gameTime;
    private final TiledMap sharedNpcMap;
    private final java.util.Map<String, TiledMap> mapCache = new java.util.HashMap<>();
    private final com.badlogic.gdx.utils.Array<FloatingIcon> floatingIcons = new com.badlogic.gdx.utils.Array<>();
    private TiledMap map;
    private OrthographicCamera camera;
    private String currentMapPath;
    private String previousMapPath;
    private boolean roofVisible = true;
    private Weather weather;
    private WorldController worldController;
    private boolean greenhouseUnlocked = false;
    private Tile[][] tileGrid;
    private int lastUpdatedDay = -1;
    private boolean fridgeOpen = false;
    private boolean playingFridgeAnim = false;
    private float fridgeAnimTime = 0f;
    private RectangleMapObject fridgeObject;  // ← loaded from TMX
    private com.badlogic.gdx.math.Rectangle fridgeBounds;
    private Animation<TextureRegion> fridgeOpenAnim;
    private Animation<TextureRegion> fridgeCloseAnim;
    private TextureRegion frame;
    private float timeAccumulator = 0f;
    private GiftPopupView giftPopup;
    private DoorHook doorHook;

    public GameController(Player player, String initialMapPath, GameTime gameTime, TiledMap sharedNpcMap) {
        this.player = player;
        this.gameTime = gameTime;
        this.currentMapPath = initialMapPath;
        this.homeMapPath = initialMapPath;
        this.sharedNpcMap = sharedNpcMap;
        this.map = GameAssetManager.getGameAssetManager().loadFreshMap(currentMapPath);
        mapCache.put(currentMapPath, this.map);
        int width = map.getProperties().get("width", Integer.class);
        int height = map.getProperties().get("height", Integer.class);
        tileGrid = new Tile[width][height];

        // Set all tiles as walkable by default
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tileGrid[x][y] = new Tile(true, x * TILE_SIZE, y * TILE_SIZE);
            }
        }

        loadFridgeAnimations();
        loadFridgeFromMap();

        this.weather = new Weather(WeatherType.SUNNY);
        loadDoorsFromMap();
        loadRoofClickZones();
    }

    public List<MachineInstance> getMachines() {
        return machines;
    }

    public Tile getTileAt(int tileX, int tileY) {
        if (tileX < 0 || tileY < 0 || tileX >= tileGrid.length || tileY >= tileGrid[0].length)
            return null;
        return tileGrid[tileX][tileY];
    }

    public Tile[][] getTiles() {
        return tileGrid; // This must be declared and initialized properly
    }

    public Weather getWeather() {
        return weather;
    }

    public TiledMap getMap() {
        return map;
    }

    public String getCurrentMapPath() {
        return currentMapPath;
    }

    // add an overload (keep your existing update(...) if you want)
    public void update(float delta, boolean canAct) {
        if (canAct) {
            handleInput(delta);     // ← only the active player reads input & moves
        }
        player.update(delta);

        for (int x = 0; x < tileGrid.length; x++) {
            for (int y = 0; y < tileGrid[0].length; y++) {
                var t = tileGrid[x][y];
                if (t.getContent() instanceof Tree tree) {
                    tree.updateRealtime(delta);
                }
            }
        }// animations etc. (keep this)

        // award wood + clear tile after chop animation finishes
        for (int x = 0; x < tileGrid.length; x++) {
            for (int y = 0; y < tileGrid[0].length; y++) {
                var t = tileGrid[x][y];
                if (t.getContent() instanceof Tree tree && tree.needsChopPayout()) {
                    int wood = tree.getType().woodYield();
                    player.getInventory().add(MaterialType.Wood, wood);

                    spawnFloatingIcon(MaterialType.Wood.iconPath(),
                        t.getWorldX() + TILE_SIZE / 2f, t.getWorldY() + TILE_SIZE, 0.9f);

                    // remove the tree from the tile completely
                    t.setContent(null);
                }
            }
        }

        if (gameTime.getDay() != lastUpdatedDay) {
            boolean inGreenhouse = isGreenhouseMap();
            for (int x = 0; x < tileGrid.length; x++) {
                for (int y = 0; y < tileGrid[0].length; y++) {
                    var t = tileGrid[x][y];
                    if (t.getContent() instanceof Crop c) {
                        c.updateDaily(inGreenhouse);
                    } else if (t.getContent() instanceof Tree tree) {
                        tree.updateDaily(inGreenhouse);   // ← pass it through
                    }
                }
            }
            lastUpdatedDay = gameTime.getDay();
        }


        for (MachineInstance m : machines) m.update(delta);


        TiledMapTileLayer roofLayer = (TiledMapTileLayer) map.getLayers().get("RoofTiles");
        if (roofLayer != null && roofLayer.isVisible() && !isPlayerInHouse()) {
            fridgeOpen = false;
            playingFridgeAnim = false;
        }

        if (playingFridgeAnim) {
            fridgeAnimTime += delta;
            Animation<TextureRegion> current = fridgeOpen ? fridgeOpenAnim : fridgeCloseAnim;
            if (fridgeAnimTime >= current.getAnimationDuration()) {
                playingFridgeAnim = false;
            }
        }
    }

    public void addMachine(MachineType type, int tileX, int tileY) {
        machines.add(new MachineInstance(type, tileX, tileY));
    }

    public MachineInstance findNearestMachine(float worldX, float worldY, float radius) {
        MachineInstance best = null;
        float best2 = radius * radius;
        for (MachineInstance m : machines) {
            float cx = m.tileX * TILE_SIZE + TILE_SIZE / 2f;
            float cy = m.tileY * TILE_SIZE + TILE_SIZE / 2f;
            float dx = worldX - cx, dy = worldY - cy;
            float d2 = dx * dx + dy * dy;
            if (d2 <= best2) {
                best2 = d2;
                best = m;
            }
        }
        return best;
    }

    private void handleInput(float delta) {
        if (player.isActionLocked()) return;

        float speed = 100f;
        float newX = player.getX();
        float newY = player.getY();
        boolean moved = false;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            float tentativeX = newX - speed * delta;
            if (canMoveTo(tentativeX, newY)) {
                newX = tentativeX;
                moved = true;
                player.setAnimation("walk/left", "character/walk/left", 2, 0.2f, true);

            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            float tentativeX = newX + speed * delta;
            if (canMoveTo(tentativeX, newY)) {
                newX = tentativeX;
                moved = true;
                player.setAnimation("walk/right", "character/walk/right", 2, 0.2f, true);
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            float tentativeY = newY + speed * delta;
            if (canMoveTo(newX, tentativeY)) {
                newY = tentativeY;
                moved = true;
                player.setAnimation("walk/up", "character/walk/up", 2, 0.2f, true);
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            float tentativeY = newY - speed * delta;
            if (canMoveTo(newX, tentativeY)) {
                newY = tentativeY;
                moved = true;
                player.setAnimation("walk/down", "character/walk/down", 2, 0.2f, true);
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            for (Door door : doors) {
                if (!door.isPlayerInside(player.getX(), player.getY())) continue;

                // resolve target path same way enterDoor() does
                String targetMapPath = door.targetMap.equals("previousMap")
                    ? previousMapPath
                    : (door.targetMap.startsWith("maps/") ? door.targetMap : "maps/" + door.targetMap);

                boolean isGreenhouseTarget = (targetMapPath != null &&
                    targetMapPath.toLowerCase().contains("greenhouse"));

                if (isGreenhouseTarget && !isGreenhouseUnlocked()) {
                    // ask UI to open unlock popup
                    if (doorHook != null) doorHook.onTryEnterGreenhouse(door);
                    break; // stop here; don't enter yet
                }

                // normal enter (already unlocked or not greenhouse)
                enterDoor(door);
                break;
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            if (worldController != null) {
                var npc = worldController.npc().closestOn(currentMapPath, player.getX(), player.getY(), 32f);
                if (npc != null) {
                    int gained = worldController.npc().talk(this, npc, gameTime);
                    // TODO: show toast if gained > 0 (first talk today), otherwise "already talked today"
                    Gdx.app.log("Talk", "points=" + gained);
                }
            }
        } else if (Gdx.input.justTouched() && camera != null) {
            Vector3 world = camera.unproject(
                new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            for (RectangleMapObject rect : roofClickZones) {
                if (rect.getRectangle().contains(world.x, world.y)) {
                    if (isPlayerInHouse()) continue;  // ⛔ Can't toggle roof inside
                    roofVisible = !roofVisible;

                    TiledMapTileLayer roofTiles = (TiledMapTileLayer) map.getLayers().get("RoofTiles");
                    if (roofTiles != null) {
                        roofTiles.setVisible(roofVisible);
                    }
                    break;
                }
            }

            if (isPlayerInHouse() && fridgeBounds.contains(world.x, world.y)) {
                if (!playingFridgeAnim) {
                    playingFridgeAnim = true;
                    fridgeAnimTime = 0f;
                    fridgeOpen = !fridgeOpen;
                }
            }


            if (harvestNearestCrop(world.x, world.y)) {
                String direction = player.getFacingDirection();  // "down", "left", etc.
//                player.setAnimation("pickaxe_" + direction, "character/pickaxe/" + direction, 2, 0.1f, false);
                player.setActionCooldown(0.3f);
                return;                      // we harvested, skip further click checks
            }


            int tileX = (int) (world.x / TILE_SIZE);
            int tileY = (int) (world.y / TILE_SIZE);

            // after: int tileX = ..., int tileY = ...;
            Tile gameTile = getTileAt(tileX, tileY);
            ItemType held = player.getInventoryRenderer().getSelectedType();

            // --- CLICK TO PLANT: seeds & tree saplings ---
            if (held instanceof SeedType seed) {
                // only plant on empty tile
                if (gameTile != null && gameTile.getContent() == null) {
                    if (!player.hasEnergy(EC_PLANT)) return;
                    if (plantCrop(tileX, tileY, seed.product())) {
                        player.consumeEnergy(EC_PLANT);
                        player.getInventory().remove(seed, 1);

                        // quick plant animation (reuse hoe or pick any “plant” anim you have)
                        String dir = player.getFacingDirection();
                        player.setAnimation("hoe_" + dir, "character/hoe/" + dir, 2, 0.10f, false);
                        player.setActionCooldown(0.25f);
                    }
                }
                return; // handled click
            }

            if (held instanceof TreeSaplingType sapling) {
                // only plant on empty tile
                if (gameTile != null && gameTile.getContent() == null) {
                    if (!player.hasEnergy(EC_PLANT)) return;
                    if (plantTree(tileX, tileY, sapling.growsInto())) {
                        player.consumeEnergy(EC_PLANT);
                        player.getInventory().remove(sapling, 1);

                        String dir = player.getFacingDirection();
                        player.setAnimation("hoe_" + dir, "character/hoe/" + dir, 2, 0.10f, false);
                        player.setActionCooldown(0.25f);
                    }
                }
                return; // handled click
            }


            // --- Greenhouse-safe: direct "clicked tile" harvest before any Grass layer logic ---
            if (held instanceof ToolType t && t == ToolType.SCYTHE) {
                Tile clicked = getTileAt(tileX, tileY);
                if (clicked != null && clicked.getContent() instanceof Crop c && !c.isHarvested()) {
                    if (c.isDead() || c.isFullyGrown()) {
                        if (!player.hasEnergy(EC_HARVEST)) return;
                        player.consumeEnergy(EC_HARVEST);

                        boolean didHarvest = harvestCropOnTile(c, clicked);

                        if (didHarvest) {
                            String dir = player.getFacingDirection();
                            player.setAnimation("pickaxe_" + dir, "character/pickaxe/" + dir, 2, 0.1f, false);
                            player.setActionCooldown(0.3f);
                        }
                        return; // handled; skip rest of click checks
                    }
                }
            }

            if (held instanceof ToolType) {
                ToolType tool = (ToolType) held;

                // ----- WATERING -----
                if (tool == ToolType.WATERCAN && gameTile != null) {
                    Object content = gameTile.getContent();

                    if (content instanceof Crop crop) {
                        if (!crop.isDead() && !player.isActionLocked()) {
                            if (!player.hasEnergy(EC_WATER)) return;
                            crop.water();
                            player.consumeEnergy(EC_WATER);
                            String direction = player.getFacingDirection();
                            player.setAnimation("watercan_" + direction, "character/watercan/" + direction, 2, 0.12f, false);
                            player.setActionCooldown(0.25f);
                        }
                        return;
                    } else if (content instanceof Tree tree) {
                        if (!tree.isDead() && !player.isActionLocked()) {
                            if (!player.hasEnergy(EC_WATER)) return;
                            tree.water();  // sets wateredToday=true
                            player.consumeEnergy(EC_WATER);
                            String direction = player.getFacingDirection();
                            player.setAnimation("watercan_" + direction, "character/watercan/" + direction, 2, 0.12f, false);
                            player.setActionCooldown(0.25f);
                        }
                        return;
                    }
                }

            }

            // --- Trees: shake for fruit / chop for wood ---
            if (gameTile != null && gameTile.getContent() instanceof Tree tree) {
                // what tool are we holding?
                ToolType tool = (held instanceof ToolType) ? (ToolType) held : null;

                // 1) Shake fruit (allow with scythe or empty hand)
                boolean canShakeWithThis = (tool == null || tool == ToolType.SCYTHE);
                if (canShakeWithThis && tree.canShakeFruit()) {
                    if (!player.hasEnergy(EC_HARVEST)) return;
                    player.consumeEnergy(EC_HARVEST);

                    ItemType fruit = tree.collectFruit();
                    if (fruit != null) {
                        player.getInventory().add(fruit, 1);
                    }

                    // quick shake animation reuse (pickaxe swing looks ok as a “shake”)
                    String dir = player.getFacingDirection();
                    player.setAnimation("pickaxe_" + dir, "character/pickaxe/" + dir, 2, 0.10f, false);
                    player.setActionCooldown(0.25f);
                    return;
                }

                // 2) Chop tree (use AXE if you have it; otherwise reuse PICKAXE)
                boolean isChopTool =
                    tool == ToolType.AXE || tool == ToolType.PICKAXE; // change if you have a dedicated AXE
                if (isChopTool && tree.isMature() && !tree.isChopped()) {
                    // pick an energy cost for chopping (separate from harvest)
                    final int EC_CHOP = 4;
                    if (!player.hasEnergy(EC_CHOP)) return;
                    player.consumeEnergy(EC_CHOP);

                    // was: tree.chopDown(); ... gameTile.setContent(null); (remove these)

// trigger the animated chop (payout later)
                    tree.playChop();

// energy, animation, cooldown (keep these)
                    String dir = player.getFacingDirection();
                    player.setAnimation("pickaxe_" + dir, "character/pickaxe/" + dir, 2, 0.11f, false);
                    player.setActionCooldown(0.3f);

// optional XP stays here (or move to payout time if you prefer)
                    if (player.getSkills().get(Skill.FORAGING).addXp(5)) player.addMaxEnergy(5);

                    return;
                }

                // If we clicked a tree but didn’t match any action, just stop further ground edits.
                return;
            }


            TiledMapTileLayer grassLayer = (TiledMapTileLayer) map.getLayers().get("Grass");
            if (grassLayer == null) return;

//            if (!(held instanceof ToolType) || held != ToolType.SCYTHE) return;
            if (!(held instanceof ToolType)) return;
            ToolType tool = (ToolType) held;

            TiledMapTileLayer.Cell cell = grassLayer.getCell(tileX, tileY);
            if (cell == null) return;

            if (gameTile == null || gameTile.getContent() != null) return;

            List<Integer> grassIds = Arrays.asList(
                1, 2, 3, 4, 5, 6,
                11, 12, 13, 14, 15, 16, 43
            );
            int tilledId = 89;

            // Check if any top layer is blocking tilling
            String[] blockingLayers = {"Water", "Quarry", "House", "Greenhouse"};
            for (String layerName : blockingLayers) {
                TiledMapTileLayer topLayer = (TiledMapTileLayer) map.getLayers().get(layerName);
                if (topLayer != null) {
                    TiledMapTileLayer.Cell topCell = topLayer.getCell(tileX, tileY);
                    if (topCell != null && topCell.getTile() != null) {
                        return; // blocked by structure or water
                    }
                }
            }

            int currentId = cell.getTile().getId();

            String direction = player.getFacingDirection();

            if (tool == ToolType.PICKAXE && grassIds.contains(currentId)) {
                if (!player.hasEnergy(EC_TILL)) return;           // کمبود انرژی
                player.consumeEnergy(EC_TILL);
                cell.setTile(map.getTileSets().getTile(tilledId));
                player.setAnimation("hoe_" + direction, "character/hoe/" + direction, 2, 0.1f, false);
                player.setActionCooldown(0.3f);
                // XP
                if (player.getSkills().get(Skill.FARMING).addXp(2)) {
                    player.addMaxEnergy(10);
                }
            } else if (tool == ToolType.PICKAXE && currentId == tilledId) {
                if (!player.hasEnergy(EC_UNTILL)) return;
                player.consumeEnergy(EC_UNTILL);
                cell.setTile(map.getTileSets().getTile(43));
                player.setAnimation("pickaxe_" + direction, "character/pickaxe/" + direction, 2, 0.1f, false);
                player.setActionCooldown(0.3f);
                if (player.getSkills().get(Skill.FORAGING).addXp(1)) {
                    player.addMaxEnergy(5);
                }
            }

        }

        if (!moved && !player.isActionLocked()) {
            String direction = player.getFacingDirection();
            player.setAnimation("idle_" + direction, "character/idle/" + direction, 2, 0.4f, true);
        }


        if (moved) {
            float clampedX = MathUtils.clamp(newX, 0, getMapWidthInPixels() - player.getWidth());
            float clampedY = MathUtils.clamp(newY, 0, getMapHeightInPixels() - player.getHeight());
            player.setX(clampedX);
            player.setY(clampedY);
        }
    }

    public GameTime getGameTime() {
        return gameTime;
    }

    public void render(SpriteBatch batch) {
        player.render(batch);

        TiledMapTileLayer roofLayer = (TiledMapTileLayer) map.getLayers().get("RoofTiles");
        if (roofLayer != null && roofLayer.isVisible()) return;

        if (fridgeBounds == null) return; // ← add this

        if (playingFridgeAnim) {
            Animation<TextureRegion> anim = fridgeOpen ? fridgeOpenAnim : fridgeCloseAnim;
            frame = anim.getKeyFrame(fridgeAnimTime, false);
        } else {
            frame = fridgeOpen ? fridgeOpenAnim.getKeyFrames()[4] : fridgeOpenAnim.getKeyFrames()[0];
        }
        batch.draw(frame, fridgeBounds.x, fridgeBounds.y);
    }

    // GameController.java
    public boolean plantCrop(int tileX, int tileY, CropType type) {
        Tile tile = getTileAt(tileX, tileY);
        if (tile == null || type == null) return false;
        if (tile.getContent() != null) return false;

        if (isGreenhouseMap()) {
            // block walls/collision only
            TiledMapTileLayer collision = (TiledMapTileLayer) map.getLayers().get("Collision");
            if (collision != null) {
                TiledMapTileLayer.Cell c = collision.getCell(tileX, tileY);
                if (c != null && c.getTile() != null) return false;
            }
            Crop crop = new Crop(type);
            crop.setAutoWaterForever(true);    // ← mark it greenhouse-style
            tile.setContent(crop);
            return true;
        }

        // normal rule: must be tilled
        TiledMapTileLayer grassLayer = (TiledMapTileLayer) map.getLayers().get("Grass");
        if (grassLayer == null) return false;
        TiledMapTileLayer.Cell cell = grassLayer.getCell(tileX, tileY);
        if (cell == null || cell.getTile() == null) return false;
        int tileId = cell.getTile().getId();
        if (tileId != TILLED_TILE_ID) return false;

        tile.setContent(new Crop(type));
        return true;
    }

    // Returns true if something was harvested (so caller can play anim/cooldown)
    private boolean harvestCropOnTile(Crop crop, Tile tile) {
        boolean wasDead = crop.isDead();
        boolean wasMature = crop.isFullyGrown();

        // Will the plant stay (regrow) or be removed?
        boolean removeTile = crop.harvestAndMaybeRegrow();

        // reward only if it was mature and not dead
        if (!wasDead && wasMature) {
            player.getInventory().add(crop.getCropType(), 1);
            if (player.getSkills().get(Skill.FARMING).addXp(5)) {
                player.addMaxEnergy(10);
            }
        }

        if (removeTile) {
            tile.setContent(null);
        }

        return (wasDead || wasMature); // true means we actually harvested something
    }

    // GameController.java
    public boolean plantTree(int tileX, int tileY, TreeType type) {
        Tile tile = getTileAt(tileX, tileY);
        if (tile == null || type == null) return false;
        if (tile.getContent() != null) return false;

        // block walls/collision only (trees don’t care about tilled dirt)
        TiledMapTileLayer collision = (TiledMapTileLayer) map.getLayers().get("Collision");
        if (collision != null) {
            TiledMapTileLayer.Cell c = collision.getCell(tileX, tileY);
            if (c != null && c.getTile() != null) return false;
        }

        // block obvious structure layers too
        for (String layerName : new String[]{"House", "Water", "Quarry"}) {
            TiledMapTileLayer top = (TiledMapTileLayer) map.getLayers().get(layerName);
            if (top != null) {
                var cell = top.getCell(tileX, tileY);
                if (cell != null && cell.getTile() != null) return false;
            }
        }

        tile.setContent(new Tree(type));
        return true;
    }

    private boolean harvestNearestCrop(float mx, float my) {
        ItemType held = player.getInventoryRenderer().getSelectedType();
        if (!(held instanceof ToolType) || held != ToolType.SCYTHE) {
            return false;
        }

        Crop nearestCrop = null;
        Tile cropTile = null;
        float bestD2 = HARVEST_RANGE * HARVEST_RANGE;

        int cx = (int) (mx / TILE_SIZE);
        int cy = (int) (my / TILE_SIZE);

        for (int ty = cy - 2; ty <= cy + 2; ty++) {
            for (int tx = cx - 2; tx <= cx + 2; tx++) {
                Tile tile = getTileAt(tx, ty);
                if (tile == null || !(tile.getContent() instanceof Crop)) continue;

                Crop crop = (Crop) tile.getContent();
                if (crop.isHarvested()) continue;

                // ✅ now allow dead OR fully grown
                if (!(crop.isDead() || crop.isFullyGrown())) continue;

                float cxWorld = tile.getWorldX() + TILE_SIZE * 0.5f;
                float cyWorld = tile.getWorldY() + TILE_SIZE * 0.5f;
                float dx = mx - cxWorld, dy = my - cyWorld;
                float d2 = dx * dx + dy * dy;

                if (d2 < bestD2) {
                    bestD2 = d2;
                    nearestCrop = crop;
                    cropTile = tile;
                }
            }
        }

        if (nearestCrop != null) {
            if (!player.hasEnergy(EC_HARVEST)) return false;
            player.consumeEnergy(EC_HARVEST);

            boolean didHarvest = harvestCropOnTile(nearestCrop, cropTile);

            if (didHarvest) {
                String direction = player.getFacingDirection();
                player.setActionCooldown(0.3f);
                player.setAnimation("pickaxe_" + direction, "character/pickaxe/" + direction, 2, 0.1f, false);
            }

            return didHarvest;
        }
        return false;
    }

    public float getPlayerX() {
        return player.getX();
    }

    public float getPlayerY() {
        return player.getY();
    }

    public void dispose() {
        player.dispose();
        for (TiledMap m : mapCache.values()) {
            if (m != null && m != sharedNpcMap) m.dispose();
        }
        mapCache.clear();
    }

    public boolean canMoveTo(float newX, float newY) {
        float width  = player.getWidth();
        float height = player.getHeight();

        // Player AABB in world space
        com.badlogic.gdx.math.Rectangle playerRect =
            new com.badlogic.gdx.math.Rectangle(newX, newY, width, height);

        // Layers
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("Collision");
        TiledMapTileLayer floorLayer     = (TiledMapTileLayer) map.getLayers().get("Floor");

        // --- 1) Tile-layer (TMX) blocking as before ---
        int[][] corners = {
            {(int)(newX / TILE_SIZE),                    (int)(newY / TILE_SIZE)},
            {(int)((newX + width  - 1) / TILE_SIZE),     (int)(newY / TILE_SIZE)},
            {(int)(newX / TILE_SIZE),                    (int)((newY + height - 1) / TILE_SIZE)},
            {(int)((newX + width  - 1) / TILE_SIZE),     (int)((newY + height - 1) / TILE_SIZE)}
        };

        for (int[] c : corners) {
            int tx = c[0], ty = c[1];

            if (tx < 0 || ty < 0 ||
                tx >= collisionLayer.getWidth() || ty >= collisionLayer.getHeight()) {
                return false; // outside map
            }

            TiledMapTileLayer.Cell col = collisionLayer.getCell(tx, ty);
            if (col != null && col.getTile() != null) return false;

            if (floorLayer != null) {
                TiledMapTileLayer.Cell floor = floorLayer.getCell(tx, ty);
                if (floor != null && floor.getTile() != null) return false;
            }
        }

        // --- 2) Game-logic blocking: crops/trees in your tileGrid ---
        // Find all tiles touched by the proposed AABB and block if any has content
        int minTx = (int)Math.floor(newX / TILE_SIZE);
        int maxTx = (int)Math.floor((newX + width  - 1) / TILE_SIZE);
        int minTy = (int)Math.floor(newY / TILE_SIZE);
        int maxTy = (int)Math.floor((newY + height - 1) / TILE_SIZE);

        for (int ty = minTy; ty <= maxTy; ty++) {
            for (int tx = minTx; tx <= maxTx; tx++) {
                Tile t = getTileAt(tx, ty);
                if (t == null) return false; // off-map safety

                Object content = t.getContent();
                if (content == null) continue;

                // Block on any crop or tree (adjust rules if you want exceptions)
                if (content instanceof Crop crop) {
                    // If you want to allow walking on harvested stubble, change to:
                    // if (!crop.isHarvested()) return false;
                    return false;
                }
                if (content instanceof io.github.StardewValley.models.farming.Tree tree) {
                    // If you want to allow after it’s chopped, change to:
                    // if (!tree.isChopped()) return false;
                    return false;
                }

                // Any other occupied content also blocks
                return false;
            }
        }

        // --- 3) World objects (shops etc.) ---
        if (worldController != null) {
            var shops = worldController.npc().getShopsOn(currentMapPath);
            for (var s : shops) {
                if (s.bounds.overlaps(playerRect)) return false;
            }
        }

        return true;
    }


    private int getMapWidthInPixels() {
        return map.getProperties().get("width", Integer.class) * TILE_SIZE;
    }

    private int getMapHeightInPixels() {
        return map.getProperties().get("height", Integer.class) * TILE_SIZE;
    }

    private void loadDoorsFromMap() {
        MapLayer doorLayer = map.getLayers().get("Doors");
        if (doorLayer == null) return;

        for (MapObject obj : doorLayer.getObjects()) {
            if (!(obj instanceof RectangleMapObject)) continue;

            RectangleMapObject rectObj = (RectangleMapObject) obj;
            MapProperties props = obj.getProperties();

            String targetMap = props.get("targetMap", String.class);
            if (targetMap == null) targetMap = "same";

            boolean fade = props.get("fade", false, Boolean.class);
            Integer spawnX = props.get("spawnX", null, Integer.class);
            Integer spawnY = props.get("spawnY", null, Integer.class);

            int offsetX = props.get("offsetX", 0, Integer.class);
            int offsetY = props.get("offsetY", 0, Integer.class);

            if (targetMap.equals("same") && (spawnX == null || spawnY == null)) {
                // calculate spawn from door rectangle and offset
                spawnX = (int) (rectObj.getRectangle().x / TILE_SIZE) + offsetX;
                spawnY = (int) (rectObj.getRectangle().y / TILE_SIZE) + offsetY;
            }

            // final sanity check
            if (spawnX == null || spawnY == null) {
                continue; // skip this door
            }

            doors.add(new Door(
                rectObj.getRectangle().x,
                rectObj.getRectangle().y,
                rectObj.getRectangle().width,
                rectObj.getRectangle().height,
                targetMap,
                spawnX * TILE_SIZE,
                spawnY * TILE_SIZE,
                fade
            ));
        }
    }

    private void enterDoor(Door door) {
        if (door.targetMap.equals("same")) {
            player.setX(door.spawnX);
            player.setY(door.spawnY);

            TiledMapTileLayer roofTiles = (TiledMapTileLayer) map.getLayers().get("RoofTiles");
            if (roofTiles != null) {
                roofTiles.setVisible(false); // or true when exiting
            }

            return;

        }

        String targetMapPath = door.targetMap.equals("previousMap")
            ? previousMapPath
            : (door.targetMap.startsWith("maps/") ? door.targetMap : "maps/" + door.targetMap);

        if (targetMapPath == null) return;

        // one shared NPC map; everything else is per-controller
        TiledMap newMap;
        if (targetMapPath.equals("maps/npcMap.tmx")) {
            newMap = sharedNpcMap;
        } else {
            newMap = mapCache.get(targetMapPath);
            if (newMap == null) {
                newMap = GameAssetManager.getGameAssetManager().loadFreshMap(targetMapPath);
                mapCache.put(targetMapPath, newMap);
            }
        }

        previousMapPath = currentMapPath;
        currentMapPath = targetMapPath;
        player.setX(door.spawnX);
        player.setY(door.spawnY);

        this.map = newMap;

        doors.clear();
        roofClickZones.clear();
        loadDoorsFromMap();
        loadRoofClickZones();
    }

    private void loadRoofClickZones() {
        MapLayer roofLayer = map.getLayers().get("Roof");
        if (roofLayer == null) return;

        for (MapObject obj : roofLayer.getObjects()) {
            if (obj instanceof RectangleMapObject) {
                MapProperties props = obj.getProperties();
                if ("hideRoof".equals(props.get("type", String.class))) {
                    roofClickZones.add((RectangleMapObject) obj);
                }
            }
        }
    }

    private void loadFridgeAnimations() {
        fridgeOpenAnim = GameAssetManager.getGameAssetManager().getFridgeOpenAnimation();

        Array<TextureRegion> reversedFrames = new Array<>(fridgeOpenAnim.getKeyFrames());
        reversedFrames.reverse();
        fridgeCloseAnim = new Animation<>(0.1f, reversedFrames, Animation.PlayMode.NORMAL);
    }

    private void loadFridgeFromMap() {
        MapLayer objectLayer = map.getLayers().get("Objects");
        if (objectLayer == null) return;

        for (MapObject obj : objectLayer.getObjects()) {
            if (!(obj instanceof RectangleMapObject)) continue;

            MapProperties props = obj.getProperties();
            String type = props.get("type", String.class);
            if ("fridge".equalsIgnoreCase(type)) {
                fridgeObject = (RectangleMapObject) obj;
                fridgeBounds = fridgeObject.getRectangle();

                String state = props.get("state", String.class);
                fridgeOpen = "open".equalsIgnoreCase(state);  // optional use
                break;
            }
        }
    }

    public boolean isPlayerInHouse() {
        TiledMapTileLayer houseLayer = (TiledMapTileLayer) map.getLayers().get("House");
        if (houseLayer == null) return false;

        // Get tile coordinates under the player
        int tileX = (int) (player.getX() / TILE_SIZE);
        int tileY = (int) (player.getY() / TILE_SIZE);

        TiledMapTileLayer.Cell cell = houseLayer.getCell(tileX, tileY);
        return cell != null && cell.getTile() != null;
    }

    public GiftPopupView getGiftPopup() {        // <-- add this
        return giftPopup;
    }

    public void setGiftPopup(GiftPopupView v) {  // <-- add this
        this.giftPopup = v;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public Player getPlayer() {
        return player;
    }

    public String getHomeMapPath() {                      // ← getter
        return homeMapPath;
    }

    public void setWorldController(WorldController wc) {
        this.worldController = wc;
    }

    public void spawnFloatingIcon(String texKey, float worldX, float worldY, float lifeSec) {
        FloatingIcon e = new FloatingIcon();
        e.texKey = texKey;
        e.x = worldX;
        e.y = worldY;
        e.life = lifeSec;
        floatingIcons.add(e);
    }

    public com.badlogic.gdx.utils.Array<FloatingIcon> getFloatingIcons() {
        return floatingIcons;
    }

    private boolean isGreenhouseMap() {
        String p = (currentMapPath == null) ? "" : currentMapPath.toLowerCase();
        if (p.contains("greenhouse")) return true; // works for Greenhouse.tmx / greenhouse.tmx

        Boolean prop = map.getProperties().get("isGreenhouse", Boolean.class);
        return prop != null && prop;
    }

    public boolean isGreenhouseUnlocked() {
        return greenhouseUnlocked;
    }

    public void setGreenhouseUnlocked(boolean v) {
        greenhouseUnlocked = v;
    }

    public void setDoorHook(DoorHook hook) {
        this.doorHook = hook;
    }

    // Allow UI to actually enter after unlocking
    public void enterDoorFromUI(Door door) {
        enterDoor(door);
    }

    public void updateFloatingIcons(float dt) {
        for (int i = floatingIcons.size - 1; i >= 0; i--) {
            FloatingIcon e = floatingIcons.get(i);
            e.age += dt;
            if (e.age >= e.life) floatingIcons.removeIndex(i);
        }
    }

    // UI hook so the view can show the unlock popup instead of entering
    public interface DoorHook {
        void onTryEnterGreenhouse(Door door);
    }

    public static class FloatingIcon {
        public float x, y;          // world coordinates
        public float age = 0f;      // seconds elapsed
        public float life = 1.0f;   // seconds total
        public String texKey;       // texture key in GameAssetManager (e.g., "gift.png")
        public float rise = 24f;    // how many world units to rise over life
        public float size = 16f;    // draw size (world units)
    }

}
