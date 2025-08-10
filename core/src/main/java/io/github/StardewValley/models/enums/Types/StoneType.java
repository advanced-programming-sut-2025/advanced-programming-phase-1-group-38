package io.github.StardewValley.models.enums.Types;

public enum StoneType implements MaterialTypes {
    REGULAR_STONE;

    @Override
    public boolean isTool() {
        return false;
    }

    @Override
    public String getName() {
        return "Stone";
    }
}
