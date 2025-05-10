package models.farming;

import models.Item;
import models.enums.Types.ItemType;

public class ForagingMineral extends Item {
    private final ForagingMineralTypes type;

    public ForagingMineral(ForagingMineralTypes type) {
        super(ItemType.MINERAL);
        this.type = type;
    }

    public String getName() {
        return type.getDisplayName();
    }

    public int getSellPrice() {
        return type.getSellPrice();
    }

    public ForagingMineralTypes getMineralType() {
        return type;
    }
}
