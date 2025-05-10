package models.enums;

public enum Skill {
    FARMING(5),
    MINING(10),
    FORAGING(10),
    FISHING(5);

    private final int increasePerAction;
    Skill(int increasePerAction) {
        this.increasePerAction = increasePerAction;
    }
    public int getIncreasePerAction() {
        return increasePerAction;
    }
}
