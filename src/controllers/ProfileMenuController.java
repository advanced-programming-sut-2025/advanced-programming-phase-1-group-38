package controllers;

import models.App;
import models.Result;
import models.User;
import models.enums.Menu;

import static models.enums.Commands.LoginValidationPatterns.*;

public class ProfileMenuController {

    public Result changeUsername(String newUsername) {
        if (!VALID_USERNAME.matches(newUsername))
            return new Result(false, "Invalid username format.");

        if (App.usernameExists(newUsername))
            return new Result(false, "Username is already taken.");

        User user = App.getLoggedInUser();
        if (user == null)
            return new Result(false, "No user logged in.");

        if (user.getUsername().equalsIgnoreCase(newUsername))
            return new Result(false, "New username cannot be the same as the current one.");

        user.setUsername(newUsername);
        return new Result(true, "Username changed successfully to " + newUsername + ".");
    }

    public Result changeNickname(String newNickname) {
        User user = App.getLoggedInUser();
        if (user == null)
            return new Result(false, "No user logged in.");

        user.setNickname(newNickname);
        return new Result(true, "Nickname changed successfully to " + newNickname + ".");
    }

    public Result changeEmail(String newEmail) {
        if (!VALID_EMAIL.matches(newEmail))
            return new Result(false, "Invalid email format.");

        if (App.emailExists(newEmail))
            return new Result(false, "Email is already in use.");

        User user = App.getLoggedInUser();
        if (user == null)
            return new Result(false, "No user logged in.");

        user.setEmail(newEmail);
        return new Result(true, "Email changed successfully to " + newEmail + ".");
    }

    public Result changePassword(String oldPassword, String newPassword) {
        User user = App.getLoggedInUser();
        if (user == null)
            return new Result(false, "No user logged in.");

        if (!user.getPassword().equals(oldPassword))
            return new Result(false, "Old password does not match.");

        if (!VALID_PASSWORD.matches(newPassword))
            return new Result(false, "New password does not meet requirements.");

        user.setPassword(newPassword);
        return new Result(true, "Password changed successfully.");
    }

    public Result showUserInfo() {
        User user = App.getLoggedInUser();
        if (user == null)
            return new Result(false, "No user is currently logged in.");

        return new Result(true, user.getFormattedInfo());
    }

    public Result enterMenu() {
        App.setCurrentMenu(Menu.PROFILE_MENU);
        return new Result(true, "Entered Profile Menu.");
    }

    public Result exitMenu() {
        App.setCurrentMenu(Menu.MAIN_MENU);
        return new Result(true, "Exited to Main Menu.");
    }

    public Result showCurrentMenu() {
        return new Result(true, "Current Menu: " + App.getCurrentMenu().getDisplayName());
    }
}