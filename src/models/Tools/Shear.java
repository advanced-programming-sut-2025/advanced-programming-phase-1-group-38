package models.Tools;

import models.Tool;
import models.enums.Direction;
import models.enums.Types.ToolType;

public class Shear extends Tool {
    public Shear() {
        super(ToolType.SHEAR, 4, null);
    }

    @Override
    public void useTool(Direction direction) {}
}

