package models;

import models.enums.Types.NPCType;

public class NPCHome {
    private final NPCType owner;
    private final int cornerX;
    private final int cornerY;

    public NPCHome(NPCType owner) {
        this.owner = owner;
        this.cornerX = owner.getHouseCornerX();
        this.cornerY = owner.getHouseCornerY();
    }

    public NPCType getOwner()    { return owner; }
    public int     getCornerX()  { return cornerX; }
    public int     getCornerY()  { return cornerY; }
}
