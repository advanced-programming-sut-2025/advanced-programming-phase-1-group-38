package models;

import models.enums.Types.BackpackType;
import models.inventory.Backpack;

public class Player {
    private Position position;
    private Backpack backpack;
    private Tool currentTool;

    public Player(Position position, Backpack backpack) {
        this.position = position;
        this.backpack = new Backpack(BackpackType.INITIAL);
        this.currentTool = null;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Backpack getBackpack() {
        return backpack;
    }

    public void setBackpack(Backpack backpack) {
        this.backpack = backpack;
    }

    public Tool getCurrentTool() {
        return currentTool;
    }
    public void setCurrentTool(Tool currentTool) {
        this.currentTool = currentTool;
    }
}
