package models.Tools;

import models.Tool;
import models.enums.*;
import models.enums.Types.ToolType;

public class TrashCan extends Tool {
    public TrashCan(TrashCanQuality quality) {
        super(ToolType.TRASH_CAN, quality, null);
    }

    @Override
    public void useTool(Direction direction) {}
}