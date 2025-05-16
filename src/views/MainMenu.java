package views;

import controllers.MainMenuController;
import models.App;
import models.enums.Commands.MainMenuCommands;
import models.enums.Menu;

import java.util.Scanner;

public class MainMenu implements AppMenu {
    private final MainMenuController controller = new MainMenuController();

    @Override
    public AppMenu checkCommand(Scanner scanner) {
        if (!scanner.hasNextLine()) return this;

        String input = scanner.nextLine().trim();

        if ((MainMenuCommands.PROFILE_MENU_ENTER.getMatcher(input)) != null) {
            System.out.println(controller.profileMenuEnter());
            App.setCurrentMenu(Menu.PROFILE_MENU);
            return App.getCurrentMenu().getMenuInstance();
        }

        else if ((MainMenuCommands.GAME_MENU_ENTER.getMatcher(input)) != null) {
            System.out.println(controller.gameMenuEnter());
            App.setCurrentMenu(Menu.GAME_MENU);
            return App.getCurrentMenu().getMenuInstance();
        }

        else if ((MainMenuCommands.LOGOUT.getMatcher(input)) != null) {
            System.out.println(controller.logout());
            App.setCurrentMenu(Menu.LOGIN_AND_REGISTER_MENU);
            return App.getCurrentMenu().getMenuInstance();
        }

        else if ((MainMenuCommands.MENU_EXIT.getMatcher(input)) != null) {
            System.out.println(controller.exitMenu());
            App.setCurrentMenu(Menu.LOGIN_AND_REGISTER_MENU);
            return App.getCurrentMenu().getMenuInstance();
        }

        else if ((MainMenuCommands.SHOW_CURRENT_MENU.getMatcher(input)) != null) {
            System.out.println(controller.showCurrentMenu());
            return this;
        }

        // === invalid command
        else {
            System.out.println("Invalid command.");
            return this;
        }
    }
}