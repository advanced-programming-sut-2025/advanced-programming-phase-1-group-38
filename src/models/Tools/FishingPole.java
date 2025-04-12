package models.Tools;

import models.Tool;
import models.enums.*;

public class FishingPole extends Tool {
    public FishingPole(FishingPoleType fishingPoleType) {
        super(ToolType.FISHING_POLE, fishingPoleType, Skill.FISHING);
    }

    @Override
    public void useTool(Direction direction) {}
}
