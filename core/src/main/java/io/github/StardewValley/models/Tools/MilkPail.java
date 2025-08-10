//package io.github.StardewValley.models.Tools;
//
//import io.github.StardewValley.models.*;
//import io.github.StardewValley.models.Animals.Animal;
//import io.github.StardewValley.models.Animals.AnimalLivingSpace;
//import io.github.StardewValley.models.Animals.AnimalProduct;
//import io.github.StardewValley.models.enums.Direction;
//import io.github.StardewValley.models.enums.Types.AnimalProductType;
//
//public class MilkPail extends Tool {
//    public MilkPail() {
//        super(ToolType.MILK_PAIL, 4 , null);
//    }
//
//
//    @Override
//    public int getEnergyCost(Player player) {
//        return 4;
//    }
//
//    @Override
//    public Result useTool(Game game, Direction direction) {
//        Player player = game.getCurrentPlayer();
//        int cost = getModifiedEnergyCost(player, game.getCurrentWeather());
//
//        if (player.getEnergy() < cost)
//            return new Result(false, "You don't have enough energy.");
//        if (player.getEnergyUsedThisTurn() + cost > 50)
//            return new Result(false, "Not enough energy for this turn.");
//
//        Position target = player.getPosition().shift(direction);
//        Animal toMilk = null;
//        for (AnimalLivingSpace space : game.getCurrentPlayerMap().getAnimalBuildings()) {
//            for (Animal a : space.getAnimals()) {
//                if (a.getPosition().equals(target)) {
//                    toMilk = a;
//                    break;
//                }
//            }
//            if (toMilk != null) break;
//        }
//        if (toMilk == null)
//            return new Result(false, "There's no animal to milk here.");
//
//        AnimalProduct product = toMilk.collectProduct();
//        if (product == null)
//            return new Result(false, toMilk.getName() + " is not ready to be milked.");
//
//        AnimalProductType pt = product.getProductType();
//        if (!pt.requiresTool() ||
//            pt.getRequiredToolName() != ToolType.MILK_PAIL) {
//            return new Result(false, "You can't milk a " + toMilk.getAnimalType().name().toLowerCase() + ".");
//        }
//
//        player.reduceEnergy(cost);
//        player.addEnergyUsed(cost);
//        if (!player.getBackpack().hasSpaceFor(product, 1))
//            return new Result(false, "Your inventory is full.");
//        player.getBackpack().addToInventory(product, 1);
//
//        return new Result(true,
//            "You milked " + toMilk.getName() +
//                " and collected " + product.getQuality().name().toLowerCase() +
//                " " + pt.name().toLowerCase().replace('_',' ') + ".");
//    }
//
//    @Override
//    public String toString() {
//        return formatEnumName(type) + " (" + formatEnumName(getToolQuality()) + ")";
//    }
//}
//
