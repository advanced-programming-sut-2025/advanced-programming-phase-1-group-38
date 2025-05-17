package models.recipe;

import java.util.List;

public class CookingRecipe extends Recipe {
    private final int energy;
    private final String buff;
    private final int buffDuration;

    public CookingRecipe(String name,
                         String description,
                         List<Ingredient> ingredients,
                         String source,
                         int sellPrice,
                         int energy,
                         int buffDuration,
                         String buff) {
        super(name, description, ingredients, source, sellPrice);
        this.energy = energy;
        this.buffDuration = buffDuration;
        this.buff = buff;
    }

    @Override
    public String getRecipeType() {
        return "Cooking";
    }

    public int getEnergy() {
        return energy;
    }
    public String getBuff() {
        return buff;
    }
    public int getBuffDuration() {    // اضافه می‌کنیم
        return buffDuration;
    }
}
