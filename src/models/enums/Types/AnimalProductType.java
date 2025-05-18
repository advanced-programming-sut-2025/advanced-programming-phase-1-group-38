package models.enums.Types;

import models.Tool;

public enum AnimalProductType implements MaterialTypes {
    CHICKEN_EGG(50, false, null),
    LARGE_CHICKEN_EGG(95, false, null),
    DUCK_EGG(125, false, null),
    DUCK_FEATHER(250, false, null),
    RABBIT_WOOL(200, false, null),
    RABBIT_FOOT(350, false, null),
    DINOSAUR_EGG(350, false, null),

    COW_MILK(125, true, ToolType.MILK_PAIL),
    LARGE_COW_MILK(190, true, ToolType.MILK_PAIL),
    GOAT_MILK(225, true, ToolType.MILK_PAIL),
    LARGE_GOAT_MILK(350, true, ToolType.MILK_PAIL),
    WOOL(150, true, ToolType.SHEAR),
    TRUFFLE(625, false, null);

    private final int basePrice;
    private final boolean requiresTool;
    private final ToolType requiredToolName;

    AnimalProductType(int basePrice, boolean requiresTool, ToolType requiredToolName) {
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

    public ToolType getRequiredToolName() {
        return requiredToolName;
    }

    @Override
    public boolean isTool() {
        return false;
    }

    @Override
    public String getName() {
        return "";
    }
}
