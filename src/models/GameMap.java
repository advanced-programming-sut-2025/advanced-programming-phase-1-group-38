package models;

import models.Animals.AnimalLivingSpace;
import models.Artisan.ArtisanMachine;
import models.enums.Seasons;
import models.enums.Types.StoneType;
import models.enums.Types.TileType;
import models.enums.Weather;
import models.enums.Types.FarmBuildingType;
import models.farming.*;

import java.util.*;

public class GameMap {
    private Tile[][] tiles;
    private int width;
    private int height;
    private final Position greenhouseTopLeft = new Position(6, 3);
    private final int greenhouseWidth = 5;
    private final int greenhouseHeight = 6;
    private boolean greenhouseBuilt = false;
    private final Position homeTopLeft = new Position(3, 15);
    private final int homeWidth = 4;
    private final int homeHeight = 4;
    private final List<ArtisanMachine> artisanMachines = new ArrayList<>();
    private final Random rng = new Random();


    private Map<Position, AnimalLivingSpace> animalBuildings = new HashMap<>();

    public GameMap(Tile[][] tiles, int width, int height, List<Shop> shops, List<Player> players, Weather startingWeather) {
        this.tiles = tiles;
        this.width = width;
        this.height = height;
        markGreenhouseTiles();
        markHomeArea();
        populateQuarry();
        populateForageTrees();
    }

    public void markQuarryArea(Position topLeft, int quarryWidth, int quarryHeight) {
        for (int dx = 0; dx < quarryWidth; dx++) {
            for (int dy = 0; dy < quarryHeight; dy++) {
                Position pos = new Position(topLeft.getX() + dx, topLeft.getY() + dy);
                Tile tile = getTile(pos);
                if (tile != null) {
                    tile.setTileType(TileType.QUARRY);
                    tile.setWalkable(true);
                    tile.setContent(null);
                }
            }
        }
    }


    public AnimalLivingSpace getAvailableLivingSpace(List<FarmBuildingType> allowedTypes) {
        for (AnimalLivingSpace space : animalBuildings.values()) {
            if (allowedTypes.contains(space.getFarmBuildingType()) && !space.isFull()) {
                return space;
            }
        }
        return null;
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

    public boolean isGreenhouseBuilt() {
        return greenhouseBuilt;
    }

    public void setGreenhouseBuilt(boolean built) {
        this.greenhouseBuilt = built;
    }

    public void markGreenhouseTiles() {
        for (int dx = 0; dx < greenhouseWidth; dx++) {
            for (int dy = 0; dy < greenhouseHeight; dy++) {
                Position pos = new Position(greenhouseTopLeft.getX() + dx, greenhouseTopLeft.getY() + dy);
                Tile tile = getTile(pos);
                if (tile != null) {
                    tile.setTileType(TileType.GREENHOUSE);
                }
            }
        }
    }

    public void markHomeArea() {
        for (int dx = 0; dx < homeWidth; dx++) {
            for (int dy = 0; dy < homeHeight; dy++) {
                Position pos = new Position(homeTopLeft.getX() + dx, homeTopLeft.getY() + dy);
                Tile tile = getTile(pos);
                if (tile != null) {
                    tile.setTileType(TileType.HOME);
                    tile.setWalkable(true);
                }
            }
        }
    }

    public void markLakeArea(Position topLeft, int lakeWidth, int lakeHeight) {
        for (int dx = 0; dx < lakeWidth; dx++) {
            for (int dy = 0; dy < lakeHeight; dy++) {
                Position pos = new Position(topLeft.getX() + dx, topLeft.getY() + dy);
                Tile tile = getTile(pos);
                if (tile != null) {
                    tile.setTileType(TileType.WATER);
                    tile.setWalkable(false);
                    tile.setContent(null);
                }
            }
        }
    }

    private void populateQuarry() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile t = tiles[x][y];
                if (t.getTileType() == TileType.QUARRY) {
                    double r = rng.nextDouble();
                    if (r < 0.6) {
                        t.setContent(new Stone(StoneType.REGULAR_STONE));
                    }
                    else if (r < 0.9) {
                        var types = ForagingMineralTypes.values();
                        var pick = types[rng.nextInt(types.length)];
                        t.setContent(new ForagingMineral(pick));
                    }
                }
            }
        }
    }

    private void populateForageTrees() {
        TreeType[] forageTypes = Arrays.stream(TreeType.values())
            .filter(TreeType::isForage)
            .toArray(TreeType[]::new);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile t = tiles[x][y];
                if (t.getTileType() == TileType.REGULAR_GROUND
                    && t.getContent() == null
                    && rng.nextDouble() < 0.05) {

                    int i = rng.nextInt(forageTypes.length);
                    t.setContent(new Tree(forageTypes[i]));
                }
            }
        }
    }

    public Tile getTile(Position pos) {
        int x = pos.getX();
        int y = pos.getY();
        if (x < 0 || y < 0 || x >= width || y >= height) return null;
        return tiles[x][y];
    }

    public boolean isInsideMap(Position pos) {
        int x = pos.getX();
        int y = pos.getY();
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public boolean canPlaceBuilding(Position topLeft, FarmBuildingType type) {
        int w = type.getWidth();
        int h = type.getLength();

        for (int dx = 0; dx < w; dx++) {
            for (int dy = 0; dy < h; dy++) {
                Position p = new Position(topLeft.getX() + dx, topLeft.getY() + dy);
                if (!isInsideMap(p)) return false;
                Tile tile = getTile(p);
                if (tile == null || tile.isOccupied()) return false;
            }
        }
        return true;
    }

    public void placeBuilding(Position topLeft, FarmBuildingType type, Object building) {
        int w = type.getWidth();
        int h = type.getLength();

        for (int dx = 0; dx < w; dx++) {
            for (int dy = 0; dy < h; dy++) {
                Position p = new Position(topLeft.getX() + dx, topLeft.getY() + dy);
                Tile tile = getTile(p);
                if (tile != null) {
                    tile.setContent(building);
                    tile.setWalkable(false);
                }
            }
        }
    }

    public boolean addAnimalBuilding(AnimalLivingSpace building) {
        Position pos = building.getPositionOfUpperLeftCorner();
        if (animalBuildings.containsKey(pos)) return false;
        animalBuildings.put(pos, building);
        return true;
    }

    public Position getHomeTopLeft() { return homeTopLeft; }
    public int getHomeWidth()      { return homeWidth; }
    public int getHomeHeight()     { return homeHeight; }

    public Collection<AnimalLivingSpace> getAnimalBuildings() {
        return animalBuildings.values();
    }

    public AnimalLivingSpace getAnimalBuildingAt(Position pos) {
        return animalBuildings.get(pos);
    }

    public List<ArtisanMachine> getArtisanMachines() {
        return artisanMachines;
    }

    public void addArtisanMachine(ArtisanMachine machine) {
        artisanMachines.add(machine);
    }
}
