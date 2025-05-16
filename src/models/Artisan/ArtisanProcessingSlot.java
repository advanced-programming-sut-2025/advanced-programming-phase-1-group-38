package models.Artisan;

import models.Time;

public class ArtisanProcessingSlot {
    private final ArtisanRecipe recipe;
    private final int startHour;
    private final int requiredHours;

    public ArtisanProcessingSlot(ArtisanRecipe recipe, Time time) {
        this.recipe = recipe;
        this.startHour = (int) time.getHour(); // Get current hour from Time
        this.requiredHours = parseDurationToHours(recipe.getProcessingTime()); // e.g., "4 Days" → 96
    }

    public boolean isReady(Time currentTime) {
        int currentHour = (int) currentTime.getHour();
        return currentHour - startHour >= requiredHours;
    }

    public ArtisanProduct collectProduct() {
        return new ArtisanProduct(recipe.getProductType());
    }

    public int getHoursRemaining(Time currentTime) {
        int currentHour = (int) currentTime.getHour();
        return Math.max(0, (startHour + requiredHours) - currentHour);
    }

    public ArtisanRecipe getRecipe() {
        return recipe;
    }

    private int parseDurationToHours(String input) {
        input = input.trim().toLowerCase();
        if (input.contains("day")) {
            return Integer.parseInt(input.split(" ")[0]) * 24;
        } else if (input.contains("hour")) {
            return Integer.parseInt(input.split(" ")[0]);
        }
        return 24;
    }
}
