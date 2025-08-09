package io.github.StardewValley.models.enums.Types;


public enum FoodType implements MaterialTypes {
    FriedEgg("Fried egg", 50),
    Salad("Salad", 113),
    Beer("Beer",100),
    Spaghetti("Spaghetti", 75),
    Pizza("Pizza", 150),
    Coffee("Coffee",100),
    Olmelet("Olmelet", 100),
    PumpkinPie( "Pumpkin", 100),
    Tortilla( "Tortilla", 100),
    MakiRoll("MakiRoll", 100),
    TripleShotEspresso( "Triple Shot Espresso", 100),
    Cookie( "Cookie", 100),
    HashBrowns( "hashBrowns", 100),
    Pancakes( "pancakes", 100),
    FruitSalad( "fruitSalad", 100),
    RedPlate( "redPlate", 100),
    Bread( "bread", 100),
    SalmonDinner( "salmon Dinner", 100),
    VegetableMedley( "vegetableMedley", 100),
    FarmersLunch( "farmer's lunch", 100),
    SurvivalBurger( "survival burger", 100),
    DishOfTheSea( "dish of the sea", 100),
    SeaFormPudding( "sea form pudding", 100),
    MinersTreat( "miners treat", 100);

    private final String name;
    private final int energy;

    FoodType(String name, int energy) {
        this.name = name;
        this.energy = energy;

    }

    // Getters
    @Override
    public String getName() { return name; }

    public int getEnergy() { return energy; }

    @Override
    public boolean isTool() {
        return false;
    }

}
