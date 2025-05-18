package models.enums.Types;

public enum ToolType implements MaterialTypes {
    HOE,
    PICKAXE,
    AXE,
    WATERING_CAN,
    FISHING_POLE,
    SCYTHE,
    MILK_PAIL,
    SHEAR,
    BACKPACK,
    TRASH_CAN;

    @Override
    public boolean isTool() {
        return true;
    }

    @Override
    public String getName() {
        return "";
    }
}
