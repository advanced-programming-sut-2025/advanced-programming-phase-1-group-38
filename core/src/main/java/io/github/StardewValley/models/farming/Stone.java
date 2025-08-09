//package io.github.StardewValley.models.farming;
//
//import io.github.StardewValley.models.Item;
//import io.github.StardewValley.models.enums.Types.StoneType;
//
//public class Stone extends Item {
//    private final StoneType type;
//
//    public Stone(StoneType type) {
//        super(ItemType.STONE);
//        this.type = type;
//    }
//
//    public StoneType getStoneType() {
//        return type;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) return true;
//        if (!(obj instanceof Stone other)) return false;
//        return this.getStoneType() == other.getStoneType();
//    }
//
//    @Override
//    public int hashCode() {
//        return type.hashCode();
//    }
//}
