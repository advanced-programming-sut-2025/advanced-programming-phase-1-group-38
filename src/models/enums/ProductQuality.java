package models.enums;

public enum ProductQuality {
    NORMAL(1),
    SILVER(1.25),
    GOLD(1.5),
    IRIDIUM(2);

    private final double priceCoefficient;

    ProductQuality(double priceCoefficient) {
        this.priceCoefficient = priceCoefficient;
    }

    public double getPriceCoefficient() {
        return priceCoefficient;
    }
}
