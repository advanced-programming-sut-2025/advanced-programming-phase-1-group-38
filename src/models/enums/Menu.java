package models.enums;

import views.*;

import java.util.Scanner;

public enum Menu {
    LOGIN_AND_REGISTER_MENU("Login", new LoginMenu()),
    MAIN_MENU("Main", new MainMenu()),
    PROFILE_MENU("Profile", new ProfileMenu()),
    GAME_MENU("Game", new GameMenu()),
    EXIT_MENU("Exit", new ExitMenu());

    private final String displayName;
    private final AppMenu menu;

    Menu(String displayName, AppMenu menu) {
        this.displayName = displayName;
        this.menu = menu;
    }

    public void checkCommand(Scanner scanner) {
        menu.checkCommand(scanner);
    }

    public AppMenu getMenuInstance() {
        return menu;
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