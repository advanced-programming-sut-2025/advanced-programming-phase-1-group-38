package models.farming;

import models.Item;
import models.enums.Types.ItemType;
import models.enums.Seasons;

public class Crop extends Item {
    private final String name;
    private final String source;
    private final String stages;
    private final int totalHarvestTime;
    private final boolean oneTime;
    private final int regrowthTime;
    private final int baseSellPrice;
    private final boolean isEdible;
    private final int energy;
    private final Seasons season;
    private final boolean canBecomeGiant;

    public Crop(String name, String source, String stages, int totalHarvestTime, boolean oneTime,
                int regrowthTime, int baseSellPrice, boolean isEdible, int energy,
                Seasons season, boolean canBecomeGiant) {
        super(ItemType.CROP);
        this.name = name;
        this.source = source;
        this.stages = stages;
        this.totalHarvestTime = totalHarvestTime;
        this.oneTime = oneTime;
        this.regrowthTime = regrowthTime;
        this.baseSellPrice = baseSellPrice;
        this.isEdible = isEdible;
        this.energy = energy;
        this.season = season;
        this.canBecomeGiant = canBecomeGiant;
    }

    public boolean canBecomeGiant() { return canBecomeGiant; }
    public Seasons getSeason() { return season; }
    public int getBaseSellPrice() { return baseSellPrice; }
}
