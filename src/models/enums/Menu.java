package models.enums;

import views.*;

import java.util.Scanner;

public enum Menu {
    LOGIN_AND_REGISTER_MENU(new LoginAndRegisterMenu()),
    MAIN_MENU(new MainMenu()),
    PROFILE_MENU(new ProfileMenu()),
    GAME_MENU(new GameMenu()),
    EXIT_MENU(new ExitMenu());

    private final AppMenu menu;

    Menu(AppMenu menu) {
        this.menu = menu;
    }

    public void checkCommand(Scanner scanner) {
        this.menu.checkCommand(scanner);
    }
}
