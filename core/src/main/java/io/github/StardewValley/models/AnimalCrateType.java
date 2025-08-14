// models/AnimalCrateType.java
package io.github.StardewValley.models;

import io.github.StardewValley.controllers.GameController;

public class AnimalCrateType implements ItemType {
    private final String id;
    private final AnimalSpecies species;

    public AnimalCrateType(AnimalSpecies species) {
        this.species = species;
        this.id = "animals/" + species.name().toLowerCase() + "_crate"; // e.g. "chicken_crate"
    }

    @Override public String id() { return id; }
    public String title() { return species.display + " Crate"; }
    public String spritePath() { return id + ".png"; }
    @Override public String iconPath() { return id + ".png"; } // if your UI uses iconPath()
    @Override public int maxStack() { return 99; }

    public AnimalSpecies species() { return species; }

    public boolean place(GameController gc, float x, float y) {
        return gc.placeAnimalFromCrate(this, x, y);
    }
}
