package io.github.StardewValley.models.farming;

import io.github.StardewValley.models.enums.Seasons;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TreeType {
    APRICOT("Apricot", TreeSaplingType.APRICOT_SAPLING, "7-7-7-7", 28, FruitType.APRICOT, 1, List.of(Seasons.SPRING), false),
    CHERRY("Cherry", TreeSaplingType.CHERRY_SAPLING, "7-7-7-7", 28, FruitType.CHERRY, 1, List.of(Seasons.SPRING), false),
    BANANA("Banana", TreeSaplingType.BANANA_SAPLING, "7-7-7-7", 28, FruitType.BANANA, 1, List.of(Seasons.SUMMER), false),
    MANGO("Mango", TreeSaplingType.MANGO_SAPLING, "7-7-7-7", 28, FruitType.MANGO, 1, List.of(Seasons.SUMMER), false),
    ORANGE("Orange", TreeSaplingType.ORANGE_SAPLING, "7-7-7-7", 28, FruitType.ORANGE, 1, List.of(Seasons.SUMMER), false),
    PEACH("Peach", TreeSaplingType.PEACH_SAPLING, "7-7-7-7", 28, FruitType.PEACH, 1, List.of(Seasons.SUMMER), false),
    APPLE("Apple", TreeSaplingType.APPLE_SAPLING, "7-7-7-7", 28, FruitType.APPLE, 1, List.of(Seasons.FALL), false),
    POMEGRANATE("Pomegranate", TreeSaplingType.POMEGRANATE_SAPLING, "7-7-7-7", 28, FruitType.POMEGRANATE, 1, List.of(Seasons.FALL), false),
       FORAGING_TREE("Foraging Tree", null, "7-7-7-7", 0, null, 0, List.of(Seasons.SPECIAL), true);

    private final String name;
    private final TreeSourceType source;
    private final List<Integer> stages;
    private final int totalHarvestTime;
    private final TreeProductType product;
    private final int harvestCycle;
    private final List<Seasons> seasons;
    private final boolean isForage;

    TreeType(String name, TreeSourceType source, String stagesStr, int totalHarvestTime,
             TreeProductType product, int harvestCycle, List<Seasons> seasons,
             boolean isForage) {
        this.name = name;
        this.source = source;
        this.stages = Arrays.stream(stagesStr.split("-"))
            .map(Integer::parseInt)
            .collect(Collectors.toList());
        this.totalHarvestTime = totalHarvestTime;
        this.product = product;
        this.harvestCycle = harvestCycle;
        this.seasons = seasons;
        this.isForage = isForage;
    }

    public String getName() { return name; }
    public TreeSourceType getSource() { return source; }
    public List<Integer> getStages() { return stages; }
    public int getTotalHarvestTime() { return totalHarvestTime; }
    public TreeProductType getProduct() { return product; }
    public int getHarvestCycle() { return harvestCycle; }
    public List<Seasons> getSeasons() { return seasons; }
    public boolean isForage() { return isForage; }
    public boolean growsIn(Seasons season) {
        return seasons.contains(season);
    }

    public boolean producesFruit() {
        return product instanceof FruitType;
    }

    public boolean producesSyrup() {
        return product instanceof SyrupType;
    }

    @Override
    public String toString() {
        return name;
    }
}
