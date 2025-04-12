package models.Tools;

import models.Tool;
import models.enums.Direction;
import models.enums.ToolType;

public class MilkPail extends Tool {
    public MilkPail() {
        super(ToolType.MILK_PAIL, 4 , null);
    }

    @Override
    public void useTool(Direction direction) {}
}

