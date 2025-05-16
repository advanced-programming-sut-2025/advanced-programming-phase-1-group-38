package models.Artisan;

import models.enums.Types.ItemType;

import java.util.List;

public class ArtisanRecipe {
    private final String name;
    private final String description;
    private final int energy;
    private final String processingTime;
    private final List<RecipeOption> options;
    private final ItemType resultItemType;

    public ArtisanRecipe(String name, String description, int energy, String processingTime,
                         List<RecipeOption> options, ItemType resultItemType) {
        this.name = name;
        this.description = description;
        this.energy = energy;
        this.processingTime = processingTime;
        this.options = options;
        this.resultItemType = resultItemType;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getEnergy() { return energy; }
    public String getProcessingTime() { return processingTime; }
    public List<RecipeOption> getOptions() { return options; }

    public ItemType getProductType() {
        return resultItemType;
    }
}
