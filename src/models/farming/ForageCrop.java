package models.farming;

import models.Item;
import models.enums.Types.ItemType;
import models.enums.Seasons;

public class ForageCrop extends Item {
    private final ForageCropTypes type;
    private final Seasons season;
    private final int basePrice;
    private final int energy;

    public ForageCrop(ForageCropTypes type, Seasons season, int basePrice, int energy) {
        super(ItemType.CROP);
        this.type = type;
        this.season = season;
        this.basePrice = basePrice;
        this.energy = energy;
    }

    public ForageCropTypes getName() { return type; }
    public Seasons getSeason() { return season; }
    public int getBasePrice() { return basePrice; }
    public int getEnergy() { return energy; }
}
