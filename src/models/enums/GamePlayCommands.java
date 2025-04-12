package models.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GamePlayCommands implements Command {
    EXIT_GAME(""),
    FORCE_TERMINATE(""),
    NEXT_TURN("")
    ;

    private final String pattern;
    GamePlayCommands(String pattern) {
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

