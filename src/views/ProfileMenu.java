package views;

import controllers.ProfileMenuController;
import models.App;
import models.Result;
import models.enums.Commands.ProfileMenuCommands;
import models.enums.Menu;

import java.util.Scanner;
import java.util.regex.Matcher;

public class ProfileMenu implements AppMenu {
    private final ProfileMenuController controller = new ProfileMenuController();

    @Override
    public AppMenu checkCommand(Scanner scanner) {
        if (!scanner.hasNextLine()) return this;
        String input = scanner.nextLine().trim();
        Matcher matcher;

        if ((matcher = ProfileMenuCommands.CHANGE_USERNAME.getMatcher(input)) != null) {
            String username = matcher.group("username");
            Result result = controller.changeUsername(username);
            System.out.println(result.message());
            return this;

        } else if ((matcher = ProfileMenuCommands.CHANGE_NICKNAME.getMatcher(input)) != null) {
            String nickname = matcher.group("nickname");
            Result result = controller.changeNickname(nickname);
            System.out.println(result.message());
            return this;

        } else if ((matcher = ProfileMenuCommands.CHANGE_EMAIL.getMatcher(input)) != null) {
            String email = matcher.group("email");
            Result result = controller.changeEmail(email);
            System.out.println(result.message());
            return this;

        } else if ((matcher = ProfileMenuCommands.CHANGE_PASSWORD.getMatcher(input)) != null) {
            String oldPass = matcher.group("old");
            String newPass = matcher.group("new");
            Result result = controller.changePassword(oldPass, newPass);
            System.out.println(result.message());
            return this;

        } else if (ProfileMenuCommands.USER_INFO.getMatcher(input) != null) {
            Result result = controller.showUserInfo();
            System.out.println(result.message());
            return this;

        } else if (ProfileMenuCommands.SHOW_CURRENT_MENU.getMatcher(input) != null) {
            Result result = controller.showCurrentMenu();
            System.out.println(result.message());
            return this;

        } else if (ProfileMenuCommands.MENU_EXIT.getMatcher(input) != null) {
            Result result = controller.exitMenu();
            System.out.println(result.message());
            return Menu.MAIN_MENU.getMenuInstance();
        }

        System.out.println("Invalid command.");
        return this;
    }
}
