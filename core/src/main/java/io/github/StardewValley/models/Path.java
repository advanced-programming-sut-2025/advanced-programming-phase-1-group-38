package io.github.StardewValley.models;

import java.util.ArrayList;

public class Path {
    private ArrayList<Position> pathTiles;
    private int distanceInTiles;
    private int numOfTurns;
    private int energyNeeded;

    public Path(ArrayList<Position> pathTiles,
                int distanceInTiles,
                int numOfTurns,
                int energyNeeded) {
        this.pathTiles       = pathTiles;
        this.distanceInTiles = distanceInTiles;
        this.numOfTurns      = numOfTurns;
        this.energyNeeded    = energyNeeded;
    }

    public Path() { }

    public void setPathTiles(ArrayList<Position> pathTiles) {
        this.pathTiles = pathTiles;
    }
    public void setDistanceInTiles(int distanceInTiles) {
        this.distanceInTiles = distanceInTiles;
    }
    public void setNumOfTurns(int numOfTurns) {
        this.numOfTurns = numOfTurns;
    }
    public void setEnergyNeeded(int energyNeeded) {
        this.energyNeeded = energyNeeded;
    }

    public ArrayList<Position> getPathTiles() {
        return pathTiles;
    }
    public int getDistanceInTiles() {
        return distanceInTiles;
    }
    public int getNumOfTurns() {
        return numOfTurns;
    }
    public int getEnergyNeeded() {
        return energyNeeded;
    }

    @Override
    public String toString() {
        return String.format(
            "Path(tiles=%d, distance=%d, turns=%d, energy=%.2f)",
            pathTiles.size(),
            distanceInTiles,
            numOfTurns,
            energyNeeded / 1.0
        );
    }
}
