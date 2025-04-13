package models.farming;

import models.Item;
import models.enums.Types.ItemType;
import models.enums.Seasons;

public class ForageSeed extends Item {
    private final ForageSeedTypes type;
    private final Seasons season;

    public ForageSeed(ForageSeedTypes type, Seasons season) {
        super(ItemType.SEED);
        this.type = type;
        this.season = season;
    }

    public ForageSeedTypes getName() {
        return type;
    }

    public Seasons getSeason() {
        return season;
    }
}
