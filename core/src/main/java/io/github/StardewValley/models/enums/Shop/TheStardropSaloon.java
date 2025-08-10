package io.github.StardewValley.models.enums.Shop;


import io.github.StardewValley.models.enums.CookingRecipes;
import io.github.StardewValley.models.enums.Types.FoodType;
import io.github.StardewValley.models.enums.Types.MaterialTypes;

public enum TheStardropSaloon implements ShopEntry {

    // Permanent Stock
    Beer(FoodType.Beer, null, "Beer", "Drink in moderation.",
            400, Integer.MAX_VALUE),
    Salad(FoodType.Salad, null, "Salad", "A healthy garden salad.",
            220, Integer.MAX_VALUE),
    Bread(FoodType.Bread, null, "Bread", "A crusty baguette.",
            120, Integer.MAX_VALUE),
    Spaghetti(FoodType.Spaghetti, null, "Spaghetti", "An old favorite.",
            240, Integer.MAX_VALUE),
    Pizza(FoodType.Pizza, null, "Pizza", "It's popular for all the right reasons.",
            600, Integer.MAX_VALUE),
    Coffee(FoodType.Coffee, null, "Coffee", "It smells delicious. This is sure to give you a boost.",
            300, Integer.MAX_VALUE),

    // Recipe
    HashbrownsRecipe(CookingRecipes.HashBrowns, CookingRecipes.HashBrowns, "Hashbrowns Recipe",
            "A recipe to make Hashbrowns", 50, 1),
    OmeletRecipe(CookingRecipes.Omelet, CookingRecipes.Omelet, "Omelet Recipe",
            "A recipe to make Omelet", 100, 1),
    PancakesRecipe(CookingRecipes.Pancakes, CookingRecipes.Pancakes, "Pancakes Recipe",
            "A recipe to make Pancakes", 100, 1),
    BreadRecipe(CookingRecipes.Bread, CookingRecipes.Bread, "Bread Recipe",
            "A recipe to make Bread", 100, 1),
    TortillaRecipe(CookingRecipes.Tortilla, CookingRecipes.Tortilla, "Tortilla Recipe",
            "A recipe to make Tortilla", 100, 1),
    PizzaRecipe(CookingRecipes.Pizza, CookingRecipes.Pizza, "Pizza Recipe",
            "A recipe to make Pizza", 150, 1),
    MakiRollRecipe(CookingRecipes.MakiRoll, CookingRecipes.MakiRoll, "Maki Roll Recipe",
            "A recipe to make Maki Roll", 300, 1),
    TripleShotEspressoRecipe(CookingRecipes.TripleShotEspresso, CookingRecipes.TripleShotEspresso, "Triple Shot Espresso Recipe",
            "A recipe to make Triple Shot Espresso", 5000, 1),
    CookieRecipe(CookingRecipes.Cookie, CookingRecipes.Cookie, "Cookie Recipe",
            "A recipe to make Cookie", 300, 1);



    private final MaterialTypes itemType;
    private final CookingRecipes cookingRecipes;
    private final String name;
    private final String description;
    private final int price;
    private final int dailyLimit;

    TheStardropSaloon(MaterialTypes itemType, CookingRecipes cookingRecipes, String name, String description, int price, int dailyLimit) {

        this.itemType = itemType;
        this.cookingRecipes = cookingRecipes;
        this.name = name;
        this.description = description;
        this.price = price;
        this.dailyLimit = dailyLimit;
    }

    @Override
    public MaterialTypes getItemType() { return itemType; }
    public CookingRecipes getCookingRecipes() { return cookingRecipes; }
    @Override public String getDisplayName() { return name; }
    @Override public String getDescription() { return description; }
    @Override public int getPrice() { return price; }
    @Override public int getDailyLimit() { return dailyLimit; }

    @Override
    public String toString() {
        return this.name() + "\nPrice: " + 
        this.price + "\nDescription: " + this.description + 
        "\n----------------------\n";
    }
}
