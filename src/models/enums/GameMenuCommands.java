package models.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameMenuCommands implements Command {
    MENU_ENTER(""),
    MENU_EXIT(""),
    SHOW_CURRENT_MENU(""),
    NEW_GAME(""),
    GAME_MAP(""),
    LOAD_GAME("")
    ;

    private final String pattern;
    GameMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        if(matcher.matches()) {
            return matcher;
        }
        return null;
    }
}