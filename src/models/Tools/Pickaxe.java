package models.Tools;

import models.*;
import models.enums.Direction;
import models.enums.Skill;
import models.enums.SkillLevel;
import models.enums.ToolQuality;
import models.enums.Types.StoneType;
import models.enums.Types.TileType;
import models.enums.Types.ToolType;
import models.farming.ForagingMineral;
import models.farming.ForagingMineralTypes;
import models.farming.Stone;

public class Pickaxe extends Tool {
    public Pickaxe(ToolQuality quality) {
        super(ToolType.PICKAXE, quality, Skill.MINING);
    }

    @Override
    public int getEnergyCost(Player player) {
        int baseCost = getToolQuality().getEnergyCost();
        if (player.getSkillLevel(Skill.MINING) == SkillLevel.LEVEL_FOUR) {
            baseCost--;
        }
        return Math.max(baseCost, 0);
    }

    @Override
    public Result useTool(Game game, Direction direction) {
        Player player = game.getCurrentPlayer();
        Position target = player.getPosition().shift(direction);
        GameMap currentGameMap = game.getCurrentPlayerMap();
        Tile[][] tiles = currentGameMap.getTiles();
        int cost = getEnergyCost(player);

        if (player.getEnergy() < cost) {
            return new Result(false, "You don't have enough energy to use pickaxe.");
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
        boolean isTilled = tile.getTileType() == TileType.PLOWED_GROUND;

        if (!tile.isOccupied() && isTilled) {
            tile.setTileType(TileType.DIRT);
            player.reduceEnergy(cost);
            player.addEnergyUsed(cost);
            return new Result(true, "Dirt block at " + target);
        }

        Object content = tile.getContent();

        if (content instanceof StoneType) {
            tile.setContent(null);
            player.gainXP(Skill.MINING, Skill.MINING.getIncreasePerAction());
            player.getBackpack().addToInventory(new Stone(StoneType.REGULAR_STONE), 1);
            player.reduceEnergy(cost);
            player.addEnergyUsed(cost);
            return new Result(true, "Collected stone at " + target);
        }

        if (content instanceof ForagingMineralTypes mineral) {
            if (canBreakMineral(getToolQuality(), mineral)) {
                tile.setContent(null);
                player.gainXP(Skill.MINING, Skill.MINING.getIncreasePerAction());
                ForagingMineral mineralItem = new ForagingMineral(mineral);
                player.getBackpack().addToInventory(mineralItem, 1);
                player.reduceEnergy(cost);
                player.addEnergyUsed(cost);
                return new Result(true, "Collected " + mineral.getDisplayName() + " at " + target);
            } else {
                return new Result(false, "Your tool isn't strong enough to break this mineral.");
            }
        }

        int reduced = Math.max(1, cost - 1);
        player.reduceEnergy(reduced);
        player.addEnergyUsed(reduced);
        return new Result(false, "Nothing to use the pickaxe on.");
    }

    private boolean canBreakMineral(ToolQuality quality, ForagingMineralTypes mineral) {
        int requiredPower = switch (mineral) {
            case COAL, COPPER -> 0;
            case IRON -> 1;
            case GOLD -> 2;
            case IRIDIUM -> 3;
            default -> 1;
        };
        return quality.ordinal() >= requiredPower;
    }

    @Override
    public String toString() {
        return formatEnumName(type) + " (" + formatEnumName(getToolQuality()) + ")";
    }
}

