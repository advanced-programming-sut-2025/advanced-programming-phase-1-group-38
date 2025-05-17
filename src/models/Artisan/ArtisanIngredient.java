package models.Artisan;

import models.Animals.AnimalProduct;
import models.Item;
import models.enums.Types.AnimalProductType;
import models.enums.Types.ItemType;
import models.farming.Crop;
import models.farming.CropType;
import models.farming.Fruit;
import models.farming.FruitType;

import java.util.List;
import java.util.Objects;

public class ArtisanIngredient {
    private final List<ItemType> validTypes;
    private final Object specificType;
    private final Object excludedType;
    private final int quantity;
    private final int price;

    public ArtisanIngredient(List<ItemType> validTypes, Object specificType, Object excludedType, int quantity, int price) {
        this.validTypes = validTypes;
        this.specificType = specificType;
        this.excludedType = excludedType;
        this.quantity = quantity;
        this.price = price;
    }

    public boolean matches(List<Item> items) {
        int matched = 0;
        for (Item item : items) {
            if (matchesType(item)) {
                matched++;
                if (matched >= quantity) return true;
            }
        }
        return false;
    }

    private boolean matchesType(Item item) {
        if (excludedType != null) {
            if (excludedType instanceof FruitType et && item instanceof Fruit f && f.getFruitType() == et) return false;
            if (excludedType instanceof CropType et && item instanceof Crop c && c.getCropType() == et) return false;
            if (excludedType instanceof AnimalProductType et && item instanceof AnimalProduct ap && ap.getProductType() == et) return false;
        }

        if (specificType != null) {
            if (specificType instanceof FruitType t && item instanceof Fruit f) return f.getFruitType() == t;
            if (specificType instanceof CropType t && item instanceof Crop c) return c.getCropType() == t;
            if (specificType instanceof AnimalProductType t && item instanceof AnimalProduct ap) return ap.getProductType() == t;
            if (specificType instanceof ArtisanProductType t && item instanceof ArtisanProduct ap) return ap.getProductType() == t;
            return false;
        }

        return validTypes.contains(item.getType());
    }


    public List<ItemType> getValidTypes() {
        return validTypes;
    }

    public Object getSpecificType() {
        return specificType;
    }

    public Object getExcludedType() {
        return excludedType;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public String describe() {
        if (specificType != null) {
            return quantity + "x " + specificType.toString().replace("_", " ");
        } else if (validTypes.size() == 1) {
            return quantity + "x " + validTypes.get(0).name();
        } else {
            return quantity + "x (" + String.join(" or ", validTypes.stream().map(Enum::name).toList()) + ")";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ArtisanIngredient other)) return false;
        return Objects.equals(validTypes, other.validTypes)
            && Objects.equals(specificType, other.specificType)
            && Objects.equals(excludedType, other.excludedType)
            && quantity == other.quantity
            && price == other.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(validTypes, specificType, excludedType, quantity, price);
    }
}
