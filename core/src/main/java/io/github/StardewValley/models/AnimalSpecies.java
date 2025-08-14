package io.github.StardewValley.models;

public enum AnimalSpecies {
    CHICKEN("Chicken","wheat","egg", 5f, 150, 16f, 16f),
    COW    ("Cow",    "corn", "milk",10f, 600, 28f, 21f); // <- larger cow

    public final String display, feedItemId, productItemId;
    public final float intervalHours;
    public final int sellPrice;
    public final float drawW, drawH;

    AnimalSpecies(String display, String feedItemId, String productItemId,
                  float intervalHours, int sellPrice, float drawW, float drawH) {
        this.display = display;
        this.feedItemId = feedItemId;
        this.productItemId = productItemId;
        this.intervalHours = intervalHours;
        this.sellPrice = sellPrice;
        this.drawW = drawW;
        this.drawH = drawH;
    }
}
