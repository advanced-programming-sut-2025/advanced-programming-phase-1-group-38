package io.github.StardewValley.models;

import io.github.StardewValley.models.enums.Types.TileType;

public class Tile {
    private boolean walkable;
    private Object content;
    private final int worldX;
    private final int worldY;

    public Tile(boolean walkable, int worldX, int worldY) {
        this.walkable = walkable;
        this.content = null;
        this.worldX = worldX;
        this.worldY = worldY;
    }

    public int getWorldX() {
        return worldX;
    }

    public int getWorldY() {
        return worldY;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public boolean hasCrop() {
        return content instanceof Crop;
    }

    public Crop getCrop() {
        return hasCrop() ? (Crop) content : null;
    }

    public void updateDaily() {
        if (hasCrop()) {
            getCrop().updateDaily();
        }
    }
}
