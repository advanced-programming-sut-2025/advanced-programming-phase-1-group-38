package io.github.StardewValley.models.enums;

public enum Direction {
    UP("up"),
    DOWN("down"),
    RIGHT("right"),
    LEFT("left"),
    UP_RIGHT("up-right"),
    UP_LEFT("up-left"),
    DOWN_RIGHT("down-right"),
    DOWN_LEFT("down-left");

    private final String displayName;

    Direction(String displayName) {
        this.displayName = displayName;
    }

    public static Direction getDirectionByDisplayName(String name) {
        if (name == null) {
            return null;
        }
        return switch (name.toLowerCase()) {
            case "up" -> UP;
            case "down" -> DOWN;
            case "right" -> RIGHT;
            case "left" -> LEFT;
            case "up-right" -> UP_RIGHT;
            case "up-left" -> UP_LEFT;
            case "down-right" -> DOWN_RIGHT;
            case "down-left" -> DOWN_LEFT;
            default -> null;
        };
    }
}
