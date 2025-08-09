//package io.github.StardewValley.models.Artisan;
//
//import io.github.StardewValley.models.Item;
//
//public class ArtisanProduct extends Item {
//    private final ArtisanProductType productType;
//    private int energy;
//
//    public ArtisanProduct(ArtisanProductType type) {
//        super(type.getName(), ItemType.ARTISAN_PRODUCT, type.getSellPrice());
//        this.productType = type;
//        this.energy = type.getEnergy();
//    }
//
//    public ArtisanProduct(ArtisanProductType type, int customPrice, int customEnergy) {
//        super(type.getName(), ItemType.ARTISAN_PRODUCT, type.getSellPrice());
//        this.setPrice(customPrice);
//        this.productType = type;
//        this.energy = customEnergy;
//    }
//
//    public int getEnergy() {
//        return energy;
//    }
//
//    public String getDescription() {
//        return productType.getDescription();
//    }
//
//    public ArtisanProductType getProductType() {
//        return productType;
//    }
//}
//
