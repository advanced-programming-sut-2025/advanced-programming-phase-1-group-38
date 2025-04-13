package models.enums;

public enum TrashCanQuality {
    INITIAL(0, 0),
    COPPER(0, 15),
    IRON(0, 30),
    GOLD(0, 45),
    IRIDIUM(0, 60)
    ;
    private final int energyCost;
    private final int refundPercent;

    TrashCanQuality(int energyCost, int refundPercent) {
        this.energyCost = energyCost;
        this.refundPercent = refundPercent;
    }
    public int getEnergyCost() {
        return energyCost;
    }
    public int getRefundPercent() {
        return refundPercent;
    }

}