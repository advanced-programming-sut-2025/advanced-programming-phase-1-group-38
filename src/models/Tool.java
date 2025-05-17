package models;

import models.enums.*;
import models.enums.Types.FishingPoleType;
import models.enums.Types.ItemType;
import models.enums.Types.ToolType;

public abstract class Tool extends Item {
    protected ToolType type;
    protected ToolQuality toolQuality;
    protected Skill relatedSkill;
    protected Integer fixedEnergyCost;
    protected FishingPoleType fishingPoleType;
    protected TrashCanQuality trashCanQuality;

    public Tool(ToolType type, ToolQuality quality, Skill relatedSkill) {
        super(ItemType.TOOL);
        this.type = type;
        this.toolQuality = quality;
        this.relatedSkill = relatedSkill;
    }

    public Tool(ToolType type, TrashCanQuality quality, Skill relatedSkill) {
        super(ItemType.TOOL);
        this.type = type;
        this.trashCanQuality = quality;
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

    public ToolType getToolType() {
        return type;
    }

    public ToolQuality getToolQuality() {
        return toolQuality;
    }

    public void setToolQuality(ToolQuality quality) {
        this.toolQuality = quality;
    }


    public FishingPoleType getFishingPoleType() {
        return fishingPoleType;
    }

    public void setFishingPoleType(FishingPoleType fishingPoleType) {
        this.fishingPoleType = fishingPoleType;
    }

    public TrashCanQuality getTrashCanQuality() {
        return trashCanQuality;
    }

    public void setTrashCanQuality(TrashCanQuality trashCanQuality) {
        this.trashCanQuality = trashCanQuality;
    }

    public Skill getRelatedSkill() {
        return relatedSkill;
    }

    public abstract int getEnergyCost(Player player);

    public abstract Result useTool(Game game, Direction direction);

    public int getModifiedEnergyCost(Player player, Weather weather) {
        int base = getEnergyCost(player);

        return switch (weather) {
            case RAINY, STORM -> (int) Math.ceil(base * 1.5);
            case SNOW  -> base * 2;
            default    -> base;
        };
    }

    protected String formatEnumName(Enum<?> e) {
        String[] parts = e.name().toLowerCase().split("_");
        StringBuilder formatted = new StringBuilder();
        for (String part : parts) {
            formatted.append(Character.toUpperCase(part.charAt(0)))
                .append(part.substring(1))
                .append(" ");
        }
        return formatted.toString().trim();
    }

    @Override
    public abstract String toString();

}
