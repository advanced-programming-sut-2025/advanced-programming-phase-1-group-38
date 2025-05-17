package views;

import controllers.LoginAndRegisterController;
import models.App;
import models.Result;
import models.User;
import models.enums.Commands.LoginAndRegisterCommands;
import models.enums.Gender;
import models.enums.Menu;

import java.util.Scanner;
import java.util.regex.Matcher;

public class LoginAndRegisterMenu implements AppMenu {

    private final LoginAndRegisterController controller = new LoginAndRegisterController();

    @Override
    public AppMenu checkCommand(Scanner scanner) {
        if (!scanner.hasNextLine()) return this;

        String input = scanner.nextLine().trim();
        Matcher matcher;

        if ((matcher = LoginAndRegisterCommands.REGISTER.getMatcher(input)) != null) {
            String username = matcher.group("username").trim();
            String password = matcher.group("password").trim();
            String confirm = matcher.group("confirm").trim();
            String nickname = matcher.group("nickname").trim();
            String email = matcher.group("email").trim();
            String genderStr = matcher.group("gender").trim();

            if (password.equalsIgnoreCase("random")) {
                Result generated = controller.randomPasswordGenerator();
                password = generated.message();
                confirm = password;
                System.out.println("Generated Password: " + password);
            }

            if (!password.equals(confirm)) {
                System.out.println("Passwords do not match.");
                return this;
            }

            Gender gender = Gender.fromString(genderStr);
            if (gender == null) {
                System.out.println("Invalid gender. Please use: MAN, WOMAN, RATHER_NOT_SAY.");
                return this;
            }

            Result result = controller.registerUser(username, password, nickname, email, gender);
            System.out.println(result.message());

            if (result.success()) {
                System.out.println(controller.askSecurityQuestions().message());
            }
            return this;
        }

        else if ((matcher = LoginAndRegisterCommands.LOGIN.getMatcher(input)) != null) {
            String username = matcher.group("username").trim();
            String password = matcher.group("password").trim();
            boolean stayLogged = matcher.group("stayLogged") != null;

            Result result = controller.login(username, password, stayLogged);
            System.out.println(result.message());

            if (result.success()) {
                App.setCurrentMenu(Menu.MAIN_MENU);
                return Menu.MAIN_MENU.getMenuInstance();
            }
            return this;
        }

        else if ((matcher = LoginAndRegisterCommands.FORGET_PASSWORD.getMatcher(input)) != null) {
            String username = matcher.group("username").trim();
            Result result = controller.forgotPassword(username);
            System.out.println(result.message());
            return this;
        }

        else if ((matcher = LoginAndRegisterCommands.PICK_QUESTION.getMatcher(input)) != null) {
            int questionNumber = Integer.parseInt(matcher.group("number").trim());
            String answer = matcher.group("answer").trim();
            String confirm = matcher.group("confirm").trim();

            User user = App.getLoggedInUser();
            if (user == null) {
                System.out.println("No user in session.");
                return this;
            }

            Result result = controller.pickSecurityQuestion(user, questionNumber, answer, confirm);
            System.out.println(result.message());
            return this;
        }

        else if ((matcher = LoginAndRegisterCommands.ANSWER.getMatcher(input)) != null) {
            String answer = matcher.group("answer").trim();
            Result result = controller.checkSecurityAnswer(answer);
            System.out.println(result.message());
            return this;
        }

        else if (LoginAndRegisterCommands.SHOW_CURRENT_MENU.getMatcher(input) != null) {
            System.out.println(controller.showCurrentMenu().message());
            return this;
        }

        else if (LoginAndRegisterCommands.MENU_EXIT.getMatcher(input) != null) {
            System.out.println(controller.exitMenu().message());
            return null; // instead of System.exit
        }

        else if ((matcher = LoginAndRegisterCommands.MENU_ENTER.getMatcher(input)) != null) {
            String menuName = matcher.group("menu").trim();
            if (menuName.equalsIgnoreCase("main")) {
                App.setCurrentMenu(Menu.MAIN_MENU);
                return Menu.MAIN_MENU.getMenuInstance();
            }
            System.out.println("Invalid menu name.");
            return this;
        }

        System.out.println("Invalid command.");
        return this;
    }
}