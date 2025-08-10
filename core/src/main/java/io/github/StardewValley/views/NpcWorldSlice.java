package io.github.StardewValley.views;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import io.github.StardewValley.models.GameAssetManager;

public class NpcWorldSlice implements PlayerWorldSlice {
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;
    private final String mapId;

    public NpcWorldSlice(String tmxPath) {
        // load a separate TMX for NPC hub (e.g., "maps/NpcHub.tmx")
        this.mapId = tmxPath;
        this.map = GameAssetManager.getGameAssetManager().loadFreshMap(tmxPath);
        this.renderer = new OrthogonalTiledMapRenderer(map, 1f);
    }

    @Override public TiledMap getMap() { return map; }

    @Override
    public OrthogonalTiledMapRenderer getRenderer() { return renderer; }

    @Override
    public int getMapWidthTiles()  {
        return map.getProperties().get("width", Integer.class);
    }
    @Override
    public int getMapHeightTiles() {
        return map.getProperties().get("height", Integer.class);
    }

    @Override
    public String getMapId() {
        return mapId;
    }

    @Override
    public void renderEntities(SpriteBatch batch, boolean drawPlayer) {
        // draw NPCs, particles, etc. (optional for now)
    }

    public void dispose() {
        renderer.dispose();
        map.dispose();   // dispose the shared NPC map exactly once here
    }
}
