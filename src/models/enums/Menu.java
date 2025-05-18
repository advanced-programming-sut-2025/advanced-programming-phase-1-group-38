package models.enums;

import views.*;

import java.util.Scanner;

public enum Menu {
    LOGIN_AND_REGISTER_MENU("Login", new LoginMenu()),
    MAIN_MENU("Main", new MainMenu()),
    PROFILE_MENU("Profile", new ProfileMenu()),
    GAME_MENU("Game", new GameMenu()),
    EXIT_MENU("Exit", new ExitMenu()),
    GAMEPLAY_MENU("Gameplay", null);

    private final String displayName;
    private AppMenu menuInstance;

    Menu(String displayName, AppMenu menuInstance) {
        this.displayName = displayName;
        this.menuInstance = menuInstance;
    }

    public AppMenu getMenuInstance() {
        return menuInstance;
    }

    public void setMenuInstance(AppMenu instance) {
        this.menuInstance = instance;
    }

    public String getDisplayName() {
        return displayName;
    }

    public AppMenu checkCommand(Scanner scanner) {
        return menuInstance.checkCommand(scanner);
    }

    public static Menu getMenuFromDisplayName(String name) {
        for (Menu menu : values()) {
            if (menu.displayName.equalsIgnoreCase(name.trim()))
                return menu;
        }
        return null;
    }
}