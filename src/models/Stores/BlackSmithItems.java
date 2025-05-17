package models.Stores;

import models.farming.ForagingMineralTypes;

public enum BlackSmithItems {
    COPPER_ORE(ForagingMineralTypes.COPPER, 75, Integer.MAX_VALUE),
    IRON_ORE(ForagingMineralTypes.IRON, 150, Integer.MAX_VALUE),
    COAL(ForagingMineralTypes.COAL, 150, Integer.MAX_VALUE),
    GOLD_ORE(ForagingMineralTypes.GOLD, 400, Integer.MAX_VALUE);

    public final ForagingMineralTypes foragingMineralType;
    public final int price;
    public final int dailyLimit;
    BlackSmithItems(ForagingMineralTypes foragingMineralType, int price, int dailyLimit) {
        this.foragingMineralType = foragingMineralType;
        this.price = price;
        this.dailyLimit = dailyLimit;
    }

    public String getName() {
        return name().toLowerCase().replaceAll("_", " ");
    }
}
