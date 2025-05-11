package models.Tools;

import models.*;
import models.enums.Direction;
import models.enums.Skill;
import models.enums.SkillLevel;
import models.enums.ToolQuality;
import models.enums.Types.TileType;
import models.enums.Types.ToolType;

public class Hoe extends Tool {
    public Hoe(ToolQuality quality) {
        super(ToolType.HOE, quality, Skill.FARMING);
    }

    @Override
    public int getEnergyCost(Player player) {
        int baseCost = getToolQuality().getEnergyCost();
        if (player.getSkillLevel(Skill.FARMING) == SkillLevel.LEVEL_FOUR) {
            baseCost--;
        }
        return Math.max(baseCost, 0);
    }

    @Override
    public Result useTool(GameMap gameMap, Direction direction) {
        Player player = gameMap.getCurrentPlayer();
        Position target = player.getPosition().shift(direction);
        Tile[][] tiles = gameMap.getTiles();

        int cost = getEnergyCost(player);

        if (player.getEnergy() < cost) {
            return new Result(false, "You don't have enough energy to use hoe.");
        }

        if (player.getEnergyUsedThisTurn() + cost > 50) {
            return new Result(false, "Not enough energy for this turn.");
        }

        if (target.getX() < 0 || target.getY() < 0 ||
            target.getX() >= gameMap.getWidth() ||
            target.getY() >= gameMap.getHeight()) {
            return new Result(false, "Target tile is out of bounds.");
        }

        Tile tile = tiles[target.getY()][target.getX()];

        boolean isTillable = tile.getTileType() == TileType.DIRT;
        boolean isOccupied = tile.isOccupied();

        player.reduceEnergy(cost);
        player.addEnergyUsed(cost);

        if (isTillable && !isOccupied) {
            tile.setTileType(TileType.PLOWED_GROUND);
            return new Result(true, "Soil tilled at " + target);
        } else {
            return new Result(false, "Cannot till this tile.");
        }
    }

    @Override
    public String toString() {
        return formatEnumName(type) + " (" + formatEnumName(getToolQuality()) + ")";
    }
}
