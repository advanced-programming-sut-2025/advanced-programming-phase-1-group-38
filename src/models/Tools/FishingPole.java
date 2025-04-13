package models.Tools;

import models.Tool;
import models.enums.*;
import models.enums.Types.FishingPoleType;
import models.enums.Types.ToolType;

public class FishingPole extends Tool {
    public FishingPole(FishingPoleType fishingPoleType) {
        super(ToolType.FISHING_POLE, fishingPoleType, Skill.FISHING);
    }

    @Override
    public void useTool(Direction direction) {}
}
