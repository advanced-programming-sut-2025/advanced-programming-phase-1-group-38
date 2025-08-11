package io.github.StardewValley.views;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public interface PlayerWorldSlice {
    TiledMap getMap();                           // base TMX
    void renderEntities(SpriteBatch batch, boolean drawPlayer);      // draws crops, items, NPCs, etc. at map-local coords
    OrthogonalTiledMapRenderer getRenderer();    // reuse one renderer per map
    int getMapWidthTiles();
    int getMapHeightTiles();
    String getMapId();
}

