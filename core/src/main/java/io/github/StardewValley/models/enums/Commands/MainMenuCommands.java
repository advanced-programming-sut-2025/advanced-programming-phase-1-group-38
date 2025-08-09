package io.github.StardewValley.models.enums.Commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MainMenuCommands implements Command {

    LOGOUT("^\\s*user\\s+logout\\s*$"),
    PROFILE_MENU_ENTER("^\\s*enter\\s+profile\\s+menu\\s*$"),
    GAME_MENU_ENTER("^\\s*enter\\s+game\\s+menu\\s*$"),
    MENU_EXIT("^\\s*menu\\s+exit\\s*$"),
    SHOW_CURRENT_MENU("^\\s*show\\s+current\\s+menu\\s*$");

    private final Pattern pattern;

    MainMenuCommands(String regex) {
        this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }

    @Override
    public String getPattern() {
        return pattern.pattern();
    }

    @Override
    public Matcher getMatcher(String input) {
        Matcher matcher = pattern.matcher(input);
        return matcher.matches() ? matcher : null;
    }
}