//package io.github.StardewValley.models.farming;
//
//import io.github.StardewValley.models.Item;
//
//public class ForagingMineral extends Item {
//    private final ForagingMineralTypes type;
//
//    public ForagingMineral(ForagingMineralTypes type) {
//        super(ItemType.MINERAL);
//        this.type = type;
//    }
//
//    public String getName() {
//        return type.getDisplayName();
//    }
//
//    public int getSellPrice() {
//        return type.getSellPrice();
//    }
//
//    public ForagingMineralTypes getMineralType() {
//        return type;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) return true;
//        if (!(obj instanceof ForagingMineral other)) return false;
//        return this.getMineralType() == other.getMineralType();
//    }
//
//    @Override
//    public int hashCode() {
//        return type.hashCode();
//    }
//
//}
