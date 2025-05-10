package models.farming;

import models.enums.Seasons;

import java.util.ArrayList;

public class PlantsInfo {
    private final String name;
    private final PlantSource source;
    private final int numOfStages;
    private final ArrayList<Integer> stages;
    private final int totalHarvestTime;

    private final boolean oneTime;
    private final int regrowthTime;
    private final boolean isEdible;
    private final int energy;
    private final int baseSellPrice;

    private final Seasons season;
    private final boolean canBecomeGiant;

    public PlantsInfo(String name,
                      PlantSource source,
                      ArrayList<Integer> stages,
                      boolean oneTime,
                      int regrowthTime,
                      boolean isEdible,
                      int energy,
                      int baseSellPrice,
                      Seasons season,
                      boolean canBecomeGiant) {

        this.name = name;
        this.source = source;
        this.stages = stages;
        this.numOfStages = stages.size();
        this.totalHarvestTime = stages.stream().mapToInt(Integer::intValue).sum();

        this.oneTime = oneTime;
        this.regrowthTime = regrowthTime;
        this.isEdible = isEdible;
        this.energy = energy;
        this.baseSellPrice = baseSellPrice;
        this.season = season;
        this.canBecomeGiant = canBecomeGiant;
    }

    // Getters
    public String getName() { return name; }
    public PlantSource getSource() { return source; }
    public int getNumOfStages() { return numOfStages; }
    public ArrayList<Integer> getStages() { return stages; }
    public int getTotalHarvestTime() { return totalHarvestTime; }

    public boolean isOneTime() { return oneTime; }
    public int getRegrowthTime() { return regrowthTime; }
    public boolean isEdible() { return isEdible; }
    public int getEnergy() { return energy; }
    public int getBaseSellPrice() { return baseSellPrice; }
    public Seasons getSeason() { return season; }
    public boolean canBecomeGiant() { return canBecomeGiant; }
}
