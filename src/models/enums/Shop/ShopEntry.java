package models.enums.Shop;

import models.enums.Types.MaterialTypes;

public interface ShopEntry {
    String getDisplayName();
    String getDescription();
    int getPrice();
    int getDailyLimit();
    MaterialTypes getItemType();
}
