package models.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LoginAndRegisterCommands implements Command {
    REGISTER(""),
    PICK_QUESTION(""),
    LOGIN(""),
    FORGET_PASSWORD(""),
    ANSWER(""),
    MENU_ENTER(""),
    MENU_EXIT(""),
    SHOW_CURRENT_MENU("")
    ;

    private final String pattern;
    LoginAndRegisterCommands(String pattern) {
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
