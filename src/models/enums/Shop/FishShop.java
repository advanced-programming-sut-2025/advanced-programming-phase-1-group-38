package models.enums.Shop;

import models.enums.CraftingRecipes;
import models.enums.Types.MaterialType;
import models.enums.Types.MaterialTypes;


public enum FishShop implements ShopEntry{

    FishSmokerRecipe(CraftingRecipes.FishSmokerRecipe, "Fish Smoker (Recipe)",
            "A recipe to make Fish Smoker", 10000, -1, 1),
    TroutSoup(MaterialType.TroutSoup,  "Trout Soup",
            "Pretty salty.", 250, -1, 1),
    BambooPole(MaterialType.BambooPole, "Bamboo Pole",
            "Use in the water to catch fish.", 500, -1, 1),
    TrainingRod(MaterialType.TrainingRod, "Training Rod",
            "It's a lot easier to use than other rods, but can only catch basic fish.", 25, -1, 1),
    FiberglassRod(MaterialType.FiberglassRod, "Fiberglass Rod",
            "Use in the water to catch fish.", 1800, 2, 1),
    IridiumRod(MaterialType.IridiumRod, "Iridium Rod",
            "Use in the water to catch fish.", 7500, 4, 1);


    private final MaterialTypes itemType;
    private final String displayName;
    private final String description;
    private final int price;
    private final int fishingSkillRequired;
    private final int dailyLimit;

    FishShop(MaterialTypes itemType, String displayName, String description, int price,
                int fishingSkillRequired, int dailyLimit) {
        this.itemType = itemType;
        this.displayName = displayName;
        this.description = description;
        this.price = price;
        this.fishingSkillRequired = fishingSkillRequired;
        this.dailyLimit = dailyLimit;
    }

    @Override
    public MaterialTypes getItemType() { return itemType; }
    @Override public String getDisplayName() { return displayName; }
    @Override public String getDescription() { return description; }
    @Override public int getPrice() { return price; }
    public int getFishingSkillRequired() { return fishingSkillRequired; }
    @Override public int getDailyLimit() { return dailyLimit; }

    @Override
    public String toString() {
        return this.name() + "\nPrice: " + 
        this.price + "\nDescription: " + this.description + 
        "\n----------------------\n";
    }
}
