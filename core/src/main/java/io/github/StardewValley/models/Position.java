package io.github.StardewValley.models;

import io.github.StardewValley.models.enums.Direction;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Position shift(Direction direction) {
        return switch (direction) {
            case UP -> new Position(x, y - 1);
            case DOWN -> new Position(x, y + 1);
            case LEFT -> new Position(x - 1, y);
            case RIGHT -> new Position(x + 1, y);
            case UP_RIGHT -> new Position(x + 1, y - 1);
            case UP_LEFT -> new Position(x - 1, y - 1);
            case DOWN_RIGHT -> new Position(x + 1, y + 1);
            case DOWN_LEFT -> new Position(x - 1, y + 1);
        };
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
