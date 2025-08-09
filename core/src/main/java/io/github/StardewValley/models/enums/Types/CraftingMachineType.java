package io.github.StardewValley.models.enums.Types;

public enum CraftingMachineType implements MaterialTypes {
    CHERRY_BOMB("Cherry Bomb", 50),
    BOMB("Bomb", 50),
    MEGA_BOMB("Mega Bomb", 50),
    SPRINKLER("Sprinkler", null),
    QUALITY_SPRINKLER("Quality Sprinkler", null),
    IRIDIUM_SPRINKLER("Iridium Sprinkler", null),
    CHARCOAL_KILN("Charcoal Kiln", null),
    FURNACE("Furnace", null),
    SCARECROW("Scarecrow", null),
    DELUXE_SCARECROW("Deluxe Scarecrow", null),
    BEE_HOUSE("Bee House", null),
    CHEESE_PRESS("Cheese Press", null),
    KEG("Keg", null),
    LOOM("Loom", null),
    MAYONNAISE_MACHINE("Mayonnaise Machine", null),
    OIL_MAKER("Oil Maker", null),
    PRESERVES_JAR("Preserves Jar", null),
    DEHYDRATOR("Dehydrator", null),
    GRASS_STARTER("Grass Starter", null),
    FISH_SMOKER("Fish Smoker", null),
    MYSTIC_TREE_SEED("Mystic Tree Seed", 100);

    private final String productName;
    private final Integer price;

    CraftingMachineType(String productName, Integer price) {
        this.productName = productName;
        this.price = price;
    }

    @Override
    public boolean isTool() {
        return false;
    }

    @Override
    public String getName() {
        return this.productName;
    }

    public Integer getPrice() {
        return this.price;
    }
}
