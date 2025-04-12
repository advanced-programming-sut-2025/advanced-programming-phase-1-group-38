package models.Tools;

import models.Tool;
import models.enums.Direction;
import models.enums.Skill;
import models.enums.ToolQuality;
import models.enums.ToolType;

public class Shear extends Tool {
    public Shear() {
        super(ToolType.SHEAR, 4, null);
    }

    @Override
    public void useTool(Direction direction) {}
}

