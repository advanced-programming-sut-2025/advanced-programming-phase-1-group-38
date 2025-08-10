//package io.github.StardewValley.models.recipe;
//
//import io.github.StardewValley.models.Item;
//
//import java.util.List;
//
//public abstract class Recipe extends Item {
//    private final String name;
//    private final String description;
//    private final List<Ingredient> ingredients;
//    private final String source;
//    private final int sellPrice;
//
//    public Recipe(String name,
//                  String description,
//                  List<Ingredient> ingredients,
//                  String source,
//                  int sellPrice) {
//        super(ItemType.RECIPE);
//        this.name = name;
//        this.description = description;
//        this.ingredients = ingredients;
//        this.source = source;
//        this.sellPrice = sellPrice;
//    }
//
//    public String getName()            { return name; }
//    public String getDescription()     { return description; }
//    public List<Ingredient> getIngredients() { return ingredients; }
//    public String getSource()          { return source; }
//    public int getSellPrice()          { return sellPrice; }
//
//    public abstract String getRecipeType();
//}
