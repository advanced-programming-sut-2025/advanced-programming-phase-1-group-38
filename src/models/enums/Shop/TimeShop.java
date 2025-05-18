package models.enums.Shop;

public enum TimeShop {
    Blacksmith(9,16),
    JojaMart(9,11),
    PierresGeneralStore(9,17),
    CarpentersShop(9,20),
    FishShop(9,17),
    MarniesRanch(9,16),
    TheStardropSaloon(12,24);

    private final int timeStart;
    private final int timeEnd;

    TimeShop(int timeStart, int timeEnd) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public int getTimeStart() {
        return timeStart;
    }
    public int getTimeEnd() {
        return timeEnd;
    }
}
