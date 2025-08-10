//package io.github.StardewValley.models.farming;
//
//import io.github.StardewValley.models.CropType;
//import io.github.StardewValley.models.Item;
//
//public class Seed extends Item {
//    private final SeedType type;
//
//    public Seed(SeedType type) {
//        super(type.getName(), ItemType.SEED, 0); // seed price = 0 or handled elsewhere
//        this.type = type;
//    }
//
//    public boolean isCropSeed() {
//        return type.isCrop();
//    }
//
//    public boolean isTreeSeed() {
//        return !type.isCrop();
//    }
//
//    public CropType getCropType() {
//        return isCropSeed() ? ((CropSeedType) type).getCropType() : null;
//    }
//
//    public TreeType getTreeType() {
//        return isTreeSeed() ? ((TreeSeedType) type).getTreeType() : null;
//    }
//
//    public SeedType getSeedType() {
//        return type;
//    }
//
//    public String getName() {
//        return type.getName();
//    }
//
//    @Override
//    public String toString() {
//        return type.getName();
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        return obj instanceof Seed s && s.type == this.type;
//    }
//
//    @Override
//    public int hashCode() {
//        return type.hashCode();
//    }
//}
