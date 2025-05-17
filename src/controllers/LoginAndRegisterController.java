package controllers;

import models.App;
import models.Result;
import models.User;
import models.enums.Gender;
import models.enums.Menu;

import java.util.Random;

import static models.enums.Commands.LoginValidationPatterns.*; // ✅ اینجا جای درستشه

public class LoginAndRegisterController {

    public Result registerUser(String username, String password, String nickname, String email, Gender gender) {
        if (!VALID_USERNAME.matches(username))
            return new Result(false, "Invalid username format.");

        if (!VALID_PASSWORD.matches(password))
            return new Result(false, "Invalid password format.");

        if (!VALID_EMAIL.matches(email))
            return new Result(false, "Invalid email format.");

        if (App.usernameExists(username))
            return new Result(false, "Username is already taken.");

        if (App.emailExists(email))
            return new Result(false, "Email is already registered.");

        User user = new User(username, password, nickname, email, gender);
        App.addUser(user);
        App.setLoggedInUser(user);
        return new Result(true, "User registered successfully!");
    }

    public Result login(String username, String password, boolean stayLogged) {
        User user = App.getUserByUsername(username);
        if (user == null) {
            return new Result(false, "Username not found.");
        }
        if (!user.getPassword().equals(password)) {
            return new Result(false, "Incorrect password.");
        }

        App.setLoggedInUser(user);
        if (stayLogged) {
            System.out.println("(Stay Logged In: ON) - Future persistence logic can go here.");
        }

        return new Result(true, "Login successful. Welcome, " + user.getUsername() + "!");
    }

    public Result randomPasswordGenerator() {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder builder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            builder.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }

        return new Result(true, builder.toString());
    }

    public Result askSecurityQuestions() {
        StringBuilder sb = new StringBuilder("Select a security question:\n");
        sb.append("1. What is your favorite color?\n");
        sb.append("2. What is your mother’s maiden name?\n");
        sb.append("3. What was your first pet’s name?\n");
        return new Result(true, sb.toString());
    }

    public Result pickSecurityQuestion(User user, int questionNumber, String answer, String answerConfirm) {
        if (!answer.equals(answerConfirm))
            return new Result(false, "Answers do not match.");

        String question;
        switch (questionNumber) {
            case 1 -> question = "What is your favorite color?";
            case 2 -> question = "What is your mother’s maiden name?";
            case 3 -> question = "What was your first pet’s name?";
            default -> {
                return new Result(false, "Invalid question number.");
            }
        }

        user.setSecurityQuestion(question, answer);
        return new Result(true, "Security question saved.");
    }

    public Result forgotPassword(String username) {
        User user = App.getUserByUsername(username);
        if (user == null)
            return new Result(false, "No such user.");

        App.setLoggedInUser(user);
        return showUserSecurityQuestion(user);
    }

    public Result showUserSecurityQuestion(User user) {
        return new Result(true, user.getSecurityQuestion().getQuestion());
    }

    private Result isAnswerValid(User user, String answer) {
        if (user.getSecurityQuestion() == null)
            return new Result(false, "No security question set.");

        if (!user.getSecurityQuestion().getAnswer().equalsIgnoreCase(answer.trim()))
            return new Result(false, "Incorrect answer.");

        return new Result(true, "Answer is correct. You can now set a new password.");
    }

    public Result checkSecurityAnswer(String answer) {
        User user = App.getLoggedInUser();
        if (user == null)
            return new Result(false, "No user is currently selected for recovery.");
        return isAnswerValid(user, answer);
    }

    public Result enterMenu() {
        App.setCurrentMenu(Menu.LOGIN_AND_REGISTER_MENU);
        return new Result(true, "Entered Login & Register Menu.");
    }

    public Result exitMenu() {
        App.setCurrentMenu(Menu.EXIT_MENU);
        return new Result(true, "Exited to Exit Menu.");
    }

    public Result showCurrentMenu() {
        return new Result(true, "Current Menu: " + App.getCurrentMenu().getDisplayName());
    }
}