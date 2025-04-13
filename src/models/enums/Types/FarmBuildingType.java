package models.enums.Types;

public enum FarmBuildingType {
    BARN(7, 4, "Houses 4 barn-dwelling animals.", 350,150, 6000),
    BIG_BARN(7, 4, "Houses 8 barn-dwelling animals. Unlocks goats.", 450, 200, 12000),
    DELUXE_BARN(7, 4, "Houses 12 barn-dwelling animals. Unlocks sheep and pigs.", 550, 300, 25000),
    COOP(6, 3, "Houses 4 coop-dwelling animals.", 300, 100, 4000),
    BIG_COOP(6, 3, "Houses 8 coop-dwelling animals. Unlocks ducks.", 400, 150, 10000),
    DELUXE_COOP(6, 3, "Houses 12 coop-dwelling animals. Unlocks rabbits.", 500, 200, 20000),
    WELL(3, 3, "Provides a place for you to refill your watering can.", 0, 75, 1000),
    SHIPPING_BIN(1, 1, "Items placed in it will be included in the nightly shipment.", 150, 0, 250);

    private final int width;
    private final int length;
    private final String description;
    private final int woodNeed;
    private final int stoneNeed;
    private final int cost;


    FarmBuildingType(int width, int length, String description, int woodNeed, int stoneNeed, int cost) {
        this.width = width;
        this.length = length;
        this.description = description;
        this.woodNeed = woodNeed;
        this.stoneNeed = stoneNeed;
        this.cost = cost;
    }

    public int getWidth() {
        return width;
    }

    public String getDescription() {
        return description;
    }

    public int getLength() {
        return length;
    }

}
