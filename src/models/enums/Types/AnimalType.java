package models.enums.Types;

import java.util.Arrays;
import java.util.List;

public enum AnimalType {
    CHICKEN(Arrays.asList(AnimalProductType.CHICKEN_EGG, AnimalProductType.LARGE_CHICKEN_EGG), true, 800),
    DUCK(Arrays.asList(AnimalProductType.DUCK_EGG, AnimalProductType.DUCK_FEATHER), true, 1200),
    RABBIT(Arrays.asList(AnimalProductType.RABBIT_WOOL, AnimalProductType.RABBIT_FOOT), true, 8000),
    DINOSAUR(Arrays.asList(AnimalProductType.DINOSAUR_EGG), true, 14000),
    COW(Arrays.asList(AnimalProductType.COW_MILK, AnimalProductType.LARGE_COW_MILK), false, 1500),
    GOAT(Arrays.asList(AnimalProductType.GOAT_MILK, AnimalProductType.LARGE_GOAT_MILK), false, 4000),
    SHEEP(Arrays.asList(AnimalProductType.WOOL), false, 8000),
    PIG(Arrays.asList(AnimalProductType.TRUFFLE), false, 16000);

    private final List<AnimalProductType> animalProducts;
    private final boolean livesInCage;
    private final int animalPrice;

    AnimalType(List<AnimalProductType> animalProducts, boolean livesInCage, int animalPrice) {
        this.animalProducts = animalProducts;
        this.livesInCage = livesInCage;
        this.animalPrice = animalPrice;
    }

    public List<AnimalProductType> getAnimalProducts() {
        return animalProducts;
    }

    public boolean isLivesInCage() {
        return livesInCage;
    }

    public int getAnimalPrice() {
        return animalPrice;
    }
}
