package models.recipe;

import models.Item;
import models.enums.Types.ItemType;

import java.util.List;

public abstract class Recipe extends Item {
    protected final String name;
    protected final String description;
    protected final List<Ingredient> ingredients;
    protected final String source;
    protected final int sellPrice;

    public Recipe(String name, String description, List<Ingredient> ingredients, String source, int sellPrice) {
        super(ItemType.RECIPE);
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.source = source;
        this.sellPrice = sellPrice;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<Ingredient> getIngredients() { return ingredients; }
    public String getSource() { return source; }
    public int getSellPrice() { return sellPrice; }

    public abstract String getRecipeType();
}
