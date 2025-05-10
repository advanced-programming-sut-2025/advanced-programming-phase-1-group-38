package models.farming;

public class PlantSource {
    private final String name;
    private final boolean isMixedSeed;

    public PlantSource(String name, boolean isMixedSeed) {
        this.name = name;
        this.isMixedSeed = isMixedSeed;
    }

    public String getName() {
        return name;
    }

    public boolean isMixedSeed() {
        return isMixedSeed;
    }
}
