package io.github.StardewValley.models.Relationship;

public enum FriendshipLevel {
    STRANGER(0),
    FRIEND(1),
    CLOSE_FRIEND(2),
    BEST_FRIEND(3),
    MARRIED(4);

    int level;

    FriendshipLevel(int level) {
        this.level = level;
    }
}
