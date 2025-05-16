package models.Tools;

import models.*;
import models.Animals.Animal;
import models.Animals.AnimalProduct;
import models.enums.Direction;
import models.enums.Types.AnimalProductType;
import models.enums.Types.AnimalType;
import models.enums.Types.ToolType;

public class Shear extends Tool {
    public Shear() {
        super(ToolType.SHEAR, 4, null);
    }

    @Override
    public int getEnergyCost(Player player) {
        return 4;
    }

    @Override
    public Result useTool(Game game, Direction direction) {
        Player player = game.getCurrentPlayer();
        int cost = getModifiedEnergyCost(player, game.getCurrentWeather());

        if (player.getEnergy() < cost) {
            return new Result(false, "You don't have enough energy.");
        }

        if (player.getEnergyUsedThisTurn() + cost > 50) {
            return new Result(false, "Not enough energy for this turn.");
        }

        Position target = player.getPosition().shift(direction);
        GameMap currentGameMap = game.getCurrentPlayerMap();
        Tile[][] tiles = currentGameMap.getTiles();

        if (target.getX() < 0 || target.getY() < 0 ||
            target.getX() >= currentGameMap.getWidth() ||
            target.getY() >= currentGameMap.getHeight()) {
            return new Result(false, "Target tile is out of bounds.");
        }

        Tile tile = tiles[target.getY()][target.getX()];
        Object content = tile.getContent();

        player.reduceEnergy(cost);
        player.addEnergyUsed(cost);

        if (content instanceof Animal animal) {
            if (animal.getAnimalType() == AnimalType.SHEEP) {
                AnimalProduct product = animal.collectProduct();
                if (product != null && product.getProductType() == AnimalProductType.WOOL) {
                    player.getBackpack().addToInventory(product, 1);
                    return new Result(true, "You sheared " + animal.getName() + " and collected " +
                        product.getQuality().name().toLowerCase() + " wool.");
                } else {
                    return new Result(false, "The sheep is not ready to be sheared.");
                }
            } else {
                return new Result(false, "You can only use Shear on sheep.");
            }
        }

        return new Result(false, "There's no sheep to shear on this tile.");
    }

        @Override
    public String toString() {
        return formatEnumName(type) + " (" + formatEnumName(getToolQuality()) + ")";
    }
}

