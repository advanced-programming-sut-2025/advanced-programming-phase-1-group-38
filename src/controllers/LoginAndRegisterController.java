package controllers;

import models.App;
import models.Result;
import models.User;
import models.enums.Gender;
import models.enums.Menu;

public class LoginAndRegisterController {
    public Result registerUser(String username, String password, String email, Gender gender) {
    }

    public Result randomPasswordGenerator() {
    }

    public Result askSecurityQuestions() {
    }

    public Result pickSecurityQuestion(User user, int questionNumber, String answer , String answerConfirm) {
    }

    private Result sha256() {
    }

    private boolean isUsernameUnique(String username) {
    }

    private boolean isValidUsername(String username) {
    }

    private boolean isValidPassword(String password) {
    }

    private boolean isValidEmail(String email) {
    }

    public Result login(String username, String password) {
    }

    public Result forgotPassword(String username) {
    }

    public Result showUserSecurityQuestion(User user) {
    }

    private Result isAnswerValid(User user, String answer) {
    }

    public Result enterMenu() {
    }

    public Result exitMenu() {
    }

    public Result showCurrentMenu() {
    }

}