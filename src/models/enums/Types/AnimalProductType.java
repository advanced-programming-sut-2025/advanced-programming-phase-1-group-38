package models.enums.Types;

public enum AnimalProductType {
    CHICKEN_EGG(50, false, null),
    LARGE_CHICKEN_EGG(95, false, null),
    DUCK_EGG(125, false, null),
    DUCK_FEATHER(250, false, null),
    RABBIT_WOOL(200, false, null),
    RABBIT_FOOT(350, false, null),
    DINOSAUR_EGG(350, false, null),

    COW_MILK(125, true, "Milk Pail"),
    LARGE_COW_MILK(190, true, "Milk Pail"),
    GOAT_MILK(225, true, "Milk Pail"),
    LARGE_GOAT_MILK(350, true, "Milk Pail"),
    WOOL(150, true, "Shear"),
    TRUFFLE(625, false, null);

    private final int basePrice;
    private final boolean requiresTool;
    private final String requiredToolName;

    AnimalProductType(int basePrice, boolean requiresTool, String requiredToolName) {
        this.basePrice = basePrice;
        this.requiresTool = requiresTool;
        this.requiredToolName = requiredToolName;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public boolean requiresTool() {
        return requiresTool;
    }

    public String getRequiredToolName() {
        return requiredToolName;
    }
}