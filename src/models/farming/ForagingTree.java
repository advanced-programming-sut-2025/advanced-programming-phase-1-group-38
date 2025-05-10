package models.farming;

import models.Item;
import models.enums.Types.ItemType;
import models.enums.Seasons;

public class ForagingTree extends Item {
    private final ForagingTreeTypes type;
    private final Seasons season;

    public ForagingTree(ForagingTreeTypes type) {
        super(ItemType.TREE);
        this.type = type;
        this.season = Seasons.SPECIAL;
    }

    public ForagingTreeTypes getTreeType() {
        return type;
    }

    public Seasons getSeason() {
        return season;
    }
}
