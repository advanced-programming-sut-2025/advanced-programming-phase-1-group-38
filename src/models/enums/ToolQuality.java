package models.enums;

public enum ToolQuality {
    INITIAL(5),
    COPPER(4),
    IRON(3),
    GOLD(2),
    IRIDIUM(1)
    ;
    private final int energyCost;
    ToolQuality(int energyCost) {
        this.energyCost = energyCost;
    }
    public int getEnergyCost() {
        return energyCost;
    }

}
