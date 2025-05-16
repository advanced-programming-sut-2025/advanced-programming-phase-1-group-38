package models.enums.Commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ProfileMenuCommands implements Command {
    CHANGE_USERNAME(""),
    CHANGE_NICKNAME(""),
    CHANGE_EMAIL(""),
    CHANGE_PASSWORD(""),
    USER_INFO(""),
    MENU_ENTER(""),
    MENU_EXIT(""),
    SHOW_CURRENT_MENU("")
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
