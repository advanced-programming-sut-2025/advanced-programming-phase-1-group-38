package models.recipe;

import java.util.List;

public class CookingRecipe extends Recipe {
    private final int energy;
    private final String buff;

    public CookingRecipe(String name, String description, List<Ingredient> ingredients, String source,
                         int sellPrice, int energy, String buff) {
        super(name, description, ingredients, source, sellPrice);
        this.energy = energy;
        this.buff = buff;
    }

    @Override
    public String getRecipeType() {
        return "Cooking";
    }

    public int getEnergy() { return energy; }
    public String getBuff() { return buff; }
}
