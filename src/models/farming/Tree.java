package models.farming;

import models.enums.Seasons;
import java.util.List;

public class Tree {
    private final TreeType type;

    private boolean isBurnt = false;
    private boolean isMature = false;
    private int daysSincePlanted = 0;
    private int daysSinceLastHarvest = 0;
    private boolean hasProductReady = false;

    public Tree(TreeType type) {
        this.type = type;
    }

    public void treeNextDay(Seasons currentSeason) {
        if (isBurnt) return;

        if (!isMature) {
            daysSincePlanted++;
            if (daysSincePlanted >= type.getTotalHarvestTime()) {
                isMature = true;
                if (!type.isForage()) {
                    daysSinceLastHarvest = 0;
                }
            }
            return;
        }

        if (!type.isForage() && !type.getSeasons().contains(currentSeason)) {
            daysSinceLastHarvest = 0;
            return;
        }

        if (!type.isForage()) {
            daysSinceLastHarvest++;
            if (daysSinceLastHarvest >= type.getHarvestCycle()) {
                hasProductReady = true;
            }
        }
    }

    public boolean canHarvestSyrup() {
        return type.getProduct() instanceof SyrupType && isMature && hasProductReady && !isBurnt;
    }

    public SyrupType harvestSyrup() {
        if (canHarvestSyrup()) {
            hasProductReady = false;
            daysSinceLastHarvest = 0;
            return (SyrupType) type.getProduct();
        }
        return null;
    }

    public boolean canHarvestFruit() {
        return type.getProduct() instanceof FruitType && isMature && hasProductReady && !isBurnt;
    }

    public FruitType harvestFruit() {
        if (canHarvestFruit()) {
            hasProductReady = false;
            daysSinceLastHarvest = 0;
            return (FruitType) type.getProduct();
        }
        return null;
    }

    public int getCurrentStageNumber() {
        int sum = 0;
        List<Integer> stages = type.getStages();
        for (int i = 0; i < stages.size(); i++) {
            sum += stages.get(i);
            if (daysSincePlanted < sum) return i + 1;
        }
        return stages.size();
    }

    public String getName() { return type.getName(); }
    public TreeSourceType getSource() { return type.getSource(); }
    public List<Integer> getGrowthStages() { return type.getStages(); }
    public int getTotalHarvestTime() { return type.getTotalHarvestTime(); }
    public TreeProductType getProductType() { return type.getProduct(); }
    public int getProductHarvestCycle() { return type.getHarvestCycle(); }
    public List<Seasons> getSeasons() { return type.getSeasons(); }
    public boolean isForageTree() { return type.isForage(); }

    public boolean isBurnt() { return isBurnt; }
    public void burn() { isBurnt = true; }
    public boolean isMature() { return isMature; }
    public boolean hasProductReady() { return hasProductReady; }

    public TreeSeedType cutDown() {
        if (type.isForage() && type.getSource() instanceof TreeSeedType seed) {
            return seed;
        }
        return null;
    }
}
