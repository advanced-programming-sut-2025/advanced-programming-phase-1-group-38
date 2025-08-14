package io.github.StardewValley.models;

import io.github.StardewValley.models.enums.Shop.ShopEntry;

public class ShopProduct {
    private final Shop shop;
    private final ShopEntry entry;

    public ShopProduct(Shop shop, ShopEntry entry) {
        this.shop = shop;
        this.entry = entry;
    }

    public ShopEntry getEntry() { return entry; }
    public ItemType getItem() { return entry.getItemType(); }
    public int getPrice() { return entry.getPrice(); }
    public io.github.StardewValley.models.enums.Types.ShopType getShopType() { return shop.getShopType(); }

    public int getStock() { return shop.getAvailableStock(entry); }   // <-- entry, not name
    public boolean isOutOfStock() { return getStock() == 0; }
    public void take(int qty) { shop.purchase(entry, qty); }          // <-- entry, not name
}
