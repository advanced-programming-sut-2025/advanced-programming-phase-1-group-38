//package io.github.StardewValley.models.farming;
//
//import io.github.StardewValley.models.Item;
//
//public class Fruit extends Item {
//    private final FruitType type;
//
//    public Fruit(FruitType type) {
//        super(type.getName(), ItemType.FRUIT, type.getBaseSellPrice());
//        this.type = type;
//    }
//
//    public FruitType getFruitType() {
//        return type;
//    }
//
//    public boolean isEdible() {
//        return type.isEdible();
//    }
//
//    public int getEnergy() {
//        return type.getEnergy();
//    }
//
//    @Override
//    public String toString() {
//        return type.getName();
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) return true;
//        if (!(obj instanceof Fruit other)) return false;
//        return this.type == other.type;
//    }
//
//    @Override
//    public int hashCode() {
//        return type.hashCode();
//    }
//}
