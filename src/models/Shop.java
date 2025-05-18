package models;

import models.enums.Types.ShopType;
import models.enums.Shop.ShopEntry;
import models.enums.Shop.Blacksmith;
import models.enums.Shop.CarpentersShop;
import models.enums.Shop.FishShop;
import models.enums.Shop.JojaMart;
import models.enums.Shop.MarniesRanch;
import models.enums.Shop.PierresGeneralStore;
import models.enums.Shop.TheStardropSaloon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Shop {
    private final ShopType shopType;
    private final ArrayList<ShopEntry> entries;
    private final Map<String, Integer> dailyLimits;
    private HashMap<String, Integer> availableStocks;

    public Shop(ShopType shopType) {
        this.shopType = shopType;
        this.entries = new ArrayList<>();
        this.dailyLimits = new HashMap<>();
        initializeStock();
        this.availableStocks = new HashMap<>(dailyLimits);
    }

    private void initializeStock() {
        ShopEntry[] catalog;
        catalog = switch (shopType) {
            case BLACKSMITH -> Blacksmith.values();
            case CARPENTER_SHOP -> CarpentersShop.values();
            case FISH_SHOP -> FishShop.values();
            case JOJA_MART -> JojaMart.values();
            case MARNIE_RANCH -> MarniesRanch.values();
            case PIERRE_STORE -> PierresGeneralStore.values();
            case THE_STAR_DROP_SALOON -> TheStardropSaloon.values();
            default -> new ShopEntry[0];
        };
        for (ShopEntry e : catalog) {
            entries.add(e);
            dailyLimits.put(e.getDisplayName(), e.getDailyLimit());
        }
    }

    public HashMap<String, Integer> getStock() {
        return this.availableStocks;
    }

    public ShopType getShopType() {
        return shopType;
    }

    public ArrayList<ShopEntry> getEntries() {
        return entries;
    }

    public boolean hasEntry(String name) {
        return dailyLimits.containsKey(name);
    }

    public ShopEntry findEntry(String name) {
        for (ShopEntry e : entries) {
            if (e.getDisplayName().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }

    public int getDailyLimit(String name) {
        return dailyLimits.getOrDefault(name, 0);
    }

    public int getAvailableStock(String name) {
        return availableStocks.getOrDefault(name, 0);
    }

    public boolean purchase(String itemName, int quantity) {
        int available = getAvailableStock(itemName);
        if (available >= quantity) {
            availableStocks.put(itemName, available - quantity);
            return true;
        }
        return false;
    }

    public void resetStock() {
        this.availableStocks = new HashMap<>(dailyLimits);
    }
}
