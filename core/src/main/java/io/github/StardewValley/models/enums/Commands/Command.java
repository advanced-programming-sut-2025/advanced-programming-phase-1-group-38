package io.github.StardewValley.models.enums.Commands;

import java.util.regex.Matcher;

public interface Command {
    String getPattern();
    Matcher getMatcher(String input);
}
