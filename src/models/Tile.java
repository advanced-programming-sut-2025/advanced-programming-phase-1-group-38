package models;

import models.enums.Types.TileType;

public class Tile {
    private TileType type;
    private boolean walkable;
    private Object content;

    public Tile(TileType type, boolean walkable) {
        this.type = type;
        this.walkable = walkable;
        this.content = null;
    }

    public TileType getTileType() {
        return type;
    }

    public void setTileType(TileType type) {
        this.type = type;
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
        this.walkable = (content == null);
    }

    public boolean isOccupied() {
        return content != null;
    }

    public void clearContent() {
        this.content = null;
    }
}
