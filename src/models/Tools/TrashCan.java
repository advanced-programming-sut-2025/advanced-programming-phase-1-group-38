package models.Tools;

import models.*;
import models.enums.*;
import models.enums.Types.ToolType;

public class TrashCan extends Tool {

    public TrashCan(TrashCanQuality quality) {
        super(ToolType.TRASH_CAN, quality, null);
    }

    @Override
    public int getEnergyCost(Player player) {
        return 0;
    }

    @Override
    public Result useTool(Game game, Direction direction) {
        return new Result(false, "Trash can is used from the inventory menu to discard items.");
    }

    @Override
    public String toString() {
        return "Trash Can (" + formatEnumName(getTrashCanQuality()) + ")";
    }
}
