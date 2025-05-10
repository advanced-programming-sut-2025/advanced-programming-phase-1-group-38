package models;

import models.enums.*;
import models.enums.Types.FishingPoleType;
import models.enums.Types.ItemType;
import models.enums.Types.ToolType;

public class Tool extends Item {
    private ToolType type;
    private ToolQuality toolQuality;
    private Skill relatedSkill;
    private Integer fixedEnergyCost;
    private FishingPoleType fishingPoleType;
    private TrashCanQuality TrashCanQuality;

    public Tool(ToolType type, ToolQuality quality, Skill relatedSkill) {
        super(ItemType.TOOL);
        this.type = type;
        this.toolQuality = quality;
        this.relatedSkill = relatedSkill;
    }

    public Tool(ToolType type, TrashCanQuality quality, Skill relatedSkill) {
        super(ItemType.TOOL);
        this.type = type;
        this.TrashCanQuality = quality;
        this.relatedSkill = relatedSkill;
    }

    public Tool(ToolType type, FishingPoleType fishingPoleType, Skill relatedSkill) {
        super(ItemType.TOOL);
        this.type = type;
        this.fishingPoleType = fishingPoleType;
        this.relatedSkill = relatedSkill;
    }

    public Tool(ToolType type, int fixedEnergyCost, Skill relatedSkill) {
        super(ItemType.TOOL);
        this.type = type;
        this.relatedSkill = relatedSkill;
        this.fixedEnergyCost = fixedEnergyCost;
    }

    public ToolQuality getToolQuality() {
        return toolQuality;
    }

    public void setToolQuality(ToolQuality quality) {
        this.toolQuality = quality;
    }

    public Skill getRelatedSkill() {
        return relatedSkill;
    }

    public void setRelatedSkill(Skill relatedSkill) {
        this.relatedSkill = relatedSkill;
    }

    public int getEnergyCost() {
        return 0;
    }

    public void useTool(Direction direction) {

    }

//    public String toDisplayString() {
//        String formattedType = type.toString()
//                .toLowerCase()
//                .replace('_', ' ');
//        formattedType = Character.toUpperCase(formattedType.charAt(0)) + formattedType.substring(1);
//
//        String formattedQuality = (quality != null)
//                ? quality.toString().charAt(0) + quality.toString().substring(1).toLowerCase()
//                : null;
//
//        StringBuilder display = new StringBuilder(formattedType);
//        if (formattedQuality != null) {
//            display.append(" (").append(formattedQuality).append(")");
//        }
//
//        return display.toString();
//    }
//
//    @Override
//    public String toString() {
//        return toDisplayString();
//    }

}
