package models.enums;

import models.enums.Types.MaterialType;
import models.enums.Types.MaterialTypes;

import java.util.HashMap;
import java.util.Map;

public enum CookingRecipes implements MaterialTypes {
    FriedEgg("Fried egg", 50, null, false, 0, 35, Map.of(
            MaterialType.Egg, 1
    )),

    BakedFish("Baked Fish", 75, null, false, 0, 100, Map.of(
            MaterialType.Sardine, 1,
            MaterialType.Salmon, 1,
            MaterialType.Wheat, 1
    )),

    Salad("Salad", 113, null, false, 0, 110, Map.of(
            MaterialType.Leek, 1,
            MaterialType.Dandelion, 1
    )),

    Omelet("Omelet", 100, null, false, 0, 125, Map.of(
            MaterialType.Egg, 1,
            MaterialType.Milk, 1
    )),

    PumpkinPie("Pumpkin pie", 225, null, false, 0, 385, Map.of(
            MaterialType.Pumpkin, 1,
            MaterialType.WheatFlour, 1,
            MaterialType.Milk, 1,
            MaterialType.Sugar, 1
    )),

    Spaghetti("Spaghetti", 75, null, false, 0, 120, Map.of(
            MaterialType.WheatFlour, 1,
            MaterialType.Tomato, 1
    )),

    Pizza("Pizza", 150, null, false, 0, 300, Map.of(
            MaterialType.WheatFlour, 1,
            MaterialType.Tomato, 1,
            MaterialType.Cheese, 1
    )),

    Tortilla("Tortilla", 50, null, false, 0, 50, Map.of(
            MaterialType.Corn, 1
    )),

    MakiRoll("Maki Roll", 100, null, false, 0, 220, Map.of(
            MaterialType.AnyFish, 1,
            MaterialType.Rice, 1,
            MaterialType.Fiber, 1
    )),

    TripleShotEspresso("Triple Shot Espresso", 200, null, true, 5, 450, Map.of(
            MaterialType.Coffee, 3
    )),

    Cookie("Cookie", 90, null, false, 0, 140, Map.of(
            MaterialType.WheatFlour, 1,
            MaterialType.Sugar, 1,
            MaterialType.Egg, 1
    )),

    HashBrowns("Hash browns", 90, Skill.FARMING, false, 5, 120, Map.of(
            MaterialType.Potato, 1,
            MaterialType.Oil, 1
    )),

    Pancakes("Pancakes", 90, Skill.FORAGING, false, 11, 80, Map.of(
            MaterialType.WheatFlour, 1,
            MaterialType.Egg, 1
    )),

    FruitSalad("Fruit salad", 263, null, false, 0, 450, Map.of(
            MaterialType.Blueberry, 1,
            MaterialType.Melon, 1,
            MaterialType.Apricot, 1
    )),

    RedPlate("Red plate", 240, null, true, 3, 400, Map.of(
            MaterialType.RedCabbage, 1,
            MaterialType.Radish, 1
    )),

    Bread("Bread", 50, null, false, 0, 60, Map.of(
            MaterialType.WheatFlour, 1
    )),

    SalmonDinner("Salmon dinner", 125, null, false, 0, 300, Map.of(
            MaterialType.Salmon, 1,
            MaterialType.Amaranth, 1,
            MaterialType.Kale, 1
    )),

    VegetableMedley("Vegetable medley", 165, null, false, 0, 120, Map.of(
            MaterialType.Tomato, 1,
            MaterialType.Beet, 1
    )),

    FarmersLunch("Farmer's lunch", 200, Skill.FARMING, false, 5, 150, Map.of(
            MaterialType.Omelet, 1,
            MaterialType.Parsnip, 1
    )),

    SurvivalBurger("Survival burger", 125, Skill.FORAGING, false, 5, 180, Map.of(
            MaterialType.Bread, 1,
            MaterialType.Carrot, 1,
            MaterialType.Eggplant, 1
    )),

    DishOTheSea("Dish O' the Sea", 150, Skill.FISHING, false, 5, 220, Map.of(
            MaterialType.Sardine, 2,
            MaterialType.HashBrowns, 1
    )),

    SeaFoamPudding("SeaFoam Pudding", 175, Skill.FISHING, false, 10, 300, Map.of(
            MaterialType.Flounder, 1,
            MaterialType.MidnightCarp, 1
    )),

    MinersTreat("Miner's treat", 125, Skill.MINING, false, 5, 200, Map.of(
            MaterialType.Carrot, 2,
            MaterialType.Sugar, 1,
            MaterialType.Milk, 1
    ));

    // =====================================
    // Fields & Constructor
    // =====================================
    private final String displayName;
    private final int energy;
    private final Skill skillBuff;
    private final boolean isBuffMaxEnergy;
    private final int effectiveTime;
    private final int sellPrice;
    private final Map<MaterialType, Integer> ingredients;

    CookingRecipes(String displayName, int energy, Skill skillBuff, boolean isBuffMaxEnergy,
                   int effectiveTime, int sellPrice, Map<MaterialType, Integer> ingredients) {
        this.displayName = displayName;
        this.energy = energy;
        this.skillBuff = skillBuff;
        this.isBuffMaxEnergy = isBuffMaxEnergy;
        this.effectiveTime = effectiveTime;
        this.sellPrice = sellPrice;
        this.ingredients = new HashMap<>(ingredients);
    }

    // =====================================
    // Getters
    // =====================================
    public Map<MaterialType, Integer> getIngredients() {
        return new HashMap<>(ingredients);
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getEnergy() {
        return energy;
    }

    public Skill getSkillBuff() {
        return skillBuff;
    }

    public boolean isBuffMaxEnergy() {
        return isBuffMaxEnergy;
    }

    public int getEffectiveTime() {
        return effectiveTime;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    @Override
    public boolean isTool() {
        return false;
    }

    @Override
    public String getName() {
        return displayName;
    }
}