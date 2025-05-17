package models.Tools;

import models.*;
import models.Animals.Animal;
import models.Animals.AnimalProduct;
import models.enums.Direction;
import models.enums.Types.ToolType;

public class MilkPail extends Tool {
    public MilkPail() {
        super(ToolType.MILK_PAIL, 4 , null);
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
        Tile tile = game.getCurrentPlayerMap().getTile(target);

        if (tile == null || tile.getContent() == null || !(tile.getContent() instanceof Animal animal)) {
            return new Result(false, "There's no animal to milk on this tile.");
        }

        if (animal.getAnimalType().getAnimalProducts().stream().noneMatch(p -> p.getRequiredToolName() == ToolType.MILK_PAIL)) {
            return new Result(false, "This animal can't be milked.");
        }

        AnimalProduct product = animal.collectProduct();
        if (product == null) {
            return new Result(false, animal.getName() + " is not ready to produce milk.");
        }

        if (product.getProductType().getRequiredToolName() != ToolType.MILK_PAIL) {
            return new Result(false, "You need another tool to collect this product.");
        }

        player.reduceEnergy(cost);
        player.addEnergyUsed(cost);
        player.getBackpack().addToInventory(product, 1);

        return new Result(true, "You milked " + animal.getName() + " and collected " +
            product.getQuality().name().toLowerCase() + " " + product.getProductType().name().toLowerCase().replace("_", " ") + ".");
    }

    @Override
    public String toString() {
        return formatEnumName(type) + " (" + formatEnumName(getToolQuality()) + ")";
    }
}

