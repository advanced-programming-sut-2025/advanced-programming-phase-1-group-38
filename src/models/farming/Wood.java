package models.farming;

import models.Item;
import models.enums.Types.ItemType;

public class Wood extends Item {
    public Wood() {
        super(ItemType.WOOD);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Wood;
    }

    @Override
    public int hashCode() {
        return ItemType.WOOD.hashCode();
    }
}
