package controllers;

import models.App;
import models.Result;
import models.enums.Menu;

public class MainMenuController {

    public Result profileMenuEnter() {
        App.setCurrentMenu(Menu.PROFILE_MENU);
        return new Result(true, "Now you are in Profile Menu.");
    }

    public Result gameMenuEnter() {
        App.setCurrentMenu(Menu.GAME_MENU);
        return new Result(true, "Now you are in Game Menu.");
    }

    public Result exitMenu() {
        App.setCurrentMenu(Menu.LOGIN_AND_REGISTER_MENU);
        return new Result(true, "You are now in the Login Menu.");
    }

    public Result showCurrentMenu() {
        Menu current = App.getCurrentMenu();
        return new Result(true, "Current Menu: " + current.getDisplayName());
    }

    public Result logout() {
        App.logout();
        App.setCurrentMenu(Menu.LOGIN_AND_REGISTER_MENU);
        return new Result(true, "You logged out successfully.");
    }
}