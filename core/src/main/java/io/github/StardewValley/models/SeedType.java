package io.github.StardewValley.models;

public enum SeedType implements ItemType {
    CORN_SEED("Corn Seed", CropType.CORN, "seeds/corn.png"),
    CARROT_SEED("Carrot Seed", CropType.CARROT, "seeds/carrot.png");

    private final String id;
    private final CropType product;
    private final String icon;

    SeedType(String id, CropType product, String icon){
        this.id=id; this.product=product; this.icon=icon;
    }
    @Override public String id()          { return id; }
    @Override public String iconPath()    { return icon; }
    @Override public int    maxStack()    { return 0; }
    public CropType product()              { return product; }
}
