package io.github.StardewValley.models.enums.Types;

public enum ShopType {
    BLACKSMITH("Clint", 9, 16),
    JOJA_MART("Morris", 9, 23),
    PIERRE_STORE("Pierre", 9, 17),
    CARPENTER_SHOP("Robin", 9, 20),
    FISH_SHOP("Willy", 9, 17),
    MARNIE_RANCH("Marnie", 9, 16),
    THE_STAR_DROP_SALOON("Gus", 12, 24);

    private final String owner;
    private final int openHour;
    private final int closeHour;

    ShopType(String owner, int openHour, int closeHour) {
        this.owner = owner;
        this.openHour = openHour;
        this.closeHour = closeHour;
    }

    public String getOwner() {
        return owner;
    }

    public int getOpenHour() {
        return openHour;
    }

    public int getCloseHour() {
        return closeHour;
    }

    public boolean isOpen(int hour) {
        return hour >= openHour && hour < closeHour;
    }
}

