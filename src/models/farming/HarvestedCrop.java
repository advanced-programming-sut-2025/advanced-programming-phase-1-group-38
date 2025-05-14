package models.farming;

import models.Item;
import models.enums.Types.ItemType;

public class HarvestedCrop extends Item {
    private final CropType type;

    public HarvestedCrop(CropType type) {
        super(type.getName(), ItemType.CROP, type.getBaseSellPrice());
        this.type = type;
    }

    public CropType getCropType() { return type; }

    @Override
    public boolean equals(Object o) {
        return o instanceof HarvestedCrop h && h.type == this.type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
