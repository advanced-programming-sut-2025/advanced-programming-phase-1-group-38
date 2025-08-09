package io.github.StardewValley.models.enums.Commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GamePlayCommands implements Command {

    EXIT_GAME("^\\s*exit\\s+game\\s*$"),
    FORCE_TERMINATE("^\\s*force\\s+terminate\\s*$"),
    NEXT_TURN("^\\s*next\\s+turn\\s*$"),

    CHEAT_ADVANCE_TIME("^\\s*cheat\\s+advance\\s+time\\s+(?<hours>\\d+)h\\s*$"),
    CHEAT_ADVANCE_DATE("^\\s*cheat\\s+advance\\s+date\\s+(?<days>\\d+)d\\s*$"),

    WALK("^\\s*walk\\s+-l\\s+(?<position>\\d+\\s*,\\s*\\d+)\\s*$"),
    WALK_CONFIRM("^\\s*walk\\s+confirm\\s+(?<answer>[yn])\\s*$"),

    PRINT_MAP("^\\s*print\\s+map\\s+-l\\s+(?<position>\\d+,\\d+)\\s+-s\\s+(?<size>\\d+)\\s*$"),

    TOOLS_EQUIP("^\\s*tools\\s+equip\\s+(?<tool_name>.+?)\\s*$"),
    TOOLS_SHOW_CURRENT("^\\s*tools\\s+show\\s+current\\s*$"),
    TOOLS_SHOW_AVAILABLE("^\\s*tools\\s+show\\s+available\\s*$"),
    TOOLS_UPGRADE("^\\s*tools\\s+upgrade\\s+(?<tools_name>.+?)\\s*$"),
    TOOLS_USE("^\\s*tools\\s+-d\\s+(?<direction>.+?)\\s*$"),

    PLANT("^\\s*plant\\s+(?<seed>.+?)\\s+-d\\s+(?<direction>.+?)\\s*$"),
    FERTILIZE("^\\s*fertilize\\s+(?<fertilizer>.+?)\\s+-d\\s+(?<direction>.+?)\\s*$"),
    SHOW_PLANT("^\\s*show\\s+plant\\s+-l\\s+(?<x>\\d+),(?<y>\\d+)\\s*$"),
    HOWMUCH_WATER("^\\s*how\\s+much\\s+water\\s*$"),

    INVENTORY_SHOW("^\\s*inventory\\s+show\\s*$"),
    THROW_ITEM_TO_TRASH("^\\s*inventory\\s+trash\\s+(?<itemName>.+?)\\s+(?<number>\\d+)\\s*$"),

    ARTISAN_USE("^\\s*artisan\\s+use\\s+(?<name>.+?)\\s+(?<item>.+?)\\s*$"),
    ARTISAN_GET("^\\s*artisan\\s+get\\s+(?<name>.+?)\\s*$"),

    CRAFT_INFO("^\\s*craft\\s+info\\s+(?<craftName>.+?)\\s*$"),
    CRAFTING_SHOW_RECIPES("^\\s*craft\\s+show\\s+recipes\\s*$"),
    CRAFTING_CRAFT("^\\s*craft\\s+(?<itemName>.+?)\\s*$"),

    COOKING_REFRIGERATOR("^\\s*fridge\\s+(?<putOrPick>put|pick)\\s+(?<item>.+?)\\s*$"),
    COOKING_SHOW_RECIPES("^\\s*cooking\\s+show\\s+recipes\\s*$"),
    COOKING_PREPARE("^\\s*cook\\s+(?<recipeName>.+?)\\s*$"),
    EAT("^\\s*eat\\s+(?<foodName>.+?)\\s*$"),

    CHEAT_ADD_ITEM("^\\s*cheat\\s+add\\s+item\\s+(?<itemName>.+?)\\s+(?<count>\\d+)\\s*$"),
    CHEAT_ADD_DOLLARS("^\\s*cheat\\s+add\\s+dollars\\s+(?<count>\\d+)\\s*$"),

    FRIENDSHIPS("^\\s*friendships\\s*$"),
    TALK("^\\s*talk\\s+(?<username>\\w+)\\s+-m\\s+(?<message>.+)$"),
    TALK_HISTORY("^\\s*talk\\s+history\\s+(?<username>\\w+)\\s*$"),
    GIFT("^\\s*gift\\s+(?<username>\\w+)\\s+(?<item>\\w+)\\s+(?<amount>\\d+)\\s*$"),
    GIFT_LIST("^\\s*gift\\s+list\\s*$"),
    GIFT_RATE("^\\s*gift\\s+rate\\s+(?<giftNumber>\\d+)\\s+(?<rate>\\d+)\\s*$"),
    GIFT_HISTORY("^\\s*gift\\s+history\\s+(?<username>\\w+)\\s*$"),
    HUG("^\\s*hug\\s+(?<username>\\w+)\\s*$"),
    FLOWER("^\\s*flower\\s+(?<username>\\w+)\\s*$"),
    ASK_MARRIAGE("^\\s*marry\\s+(?<username>\\w+)\\s+(?<ring>\\w+)\\s*$"),
    RESPONSE_MARRIAGE("^\\s*respond\\s+marriage\\s+(?<response>yes|no)\\s+(?<username>\\w+)\\s*$"),

    ANIMALS("^\\s*animals\\s*$"),
    PET("^\\s*pet\\s+(?<name>\\w+)\\s*$"),
    SHEPHERD_ANIMALS("^\\s*shepherd\\s+(?<animalName>\\w+)\\s+(?<x>\\d+)\\s+(?<y>\\d+)\\s*$"),
    FEED_HAY("^\\s*feed\\s+hay\\s+(?<animalName>\\w+)\\s*$"),
    PRODUCES("^\\s*produces\\s*$"),
    COLLECT_PRODUCE("^\\s*collect\\s+(?<name>\\w+)\\s*$"),
    SELL_ANIMAL("^\\s*sell\\s+animal\\s+(?<name>\\w+)\\s*$"),

    SHOW_TIME("^\\s*time\\s*$"),
    SHOW_DATE("^\\s*date\\s*$"),
    SHOW_DATETIME("^\\s*datetime\\s*$"),
    SHOW_DAY_OF_WEEK("^\\s*day\\s+of\\s+week\\s*$"),
    SHOW_SEASON("^\\s*season\\s*$"),
    SHOW_WEATHER("^\\s*weather\\s*$"),
    SHOW_WEATHER_FORECAST("^\\s*weather\\s+forecast\\s*$"),

    SHOW_ALL_PRODUCTS("^\\s*store\\s+show\\s+products\\s*$"),
    SHOW_ALL_AVAILABLE_PRODUCTS("^\\s*store\\s+show\\s+available\\s*$"),
    PURCHASE("^\\s*purchase\\s+(?<productName>\\w+)\\s+(?<count>\\d+)\\s*$"),
    SELL("^\\s*sell\\s+(?<productName>\\w+)\\s+(?<count>\\d+)\\s*$");

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
        if (matcher.matches()) {
            return matcher;
        }
        return null;
    }
}