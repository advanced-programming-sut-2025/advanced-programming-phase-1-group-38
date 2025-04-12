package views;

import controllers.GamePlayController;

import java.util.Scanner;

public class GamePlay implements AppMenu {
    private final GamePlayController gamePlayController = new GamePlayController();

    @Override
    public AppMenu checkCommand(Scanner scanner) {
        return this;
    }
}
