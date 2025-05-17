package views;

import controllers.GameMenuController;
import models.Result;
import models.enums.Commands.GameMenuCommands;
import models.enums.Menu;
import models.App;

import java.util.Scanner;
import java.util.regex.Matcher;

public class GameMenu implements AppMenu {
    private final GameMenuController controller = new GameMenuController();

    @Override
    public AppMenu checkCommand(Scanner scanner) {
        if (!scanner.hasNextLine()) return this;

        String input = scanner.nextLine().trim();
        Matcher matcher;

        if ((matcher = GameMenuCommands.NEW_GAME.getMatcher(input)) != null) {
            String usernames = matcher.group("usernames");
            Result result = controller.newGame(usernames);
            System.out.println(result.message());
            return this;
        }

        else if ((matcher = GameMenuCommands.GAME_MAP.getMatcher(input)) != null) {
            int mapNumber = Integer.parseInt(matcher.group("number"));
            Result result = controller.chooseMap(mapNumber);
            System.out.println(result.message());
            return this;
        }

        else if (GameMenuCommands.LOAD_GAME.getMatcher(input) != null) {
            Result result = controller.loadGame();
            System.out.println(result.message());
            return this;
        }

        else if (GameMenuCommands.SHOW_CURRENT_MENU.getMatcher(input) != null) {
            System.out.println(controller.showCurrentMenu().message());
            return this;
        }

        else if (GameMenuCommands.MENU_EXIT.getMatcher(input) != null) {
            System.out.println(controller.exitMenu().message());
            App.setCurrentMenu(Menu.MAIN_MENU);
            return Menu.MAIN_MENU.getMenuInstance();
        }

        else {
            System.out.println("Invalid command.");
            return this;
        }
    }
}