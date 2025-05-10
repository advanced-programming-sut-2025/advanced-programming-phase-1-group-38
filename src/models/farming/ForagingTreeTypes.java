package models.farming;

public enum ForagingTreeTypes {
    ACORNS("Acorns"),
    MAPLE_SEEDS("Maple Seeds"),
    PINE_CONES("Pine Cones"),
    MAHOGANY_SEEDS("Mahogany Seeds"),
    MUSHROOM_TREE_SEEDS("Mushroom Tree Seeds");

    private final String displayName;

    ForagingTreeTypes(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
