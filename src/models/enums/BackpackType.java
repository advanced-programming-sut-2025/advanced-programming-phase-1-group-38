package models.enums;

public enum BackpackType {
    INITIAL(12, false),
    LARGE(24, false),
    DELUXE(null, true);

    private final Integer capacity;
    private final boolean isUnlimited;

    BackpackType(Integer capacity, boolean isUnlimited) {
        this.capacity = capacity;
        this.isUnlimited = isUnlimited;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public boolean isUnlimited() {
        return isUnlimited;
    }
}