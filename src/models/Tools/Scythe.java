package models.Tools;

import models.Tool;
import models.enums.Direction;
import models.enums.Types.ToolType;

public class Scythe extends Tool {
    public Scythe() {
        super(ToolType.SCYTHE, 2, null);
    }

    @Override
    public void useTool(Direction direction) {}
}

