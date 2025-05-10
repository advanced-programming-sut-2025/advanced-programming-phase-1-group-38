package models.enums;

import java.util.regex.Matcher;

public interface Command {
    String getPattern();
    Matcher getMatcher(String input);
}
