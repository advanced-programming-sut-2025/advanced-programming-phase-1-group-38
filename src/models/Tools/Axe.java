package models.Tools;

import models.*;
import models.enums.Direction;
import models.enums.Skill;
import models.enums.SkillLevel;
import models.enums.ToolQuality;
import models.enums.Types.ToolType;
import models.farming.*;

import java.util.ArrayList;
import java.util.List;

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
        int cost = getModifiedEnergyCost(player, game.getCurrentWeather());

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

        if (content instanceof Branch) {
            tile.setContent(null);
            player.reduceEnergy(cost);
            player.addEnergyUsed(cost);
            return new Result(true, "Removed a fallen branch at " + target);
        }

        if (content instanceof Tree tree) {
            // Prepare syrup if available
            Syrup syrup = null;
            String syrupName = null;
            if (tree.canHarvestSyrup()) {
                SyrupType syrupType = tree.harvestSyrup();
                if (syrupType != null) {
                    syrup = new Syrup(syrupType);
                    syrupName = syrupType.getName();
                }
            }

            // Prepare wood
            Wood wood = new Wood();

            // Prepare seed drops
            List<TreeSeedType> seedTypes = tree.peekSeedDrop();
            List<Seed> seeds = new ArrayList<>();
            for (TreeSeedType type : seedTypes) {
                seeds.add(new Seed(type));
            }

            // Check inventory space
            if (syrup != null && !player.getBackpack().hasSpaceFor(syrup, 1)) {
                return new Result(false, "Your inventory is full.");
            }

            if (!player.getBackpack().hasSpaceFor(wood, 1)) {
                return new Result(false, "Your inventory is full.");
            }

            for (Seed seed : seeds) {
                if (!player.getBackpack().hasSpaceFor(seed, 1)) {
                    return new Result(false, "Your inventory is full.");
                }
            }

            // All checks passed – apply changes
            tile.setContent(null);
            player.gainXP(Skill.FORAGING, Skill.FORAGING.getIncreasePerAction());
            if (syrup != null) {
                player.getBackpack().addToInventory(syrup, 1);
            }
            player.getBackpack().addToInventory(wood, 1);
            for (TreeSeedType type : tree.cutDown()) {
                player.getBackpack().addToInventory(new Seed(type), 1);
            }

            player.reduceEnergy(cost);
            player.addEnergyUsed(cost);

            StringBuilder message = new StringBuilder("Cut down tree at " + target);
            if (syrupName != null) {
                message.insert(0, "Collected " + syrupName + ". ");
            }
            message.append("Collected seed")
                .append(seedTypes.size() > 1 ? "s: " : ": ")
                .append(seedTypes.size());

            return new Result(true, message.toString());
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
