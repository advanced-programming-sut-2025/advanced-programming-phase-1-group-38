package models.Tools;

import models.*;
import models.enums.Direction;
import models.enums.Skill;
import models.enums.SkillLevel;
import models.enums.ToolQuality;
import models.enums.Types.ItemType;
import models.enums.Types.StoneType;
import models.enums.Types.TileType;
import models.enums.Types.ToolType;
import models.farming.*;
import models.farming.Tree;

public class Axe extends Tool {
    public Axe(ToolQuality quality) {
        super(ToolType.AXE, quality, Skill.FORAGING);
    }

    @Override
    public int getEnergyCost(Player player) {
        int baseCost = getToolQuality().getEnergyCost();
        if (player.getSkillLevel(Skill.FORAGING) == SkillLevel.LEVEL_FOUR) {
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
            return new Result(false, "You don't have enough energy to use axe.");
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

        if (content == ItemType.BRANCH) {
            tile.setContent(null);
            player.reduceEnergy(cost);
            player.addEnergyUsed(cost);
            return new Result(true, "Removed a fallen branch at " + target);
        }

        if (content instanceof Tree) {
            tile.setContent(null);
            player.gainXP(Skill.FORAGING, Skill.FORAGING.getIncreasePerAction());
            player.getBackpack().addToInventory(new Wood(), 1);
            player.reduceEnergy(cost);
            player.addEnergyUsed(cost);
            return new Result(true, "Collected wood at " + target);
        }

        int reduced = Math.max(1, cost - 1);
        player.reduceEnergy(reduced);
        player.addEnergyUsed(reduced);
        return new Result(false, "Nothing to use the axe.");
    }

    @Override
    public String toString() {
        return formatEnumName(type) + " (" + formatEnumName(getToolQuality()) + ")";
    }
}

