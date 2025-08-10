//package io.github.StardewValley.models.Animals;
//
//import io.github.StardewValley.models.Item;
//import io.github.StardewValley.models.enums.FishQuality;
//import io.github.StardewValley.models.enums.Types.FishType;
//
//public class Fish extends Item {
//    private final FishType type;
//    private final FishQuality quality;
//
//    public Fish(FishType type, FishQuality quality) {
//        super(type.getName(), ItemType.FISH, type.getBasePrice());
//        this.type = type;
//        this.quality = quality;
//        setPrice((int) (type.getBasePrice() * quality.getMultiplier()));
//    }
//
//    public FishType getFishType() {
//        return type;
//    }
//
//    public FishQuality getQuality() {
//        return quality;
//    }
//
//    @Override
//    public String toString() {
//        return type.getName() + " (" + quality.name() + ")";
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Fish other)) return false;
//        return type == other.type && quality == other.quality;
//    }
//
//    @Override
//    public int hashCode() {
//        return 31 * type.hashCode() + quality.hashCode();
//    }
//}
