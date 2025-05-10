package models;

import models.enums.Menu;
import java.util.ArrayList;

public class App {
    private static final ArrayList<User> users = new ArrayList<>();
    private static final ArrayList<Game> games = new ArrayList<>();
    private static User loggedInUser = null;
    private static Menu currentMenu = Menu.LOGIN_AND_REGISTER_MENU;

    public static User getLoggedInUser() {
        return loggedInUser;
    }
    public static void setLoggedInUser(User loggedInUser) {
        App.loggedInUser = loggedInUser;
    }
    public static Menu getCurrentMenu() {
        return currentMenu;
    }
    public static void setCurrentMenu(Menu currentMenu) {
        App.currentMenu = currentMenu;
    }
}
