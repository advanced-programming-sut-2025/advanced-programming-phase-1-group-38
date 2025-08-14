package io.github.StardewValley.models;

import io.github.StardewValley.models.enums.Shop.ShopEntry;
import io.github.StardewValley.models.enums.Types.ShopType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Shop {
    private final ShopType shopType;
    private final ArrayList<ShopEntry> entries;

    // CHANGED: key by ShopEntry instead of String
    private final Map<ShopEntry, Integer> dailyLimits;
    private Map<ShopEntry, Integer> availableStocks;

    public Shop(ShopType shopType) {
        this.shopType = shopType;
        this.entries = new ArrayList<>();
        this.dailyLimits = new HashMap<>();
        initializeStock();
        this.availableStocks = new HashMap<>(dailyLimits);
    }

    private void initializeStock() {
        ShopEntry[] catalog = switch (shopType) {
            case BLACKSMITH -> io.github.StardewValley.models.enums.Shop.Blacksmith.values();
            // case CARPENTER_SHOP -> CarpentersShop.values();
            // case FISH_SHOP -> FishShop.values();
            // case THE_STAR_DROP_SALOON -> TheStardropSaloon.values();
            default -> new ShopEntry[0];
        };
        for (ShopEntry e : catalog) {
            entries.add(e);
            dailyLimits.put(e, e.getDailyLimit());
        }
    }

    public Map<ShopEntry, Integer> getStock() { return availableStocks; }
    public ShopType getShopType() { return shopType; }
    public ArrayList<ShopEntry> getEntries() { return entries; }
    public boolean hasEntry(ShopEntry e) { return dailyLimits.containsKey(e); }

    // CHANGED: API takes ShopEntry
    public int getDailyLimit(ShopEntry e) { return dailyLimits.getOrDefault(e, 0); }
    public int getAvailableStock(ShopEntry e) { return availableStocks.getOrDefault(e, 0); }

    public boolean purchase(ShopEntry e, int quantity) {
        int available = getAvailableStock(e);
        if (available >= quantity) {
            availableStocks.put(e, available - quantity);
            return true;
        }
        return false;
    }

    public void resetStock() {
        this.availableStocks = new HashMap<>(dailyLimits);
    }

    /* OPTIONAL: keep string helpers if other code still uses names */
    public boolean hasEntryByName(String name) {
        return entries.stream().anyMatch(e -> e.getDisplayName().equalsIgnoreCase(name));
    }
    public ShopEntry findEntryByName(String name) {
        for (ShopEntry e : entries) if (e.getDisplayName().equalsIgnoreCase(name)) return e;
        return null;
    }
}
