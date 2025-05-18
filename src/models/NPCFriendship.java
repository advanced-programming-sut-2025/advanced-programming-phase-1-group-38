package models;

public class NPCFriendship {
    private final NPC npc;
    private final Player player;
    private int friendshipLevel = 0;
    private int friendshipPoints = 0;

    public NPCFriendship(NPC npc, Player player) {
        this.npc = npc;
        this.player = player;
    }

    public NPC getNpc() {
        return npc;
    }

    public Player getPlayer() {
        return player;
    }

    public int getFriendshipLevel() {
        return friendshipLevel;
    }

    public int getFriendshipPoints() {
        return friendshipPoints;
    }

    public void addFriendshipPoints(int points) {
        if (this.friendshipPoints + points > 799) {
            this.friendshipPoints = 799;
        }
        else this.friendshipPoints += points;

        if (this.friendshipPoints < 200) {
            this.friendshipLevel = 0;
        }
        else if (this.friendshipPoints < 400) {
            this.friendshipLevel = 1;
        }
        else if (this.friendshipPoints < 600) {
            this.friendshipLevel = 2;
        }
        else if (this.friendshipPoints < 800) {
            this.friendshipLevel = 3;
        }
    }

    public void upgradeLevel() {
        switch (this.friendshipLevel) {
            case 0 -> this.friendshipPoints = 200;
            case 1 -> this.friendshipPoints = 400;
            case 2 -> this.friendshipPoints = 600;
        }

        if (this.friendshipLevel < 3) {
            this.friendshipLevel++;
        }
    }

    @Override
    public String toString() {
        return npc.getType().toString() + ":\n" +
            "Friendship Level: " + friendshipLevel + "\n" +
            "Friendship XP: " + friendshipPoints + "\n";
    }
}
