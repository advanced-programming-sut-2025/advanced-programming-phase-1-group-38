package io.github.StardewValley.models.enums;

public enum Menu {
    LOGIN_MENU("Login"),
    AUTH_MENU("Auth"),
    REGISTER_MENU("Register"),
    MAIN_MENU("Main"),
    PROFILE_MENU("Profile"),
    GAME_MENU("Game"),
    EXIT_MENU("Exit"),
    GAMEPLAY_MENU("Gameplay");

    private final String displayName;

    Menu(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Menu getMenuFromDisplayName(String name) {
        for (Menu menu : values()) {
            if (menu.displayName.equalsIgnoreCase(name.trim()))
                return menu;
        }
        return null;
    }
}
