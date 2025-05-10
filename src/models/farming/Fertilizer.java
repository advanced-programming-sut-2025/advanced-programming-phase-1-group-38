package models.farming;

import models.Item;
import models.enums.Types.ItemType;

public class Fertilizer extends Item {
    private FertilizerType fertilizerType;

    public Fertilizer(ItemType type, FertilizerType fertilizerType) {
        super(ItemType.FERTILIZER);
        this.fertilizerType = fertilizerType;
    }
}
