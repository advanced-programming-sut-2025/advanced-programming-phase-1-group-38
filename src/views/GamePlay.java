package views;

import controllers.GamePlayController;
import models.Game;

import java.util.Scanner;

public class GamePlay implements AppMenu {
    private final GamePlayController gamePlayController;
    public GamePlay(Game game) {
        this.gamePlayController = new GamePlayController(game);
    }

    @Override
    public AppMenu checkCommand(Scanner scanner) {
        return this;
    }
}
