package models;

import java.util.ArrayList;

public class Path {
    ArrayList<Position> pathTiles; // contains origin and destination
    int distanceInTiles;
    int numOfTurns;
    int energyNeeded;

    public ArrayList<Position> getPathTiles() {
        return pathTiles;
    }

    public int getEnergyNeeded() {
        return energyNeeded;
    }

    public int getNumOfTurns() {
        return numOfTurns;
    }

    public int getDistanceInTiles() {
        return distanceInTiles;
    }
}
