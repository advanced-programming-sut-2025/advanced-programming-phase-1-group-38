package models.Tools;

import models.*;
import models.enums.Direction;
import models.enums.Types.ItemType;
import models.enums.Types.ToolType;
import models.farming.Crop;

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
        int cost = getEnergyCost(player);

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
            Item harvestedItem = new Crop(crop.getCropType());
            player.getBackpack().addToInventory(harvestedItem, 1);

            if (crop.getCropType().isOneTime() || crop.getCropType().isForage()) {
                tile.setContent(null);
            } else {
                crop.harvest();
            }

            return new Result(true, "Harvested " + crop.getName() + " at " + target);
        }

        if (content == ItemType.WEED) {
            tile.setContent(null);
            return new Result(true, "Killed weed at " + target);
        }

        return new Result(false, "Nothing to harvest or cut.");
    }

    @Override
    public String toString() {
        return formatEnumName(type) + " (" + formatEnumName(getToolQuality()) + ")";
    }
}

