package io.github.StardewValley.models;

public class Door {
    public final String targetMap;
    public final int spawnX, spawnY;
    public final float x, y, width, height;
    public final boolean fade;

    public Door(float x, float y, float width, float height, String targetMap, int spawnX, int spawnY, boolean fade) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.targetMap = targetMap;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.fade = fade;
    }

    public boolean isPlayerInside(float px, float py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }
}
