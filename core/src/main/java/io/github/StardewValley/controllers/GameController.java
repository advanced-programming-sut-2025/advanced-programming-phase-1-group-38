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
    private final List<MachineInstance> machines = new ArrayList<>();
    public List<MachineInstance> getMachines() { return machines; }
    private final Player player;
    private final List<Door> doors = new ArrayList<>();
    private final List<RectangleMapObject> roofClickZones = new ArrayList<>();

    private TiledMap map;
    private OrthographicCamera camera;
    private String currentMapPath;
    private String previousMapPath;
    private final String homeMapPath;
    private boolean roofVisible = true;
    private final GameTime gameTime;
    private Weather weather;
    private WorldController worldController;

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

    private final TiledMap sharedNpcMap;
    private final java.util.Map<String, TiledMap> mapCache = new java.util.HashMap<>();


    private static final int TILLED_TILE_ID = 89;

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
        player.update(delta);        // animations etc. (keep this)

        if (gameTime.getDay() != lastUpdatedDay) {
            for (int x = 0; x < tileGrid.length; x++) {
                for (int y = 0; y < tileGrid[0].length; y++) {
                    tileGrid[x][y].updateDaily();
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
            float cx = m.tileX * TILE_SIZE + TILE_SIZE/2f;
            float cy = m.tileY * TILE_SIZE + TILE_SIZE/2f;
            float dx = worldX - cx, dy = worldY - cy;
            float d2 = dx*dx + dy*dy;
            if (d2 <= best2) { best2 = d2; best = m; }
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
                if (door.isPlayerInside(player.getX(), player.getY())) {
                    enterDoor(door);
                    break;
                }
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

            TiledMapTileLayer grassLayer = (TiledMapTileLayer) map.getLayers().get("Grass");
            if (grassLayer == null) return;

            ItemType held = player.getInventoryRenderer().getSelectedType();
//            if (!(held instanceof ToolType) || held != ToolType.SCYTHE) return;
            if (!(held instanceof ToolType)) return;
            ToolType tool = (ToolType) held;

            TiledMapTileLayer.Cell cell = grassLayer.getCell(tileX, tileY);
            if (cell == null) return;
            Tile gameTile = getTileAt(tileX, tileY);
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

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {

            int tileX = (int) (player.getX() / TILE_SIZE);
            int tileY = (int) (player.getY() / TILE_SIZE);

            ItemType selectedItem = player.getInventoryRenderer().getSelectedType();
            if (selectedItem instanceof SeedType) {
                SeedType seed = (SeedType) selectedItem;
                if (!player.hasEnergy(EC_PLANT)) return;
                if (plantCrop(tileX, tileY, seed.product())) {
                    player.consumeEnergy(EC_PLANT);
                    player.getInventory().remove(seed, 1);
                    if (player.getSkills().get(Skill.FARMING).addXp(3)) {
                        player.addMaxEnergy(10);
                    }
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


    public boolean plantCrop(int tileX, int tileY, CropType type) {
        Tile tile = getTileAt(tileX, tileY);
        if (tile == null || tile.getContent() != null || type == null) return false;

        TiledMapTileLayer grassLayer = (TiledMapTileLayer) map.getLayers().get("Grass");
        if (grassLayer == null) return false;

        TiledMapTileLayer.Cell cell = grassLayer.getCell(tileX, tileY);
        if (cell == null || cell.getTile() == null) return false;

        int tileId = cell.getTile().getId();

        if (tileId != TILLED_TILE_ID) return false; // ⛔ Not dirt tile

        tile.setContent(new Crop(type));
        return true;
    }



    private boolean harvestNearestCrop(float mx, float my) {
        ItemType held = player.getInventoryRenderer().getSelectedType();
        if (!(held instanceof ToolType) || held != ToolType.SCYTHE) {
            return false;
        }

        Crop      nearestCrop = null;
        Tile      cropTile    = null;
        float     bestD2      = HARVEST_RANGE * HARVEST_RANGE;

        // quick search window (5×5 tiles around cursor)
        int cx = (int)(mx / TILE_SIZE);
        int cy = (int)(my / TILE_SIZE);

        for (int ty = cy - 2; ty <= cy + 2; ty++) {
            for (int tx = cx - 2; tx <= cx + 2; tx++) {
                Tile tile = getTileAt(tx, ty);
                if (tile == null || !(tile.getContent() instanceof Crop)) continue;

                Crop crop = (Crop) tile.getContent();
                if (!crop.isFullyGrown() || crop.isHarvested()) continue;

                // centre‑point of the sprite we drew
                float cxWorld = tile.getWorldX() + TILE_SIZE * 0.5f;
                float cyWorld = tile.getWorldY() + TILE_SIZE * 0.5f;

                float dx = mx - cxWorld;
                float dy = my - cyWorld;
                float d2 = dx*dx + dy*dy;           // squared distance

                if (d2 < bestD2) {                  // keep the closest
                    bestD2       = d2;
                    nearestCrop  = crop;
                    cropTile     = tile;
                }
            }
        }

        if (nearestCrop != null) {
            if (!player.hasEnergy(EC_HARVEST)) return false;
            player.consumeEnergy(EC_HARVEST);
            nearestCrop.harvest();
            player.getInventory().add(nearestCrop.getCropType(), 1);
            cropTile.setContent(null);
            if (player.getSkills().get(Skill.FARMING).addXp(5)) {
                player.addMaxEnergy(10);
            }
            return true;
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
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("Collision");
        if (collisionLayer == null) return true;

        float width = player.getWidth();
        float height = player.getHeight();

        int[][] corners = {
            { (int) (newX / TILE_SIZE), (int) (newY / TILE_SIZE) },
            { (int) ((newX + width - 1) / TILE_SIZE), (int) (newY / TILE_SIZE) },
            { (int) (newX / TILE_SIZE), (int) ((newY + height - 1) / TILE_SIZE) },
            { (int) ((newX + width - 1) / TILE_SIZE), (int) ((newY + height - 1) / TILE_SIZE) }
        };

        for (int[] corner : corners) {
            int tileX = corner[0];
            int tileY = corner[1];
            if (tileX < 0 || tileY < 0 || tileX >= collisionLayer.getWidth() || tileY >= collisionLayer.getHeight()) return false;
            TiledMapTileLayer.Cell cell = collisionLayer.getCell(tileX, tileY);
            if (cell != null && cell.getTile() != null) return false;
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
        currentMapPath  = targetMapPath;
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
}
