package models.enums.Commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LoginAndRegisterCommands implements Command {
    REGISTER("^register\\s+-u\\s+(?<username>\\S+)\\s+-p\\s+(?<password>\\S+)\\s+(?<confirm>\\S+)\\s+" +
            "-n\\s+(?<nickname>\\S+)\\s+-e\\s+(?<email>\\S+)\\s+-g\\s+(?<gender>\\S+)$"),
    PICK_QUESTION("^pick\\s+question\\s+-q\\s+(?<number>\\d+)\\s+-a\\s+(?<answer>\\S+)\\s+-c\\s+(?<confirm>\\S+)$"),    LOGIN("^login\\s+-u\\s+(?<username>\\S+)\\s+-p\\s+(?<password>\\S+)$"),
    FORGET_PASSWORD("^forget\\s+password\\s+-u\\s+(?<username>\\S+)$"),
    ANSWER("^answer\\s+-a\\s+(?<answer>\\S+)$"),
    MENU_ENTER("^menu\\s+enter\\s+(?<menu>\\S+)$"),
    MENU_EXIT("^menu\\s+exit$"),
    SHOW_CURRENT_MENU("^show\\s+current\\s+menu$"),
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
