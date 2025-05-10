package models.enums.Types;

public enum FishingPoleType {
    TRAINING_POLE(8),
    BAMBOO_POLE(8),
    FIBERGLASS_POLE(6),
    IRIDIUM_POLE(4)
    ;
    private final int energyCost;
    FishingPoleType(int energyCost) {
        this.energyCost = energyCost;
    }
    public int getEnergyCost() {
        return energyCost;
    }
}
