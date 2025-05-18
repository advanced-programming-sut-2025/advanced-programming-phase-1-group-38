package views;

import controllers.GamePlayController;
import models.App;
import models.Result;
import models.enums.Commands.GamePlayCommands;
import models.enums.Menu;
import java.util.Scanner;
import java.util.regex.Matcher;

public class GamePlay implements AppMenu {
    private final GamePlayController controller;

    public GamePlay() {
        this.controller = new GamePlayController(App.getCurrentGame());
    }

    @Override
    public AppMenu checkCommand(Scanner scanner) {
        if (!scanner.hasNextLine()) return this;
        String input = scanner.nextLine().trim();
        Matcher m;

        if ((m = GamePlayCommands.WALK.getMatcher(input)) != null) {
            String pos = m.group("position");
            System.out.println(controller.respondForWalkRequest(pos));
            return this;
        }
        else if ((m = GamePlayCommands.WALK_CONFIRM.getMatcher(input)) != null) {
            String ans = m.group("answer");
            System.out.println(controller.confirmWalk(ans));
            return this;
        }
        else if ((m = GamePlayCommands.PRINT_MAP.getMatcher(input)) != null) {
            String pos  = m.group("position");
            String size = m.group("size");
            Result res = controller.printMap(pos, size);
            System.out.println(res.message());
            return this;
        }
        else {
            System.out.println("Invalid command.");
            return this;
        }
    }
}
