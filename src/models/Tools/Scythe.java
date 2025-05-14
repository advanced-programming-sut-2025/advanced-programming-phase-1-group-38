package models.Tools;

import models.*;
import models.enums.Direction;
import models.enums.Types.ItemType;
import models.enums.Types.ToolType;
import models.farming.*;

public class Scythe extends Tool {
    public Scythe() {
        super(ToolType.SCYTHE, 2, null);
    }


    @Override
    public int getEnergyCost(Player player) {
        return 2;
    }

    @Override
    public Result useTool(Game game, Direction direction) {
        Player player = game.getCurrentPlayer();
        Position target = player.getPosition().shift(direction);
        GameMap currentGameMap = game.getCurrentPlayerMap();
        Tile[][] tiles = currentGameMap.getTiles();
        int cost = getModifiedEnergyCost(player, game.getCurrentWeather());

        if (player.getEnergy() < cost) {
            return new Result(false, "You don't have enough energy to use scythe.");
        }

        if (player.getEnergyUsedThisTurn() + cost > 50) {
            return new Result(false, "Not enough energy for this turn.");
        }

        if (target.getX() < 0 || target.getY() < 0 ||
            target.getX() >= currentGameMap.getWidth() ||
            target.getY() >= currentGameMap.getHeight()) {
            return new Result(false, "Target tile is out of bounds.");
        }

        Tile tile = tiles[target.getY()][target.getX()];
        Object content = tile.getContent();

        player.reduceEnergy(cost);
        player.addEnergyUsed(cost);

        if (content instanceof Crop crop && crop.isReadyToHarvest()) {
            player.getBackpack().addToInventory(new HarvestedCrop(crop.getCropType()), 1);

            if (crop.shouldRemoveAfterHarvest()) {
                tile.setContent(null);
            } else {
                crop.harvest();
            }

            return new Result(true, "Harvested " + crop.getName() + " at " + target);
        }

        if (content instanceof Tree tree && tree.canHarvestFruit()) {
            FruitType fruitType = tree.harvestFruit();
            if (fruitType != null) {
                player.getBackpack().addToInventory(new Fruit(fruitType), 1);
                return new Result(true, "Harvested " + fruitType.getName() + " from tree at " + target);
            }
        }

        if (content instanceof Item item && item.getType() == ItemType.WEED) {
            tile.setContent(null);
            return new Result(true, "Cleared weed at " + target);
        }

        return new Result(false, "Nothing to harvest or cut.");
    }

    @Override
    public String toString() {
        return formatEnumName(type) + " (" + formatEnumName(getToolQuality()) + ")";
    }
}

