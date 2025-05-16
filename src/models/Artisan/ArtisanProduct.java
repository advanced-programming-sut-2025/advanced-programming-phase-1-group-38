package models.Artisan;

import models.Item;
import models.enums.Types.ItemType;

public class ArtisanProduct extends Item {
    public ArtisanProduct(String name, ItemType type, int basePrice) {
        super(name, type, basePrice);
    }
}
