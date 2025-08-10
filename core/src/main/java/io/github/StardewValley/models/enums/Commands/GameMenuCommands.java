package io.github.StardewValley.models.enums.Commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameMenuCommands implements Command {

    GAME_NEW("^game\\s+new\\s+-u\\s+(?<usernames>(\\w+\\s*){1,3})$"),
    GAME_CHOOSE_MAP("^game\\s+map\\s+(?<mapNumber>\\d+)$"),
    LOAD_GAME("^load\\s+game$"),
    MENU_ENTER("^menu\\s+enter\\s+(?<menu>\\S+)$"),
    MENU_EXIT("^menu\\s+exit$"),
    SHOW_CURRENT_MENU("^show\\s+current\\s+menu$");

    private final Pattern pattern;

    GameMenuCommands(String regex) {
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