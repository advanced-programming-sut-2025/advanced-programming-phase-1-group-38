package io.github.StardewValley.models.Artisan;

import io.github.StardewValley.models.enums.Types.MaterialTypes;

public enum ArtisanProductType implements MaterialTypes {
    HONEY("Honey", "Sweet syrup produced by bees.", 350, 75, true),
    CHEESE("Cheese", "Aged dairy product.", 230, 100, true),
    LARGE_CHEESE("Cheese", "Aged dairy product.", 345, 100, true),
    GOAT_CHEESE("Goat Cheese", "Soft cheese made from goat's milk.", 400, 100, true),
    LARGE_GOAT_CHEESE("Goat Cheese", "Soft cheese made from goat's milk.", 600, 100, true),
    BEER("Beer", "Drink in moderation.", 200, 50, true),
    VINEGAR("Vinegar", "An aged fermented liquid used in many cooking recipes.", 100, 13, true),
    COFFEE("Coffee", "It smells delicious. This is sure to give you a boost.", 150, 75, true),
    JUICE("Juice", "A sweet, nutritious beverage.", -1, -1, true),
    MEAD("Mead", "A fermented beverage made from honey. Drink in moderation.", 300, 100, true),
    PALE_ALE("Pale Ale", "Drink in moderation.", 300, 50, true),
    WINE("Wine", "Drink in moderation.", -1, -1, true),
    DRIED_MUSHROOMS("Dried Mushrooms", "A package of gourmet mushrooms.", -1, 50, true),
    DRIED_FRUIT("Dried Fruit", "Chewy pieces of dried fruit.", -1, 75, true),
    RAISINS("Raisins", "It's said to be the Junimos' favorite food.", 600, 125, true),
    COAL("Coal", "Turns 10 pieces of wood into one piece of coal.", 50, 0, false),
    GRAPE_WINE("Grap Wine", "Drink in moderation.", 300, 100, true),
    PICKLES("Pickles", "Drink in moderation.", 300, 50, true),
    ;

    private final String name;
    private final String description;
    private final int sellPrice;
    private final int energy;
    private boolean edible;

    ArtisanProductType(String name, String description, int sellPrice, int energy, boolean edible) {
        this.name = name;
        this.description = description;
        this.sellPrice = sellPrice;
        this.energy = energy;
        this.edible = edible;
    }

    @Override
    public boolean isTool() {
        return false;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getSellPrice() { return sellPrice; }
    public int getEnergy() { return energy; }
    public boolean hasDynamicPricing() {
        return sellPrice == -1;
    }
    public boolean hasDynamicEnergy() {
        return energy == -1;
    }
    public boolean isEdible() { return edible; }

    public static ArtisanProductType fromName(String name) {
        for (ArtisanProductType type : values()) {
            if (type.name.equalsIgnoreCase(name)) return type;
        }
        return null;
    }
}
