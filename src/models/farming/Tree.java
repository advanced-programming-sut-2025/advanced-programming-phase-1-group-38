package models.farming;
import models.Item;
import models.enums.Types.ItemType;
import models.enums.Seasons;

public class Tree extends Item {
    private final String name;
    private final String source;
    private final String stages;
    private final int totalHarvestTime;
    private final FruitType fruit;
    private final int fruitHarvestCycle;
    private final boolean isFruitEdible;
    private final int fruitEnergy;
    private final Seasons season;
    private boolean isBurnt;


    public Tree(String name, String source, String stages, int totalHarvestTime,
                FruitType fruit, int fruitHarvestCycle, boolean isFruitEdible,
                int fruitEnergy, Seasons season) {
        super(ItemType.TREE);
        this.name = name;
        this.source = source;
        this.stages = stages;
        this.totalHarvestTime = totalHarvestTime;
        this.fruit = fruit;
        this.fruitHarvestCycle = fruitHarvestCycle;
        this.isFruitEdible = isFruitEdible;
        this.fruitEnergy = fruitEnergy;
        this.season = season;
        this.isBurnt = false;
    }

    public boolean isBurnt() { return isBurnt; }
    public void burn() { this.isBurnt = true; }

    public FruitType getFruit() { return fruit; }
    public int getFruitEnergy() { return fruitEnergy; }
    public boolean isFruitEdible() { return isFruitEdible; }
    public Seasons getSeason() { return season; }
}
