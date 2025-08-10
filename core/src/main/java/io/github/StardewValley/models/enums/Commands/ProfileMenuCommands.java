package io.github.StardewValley.models.enums.Commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ProfileMenuCommands implements Command {
    CHANGE_USERNAME("^change\\s+username\\s+-u\\s+(?<username>\\S+)$"),
    CHANGE_NICKNAME("^change\\s+nickname\\s+-n\\s+(?<nickname>\\S+)$"),
    CHANGE_EMAIL("^change\\s+email\\s+-e\\s+(?<email>\\S+)$"),
    CHANGE_PASSWORD("^change\\s+password\\s+-o\\s+(?<old>\\S+)\\s+-n\\s+(?<new>\\S+)$"),
    USER_INFO("^user\\s+info"),
    SHOW_CURRENT_MENU("^show\\s+current\\s+menu$"),
    MENU_EXIT("^menu\\s+exit$"),
    MENU_ENTER("^menu\\s+enter$")
    ;

    private final String pattern;
    ProfileMenuCommands(String pattern) {
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
