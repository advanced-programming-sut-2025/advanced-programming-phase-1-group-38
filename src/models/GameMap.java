package models;

import models.enums.Weather;

import java.util.List;

public class GameMap {
    private Tile[][] tiles;
    int width;
    int height;

    public GameMap(Tile[][] tiles, int width, int height , List<Shop> shops, List<Player> players, Weather startingWeather) {
        this.tiles = tiles;
        this.width = width;
        this.height = height;
    }
    public Tile[][] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
