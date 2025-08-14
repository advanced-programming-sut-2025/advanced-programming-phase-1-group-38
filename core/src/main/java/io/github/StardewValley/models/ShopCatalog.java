package io.github.StardewValley.models;

public final class ShopCatalog {
    public static java.util.List<ShopProduct> productsFor(Shop liveShop) {
        java.util.ArrayList<ShopProduct> out = new java.util.ArrayList<>();
        for (io.github.StardewValley.models.enums.Shop.ShopEntry e : liveShop.getEntries()) {
            out.add(new ShopProduct(liveShop, e));
        }
        return out;
    }
}
