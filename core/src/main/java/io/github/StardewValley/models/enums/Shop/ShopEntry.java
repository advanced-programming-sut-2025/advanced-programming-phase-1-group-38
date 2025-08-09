package io.github.StardewValley.models.enums.Shop;

import io.github.StardewValley.models.enums.Types.MaterialTypes;

public interface ShopEntry {
    String getDisplayName();
    String getDescription();
    int getPrice();
    int getDailyLimit();
    MaterialTypes getItemType();
}
