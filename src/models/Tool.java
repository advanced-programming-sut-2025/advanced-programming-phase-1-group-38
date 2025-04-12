package models;

import models.enums.*;

public class Tool extends Item {
    private ToolType type;
    private ToolQuality quality;
    private Skill relatedSkill;
    private Integer fixedEnergyCost;
    private FishingPoleType fishingPoleType;

    public Tool(ToolType type, ToolQuality quality, Skill relatedSkill) {
        super(ItemType.TOOL);
        this.type = type;
        this.quality = quality;
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
    public ToolQuality getQuality() {
        return quality;
    }

    public void setQuality(ToolQuality quality) {
        this.quality = quality;
    }

    public Skill getRelatedSkill() {
        return relatedSkill;
    }

    public void setRelatedSkill(Skill relatedSkill) {
        this.relatedSkill = relatedSkill;
    }

    public int getEnergyCost() {
        if(fixedEnergyCost != null) {
            return fixedEnergyCost;
        }
        return quality != null ? quality.getEnergyCost() : 0;
    }

    public void useTool(Direction direction) {

    }
}
