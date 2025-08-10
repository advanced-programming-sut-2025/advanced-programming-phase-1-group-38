package io.github.StardewValley.models;

import java.util.ArrayList;
import java.util.List;

public final class ShopCatalog {
    private ShopCatalog() {}

    public static List<ShopProduct> basicGeneralStore() {
        List<ShopProduct> list = new ArrayList<>();
        // Seeds
        list.add(new ShopProduct(SeedType.CARROT_SEED, 20, -1));
        list.add(new ShopProduct(SeedType.CORN_SEED,   40, -1));
        // Tools (if you allow rebuy)
        list.add(new ShopProduct(ToolType.SCYTHE, 150, 2));
        list.add(new ShopProduct(ToolType.PICKAXE, 200, 1));
        // Food
//        list.add(new ShopProduct(FoodType.CARROT_SOUP, 60, 5));
        // Materials (if you added MaterialType implements ItemType)
        // list.add(new ShopProduct(MaterialType.Wood, 5, -1));
        return list;
    }
}