package models.farming;

import models.Item;
import models.Tile;
import models.enums.Types.ItemType;
import models.enums.Seasons;

public class Crop extends Item {
    private final CropType type;
    private boolean isWatered = false;
    private boolean readyToHarvest = false;
    private int currentStage = 0;
    private int daysInCurrentStage = 0;
    private int unwateredDays = 0;
    private boolean isDead = false;

    public Crop(CropType type) {
        super(ItemType.CROP);
        this.type = type;
        if (type.isForage()) {
            this.readyToHarvest = true;
        }
    }

    private void growIfWatered() {
        if (type.isForage() || readyToHarvest || isDead || !isWatered) return;

        daysInCurrentStage++;

        if (daysInCurrentStage >= type.getGrowthStages().get(currentStage)) {
            currentStage++;
            daysInCurrentStage = 0;
        }

        if (currentStage >= type.getGrowthStages().size()) {
            readyToHarvest = true;
        }
    }

    public void cropNextDay() {
        if (isDead) return;

        if (!isWatered) {
            unwateredDays++;
            if (unwateredDays >= 2) {
                kill();
                return;
            }
        } else {
            unwateredDays = 0;
        }

        growIfWatered();
        isWatered = false;
    }


    public void harvest() {
        if (!isReadyToHarvest() || isDead) return;

        if (type.isOneTime() || type.isForage()) {
            readyToHarvest = false;
        } else {
            readyToHarvest = false;
            currentStage = type.getGrowthStages().size() - type.getRegrowthTime();
            daysInCurrentStage = 0;
        }
    }

    public boolean shouldRemoveAfterHarvest() {
        return type.isOneTime() || type.isForage();
    }

    public String getName() {
        return type.getName();
    }

    public CropType getCropType() {
        return type;
    }

    public boolean isWatered() {
        return isWatered;
    }

    public void setWatered(boolean watered) {
        this.isWatered = watered;
    }

    public boolean isReadyToHarvest() {
        return type.isForage() || readyToHarvest;
    }

    public int getCurrentStage() {
        return currentStage;
    }

    public int getDaysInCurrentStage() {
        return daysInCurrentStage;
    }

    public boolean isDead() {
        return isDead;
    }

    public void kill() {
        this.isDead = true;
        this.readyToHarvest = false;
    }
}
