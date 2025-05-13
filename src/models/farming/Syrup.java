package models.farming;

import models.Item;
import models.enums.Types.ItemType;

public class Syrup extends Item {
    private final SyrupType type;

    public Syrup(SyrupType type) {
        super(type.getName(), ItemType.SYRUP, type.getBaseSellPrice());
        this.type = type;
    }

    public SyrupType getSyrupType() {
        return type;
    }

    public boolean isEdible() {
        return type.isEdible();
    }

    public int getEnergy() {
        return type.getEnergy();
    }

    @Override
    public String toString() {
        return type.getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Syrup other)) return false;
        return this.type == other.type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
