package models.enums.Commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GamePlayCommands implements Command {
    EXIT_GAME(""),
    FORCE_TERMINATE(""),
    NEXT_TURN(""),
    WALK("^\\s*walk\\s+-l\\s+(?<position>\\d+\\s*,\\s*\\d+)\\s*$"),
    WALK_CONFIRM("^\\s*walk\\s+confirm\\s+(?<answer>[yn])\\s*$"),

    // added

    TOOLS_EQUIP("^\\s*tools\\s+equip\\s+(?<tool_name>.+?)\\s*$"),
    TOOLS_SHOW_CURRENT("^\\s*tools\\s+show\\s+current\\s*$"),
    TOOLS_SHOW_AVAILABLE("^\\s*tools\\s+show\\s+available\\s*$"),
    TOOLS_UPGRADE("^\\s*tools\\s+upgrade\\s+(?<tools_name>.+?)\\s*$"),
    TOOLS_USE("^\\s*tools\\s+-d\\s+(?<direction>.+?)\\s*$"),
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

