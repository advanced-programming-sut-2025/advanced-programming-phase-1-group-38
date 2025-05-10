package views;

import controllers.GameMenuController;

import java.util.Scanner;

public class GameMenu implements AppMenu{
    private final GameMenuController GameMenuController = new GameMenuController();

    @Override
    public AppMenu checkCommand(Scanner scanner) {
        return this;
    }
}