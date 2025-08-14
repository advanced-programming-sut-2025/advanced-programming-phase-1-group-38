package io.github.StardewValley.models.enums.Shop;

import io.github.StardewValley.models.enums.Types.MaterialType;

import java.util.HashMap;
import java.util.Map;

public enum Blacksmith implements ShopEntry{
    // Stock items
    CopperOre(Type.STOCK, "Copper Ore", "A common ore that can be smelted into bars.",
            75, MaterialType.CopperOre, Integer.MAX_VALUE,0, null),
    IronOre(Type.STOCK, "Iron Ore", "A fairly common ore that can be smelted into bars.",
            150, MaterialType.IronOre, Integer.MAX_VALUE, 0, null),
    Coal(Type.STOCK, "Coal", "A combustible rock that is useful for crafting and smelting.",
            150, MaterialType.Coal, Integer.MAX_VALUE, 0, null),
    GoldOre(Type.STOCK, "Gold Ore", "A precious ore that can be smelted into bars.",
            400, MaterialType.GoldOre, 10, 0, null),
    IridiumOre(Type.STOCK, "Iridium Ore", "A rare and valuable ore used for advanced crafts.",
            1000, MaterialType.IridiumOre, Integer.MAX_VALUE, 0, null);

    public enum Type {
        STOCK,
        UPGRADE
    }

    private final Type type;
    private final String displayName;
    private final String description;
    private final Integer price;
    private final MaterialType materialType;
    private final int dailyLimit;
    private final int upgradeCost;
    private final Map<MaterialType, Integer> ingredient;

    Blacksmith(Type type, String displayName, String description, Integer price,
               MaterialType materialType, int dailyLimit, int upgradeCost, Map<MaterialType, Integer> ingredient) {
        this.type = type;
        this.displayName = displayName;
        this.description = description;
        this.price = price;
        this.materialType = materialType;
        this.dailyLimit = dailyLimit;
        this.upgradeCost = upgradeCost;
        this.ingredient = (ingredient != null) ? ingredient : new HashMap<>();
    }

    private static Map<MaterialType, Integer> createIngredients(MaterialType type, int amount) {
        Map<MaterialType, Integer> map = new HashMap<>();
        map.put(type, amount);
        return map;
    }

    public Type getType() { return type; }
    @Override public String getDisplayName() { return displayName; }
    @Override public String getDescription() { return description; }
    @Override public int getPrice() { return price; }
    public MaterialType getMaterialType() { return materialType; }
    @Override public int getDailyLimit() { return dailyLimit; }

    @Override
    public MaterialType getItemType() {
        return this.materialType;
    }

    public int getUpgradeCost() { return upgradeCost; }
    public Map<MaterialType, Integer> getIngredient() { return ingredient; }

    @Override
    public String toString() {
        return this.displayName + "\nPrice: " + this.price +
        "\nDescription: " + this.description +
        "\n---------------------\n";
    }
}
