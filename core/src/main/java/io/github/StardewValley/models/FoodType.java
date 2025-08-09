package io.github.StardewValley.models;

public enum FoodType implements ItemType {
    CARROT_SOUP("Carrot Soup", "food/carrotSoup.png", 0),
    GRILLED_CORN("Carrot Soup", "food/carrotSoup.png", 0);;

    private final String id;
    private final String iconPath;
    private final int maxStack;

    FoodType(String id, String iconPath, int maxStack) {
        this.id = id;
        this.iconPath = iconPath;
        this.maxStack = maxStack;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String iconPath() {
        return iconPath;
    }

    @Override
    public int maxStack() {
        return maxStack;
    }

    @Override
    public boolean stackable() {
        return true; // food is usually stackable
    }
}
