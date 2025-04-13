package models.inventory;

import models.enums.Types.BackpackType;

public class Backpack extends Inventory {
    private BackpackType type;
    public Backpack(BackpackType type) {
        super(type.getCapacity(), type.isUnlimited());
        this.type = type;
    }
    public BackpackType getType() {
        return this.type;
    }
}
