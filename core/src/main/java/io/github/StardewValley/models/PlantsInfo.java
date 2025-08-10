package io.github.StardewValley.models;

import io.github.StardewValley.models.enums.Seasons;

import java.util.ArrayList;

public class PlantsInfo {
    private String name;
    private int numOfStages;
    private ArrayList<Integer> stages;
    private int totalHarvestTime;
    private int baseSellPrice;
    private boolean isEdible;
    private int energy;
    private ArrayList<Seasons> seasons;
    private boolean canBecomeGiant;
    private boolean isGiant;
    private Position position;

    public void showInfo() {
    }
}
