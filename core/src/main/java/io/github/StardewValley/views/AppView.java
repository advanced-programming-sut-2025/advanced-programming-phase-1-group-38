package io.github.StardewValley.views;

import io.github.StardewValley.models.App;
import io.github.StardewValley.models.enums.Menu;

import java.util.Scanner;

public class AppView {
    public void run() {
        Scanner scanner = new Scanner(System.in);
        do {
//            App.getCurrentMenu().checkCommand(scanner);
        }
        while (App.getCurrentMenu()!= Menu.EXIT_MENU);

    }
}
