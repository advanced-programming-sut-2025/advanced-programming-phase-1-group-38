package views;

import controllers.MainMenuController;

import java.util.Scanner;

public class MainMenu implements AppMenu {
    private final MainMenuController mainMenuController = new MainMenuController();

    @Override
    public AppMenu checkCommand(Scanner scanner) {
        return this;
    }
}
