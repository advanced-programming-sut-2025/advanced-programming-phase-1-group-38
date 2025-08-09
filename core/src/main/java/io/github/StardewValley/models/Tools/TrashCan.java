//package io.github.StardewValley.models.Tools;
//
//import io.github.StardewValley.models.Game;
//import io.github.StardewValley.models.Player;
//import io.github.StardewValley.models.Result;
//import io.github.StardewValley.models.Tool;
//import io.github.StardewValley.models.enums.Direction;
//import io.github.StardewValley.models.enums.TrashCanQuality;
//
//public class TrashCan extends Tool {
//
//    public TrashCan(TrashCanQuality quality) {
//        super(ToolType.TRASH_CAN, quality, null);
//    }
//
//    @Override
//    public int getEnergyCost(Player player) {
//        return 0;
//    }
//
//    @Override
//    public Result useTool(Game game, Direction direction) {
//        return new Result(false, "Trash can is used from the inventory menu to discard items.");
//    }
//
//    @Override
//    public String toString() {
//        return "Trash Can (" + formatEnumName(getTrashCanQuality()) + ")";
//    }
//}
