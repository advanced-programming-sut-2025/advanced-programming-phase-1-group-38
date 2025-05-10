package views;

import controllers.ProfileMenuController;

import java.util.Scanner;

public class ProfileMenu implements AppMenu{
    private final ProfileMenuController profileMenuController = new ProfileMenuController();

    @Override
    public AppMenu checkCommand(Scanner scanner) {
        return this;
    }
}
