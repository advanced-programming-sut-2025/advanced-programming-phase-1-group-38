package views;

import controllers.LoginAndRegisterController;

import java.util.Scanner;

public class LoginAndRegisterMenu implements AppMenu {
    private final LoginAndRegisterController loginAndRegisterController = new LoginAndRegisterController();

    @Override
    public AppMenu checkCommand(Scanner scanner) {
        return this;
    }
}
