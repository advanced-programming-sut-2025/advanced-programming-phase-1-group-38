package models.enums.Types;

public enum AnimalProductType {
    CHICKEN_EGG(50),
    LARGE_CHICKEN_EGG(95),
    DUCK_EGG(95),
    DUCK_FEATHER(250),
    RABBIT_WOOL(340),
    RABBIT_FOOT(565),
    DINOSAUR_EGG(350),
    COW_MILK(125),
    LARGE_COW_MILK(190),
    GOAT_MILK(225),
    LARGE_GOAT_MILK(345),
    WOOL(340),
    TRUFFLE(625);

    private final int basePrice;

    AnimalProductType(int basePrice) {
        this.basePrice = basePrice;
    }
}
