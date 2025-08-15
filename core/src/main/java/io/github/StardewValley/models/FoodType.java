package io.github.StardewValley.models;

public enum FoodType implements ItemType {
    // ——— Basics
    OMELET("Omelet", r("Omelet.png"), 99),
    FRIED_EGG("Fried Egg", r("Fried_Egg.png"), 99),
    PANCAKES("Pancakes", r("Pancakes.png"), 99),
    BREAD("Bread", r("Bread.png"), 99),
    TORTILLA("Tortilla", r("Tortilla.png"), 99),
    PIZZA("Pizza", r("Pizza.png"), 99),
    SPAGHETTI("Spaghetti", r("Spaghetti.png"), 99),
    SALAD("Salad", r("Salad.png"), 99),
    SASHIMI("Sashimi", r("Sashimi.png"), 99),
    RICE_PUDDING("Rice Pudding", r("Rice_Pudding.png"), 99),
    ICE_CREAM("Ice Cream", r("Ice_Cream.png"), 99),
    COOKIE("Cookie", r("Cookie.png"), 99),
    FISH("Fish", r("Fish.png"), 0),

    // ——— Fish & soups
    TROUT_SOUP("Trout Soup", r("Trout_Soup.png"), 99),
    BAKED_FISH("Baked Fish", r("Baked_Fish.png"), 99),
    CHOWDER("Chowder", r("Chowder.png"), 99),
    FISH_STEW("Fish Stew", r("Fish_Stew.png"), 99),
    SALMON_DINNER("Salmon Dinner", r("Salmon_Dinner.png"), 99),
    CRAB_CAKES("Crab Cakes", r("Crab_Cakes.png"), 99),
    DISH_O_THE_SEA("Dish O' The Sea", r("Dish_O%27_The_Sea.png"), 99),
    CARP_SURPRISE("Carp Surprise", r("Carp_Surprise.png"), 99),

    // ——— Veg / mixed
    VEGETABLE_MEDLEY("Vegetable Medley", r("Vegetable_Medley.png"), 99),
    ROOTS_PLATTER("Roots Platter", r("Roots_Platter.png"), 99),
    STIR_FRY("Stir Fry", r("Stir_Fry.png"), 99),
    SUPER_MEAL("Super Meal", r("Super_Meal.png"), 99),
    COMPLETE_BREAKFAST("Complete Breakfast", r("Complete_Breakfast.png"), 99),

    // ——— Pumpkin / blueberry sets
    PUMPKIN_PIE("Pumpkin Pie", r("Pumpkin_Pie.png"), 99),
    PUMPKIN_SOUP("Pumpkin Soup", r("Pumpkin_Soup.png"), 99),
    BLUEBERRY_TART("Blueberry Tart", r("Blueberry_Tart.png"), 99),

    // ——— From your list (منتخب کاملِ پرکاربرد)
    ARTICHOKE_DIP("Artichoke Dip", r("Artichoke_Dip.png"), 99),
    AUTUMNS_BOUNTY("Autumn's Bounty", r("Autumn%27s_Bounty.png"), 99),
    BANANA_PUDDING("Banana Pudding", r("Banana_Pudding.png"), 99),
    BEAN_HOTPOT("Bean Hotpot", r("Bean_Hotpot.png"), 99),
    PALE_BROTH("Pale Broth", r("Pale_Broth.png"), 99),
    FARMERS_LUNCH("Farmer's Lunch", r("Farmer%27s_Lunch.png"), 99),
    TOM_KHA_SOUP("Tom Kha Soup", r("Tom_Kha_Soup.png"), 99),
    GLAZED_YAMS("Glazed Yams", r("Glazed_Yams.png"), 99),
    MAKI_ROLL("Maki Roll", r("Maki_Roll.png"), 99),
    BLACKBERRY_COBBLER("Blackberry Cobbler", r("Blackberry_Cobbler.png"), 99),
    RHUBARB_PIE("Rhubarb Pie", r("Rhubarb_Pie.png"), 99),
    SPICY_EEL("Spicy Eel", r("Spicy_Eel.png"), 99),
    FRIED_MUSHROOM("Fried Mushroom", r("Fried_Mushroom.png"), 99),
    COLESLAW("Coleslaw", r("Coleslaw.png"), 99),
    PINK_CAKE("Pink Cake", r("Pink_Cake.png"), 99),
    BRUSCHETTA("Bruschetta", r("Bruschetta.png"), 99),
    EGGPLANT_PARMESAN("Eggplant Parmesan", r("Eggplant_Parmesan.png"), 99),
    MANGO_STICKY_RICE("Mango Sticky Rice", r("Mango_Sticky_Rice.png"), 99),
    POPPYSEED_MUFFIN("Poppyseed Muffin", r("Poppyseed_Muffin.png"), 99),
    PEPPER_POPPERS("Pepper Poppers", r("Pepper_Poppers.png"), 99),
    FRIED_CALAMARI("Fried Calamari", r("Fried_Calamari.png"), 99),
    PLUM_PUDDING("Plum Pudding", r("Plum_Pudding.png"), 99),
    FIDDLEHEAD_RISOTTO("Fiddlehead Risotto", r("Fiddlehead_Risotto.png"), 99),
    PARSNIP_SOUP("Parsnip Soup", r("Parsnip_Soup.png"), 99),
    MINERS_TREAT("Miner's Treat", r("Miner%27s_Treat.png"), 99),
    MAPLE_BAR("Maple Bar", r("Maple_Bar.png"), 99),
    ESCARGOT("Escargot", r("Escargot.png"), 99),
    HASHBROWNS("Hashbrowns", r("Hashbrowns.png"), 99),
    RED_PLATE("Red Plate", r("Red_Plate.png"), 99),
    LUCKY_LUNCH("Lucky Lunch", r("Lucky_Lunch.png"), 99),
    GINGER_ALE("Ginger Ale", r("Ginger_Ale.png"), 99),
    SEAFOAM_PUDDING("Seafoam Pudding", r("Seafoam_Pudding.png"), 99),
    FRUIT_SALAD("Fruit Salad", r("Fruit_Salad.png"), 99),
    ROASTED_HAZELNUTS("Roasted Hazelnuts", r("Roasted_Hazelnuts.png"), 99),
    MOSS_SOUP("Moss Soup", r("Moss_Soup.png"), 99),
    SQUID_INK_RAVIOLI("Squid Ink Ravioli", r("Squid_Ink_Ravioli.png"), 99),
    TROPICAL_CURRY("Tropical Curry", r("Tropical_Curry.png"), 99),
    CHEESE_CAULIFLOWER("Cheese Cauliflower", r("Cheese_Cauliflower.png"), 99),
    CRANBERRY_SAUCE("Cranberry Sauce", r("Cranberry_Sauce.png"), 99),
    CRANBERRY_CANDY("Cranberry Candy", r("Cranberry_Candy.png"), 99),
    TRIPLE_SHOT_ESPRESSO("Triple Shot Espresso", r("Triple_Shot_Espresso.png"), 99),

    // ——— فقط اگر هنوز می‌خواهی نمونه‌های قبلی بمانند (آیکن موقت!)
    CARROT_SOUP("Carrot Soup", r("Parsnip_Soup.png"), 99),     // جایگزین تصویری موقت
    GRILLED_CORN("Grilled Corn", r("Tortilla.png"), 99);       // آیکن نزدیک‌تر

    private final String id;
    private final String iconPath;
    private final int maxStack;

    FoodType(String id, String iconPath, int maxStack) {
        this.id = id;
        this.iconPath = iconPath;
        this.maxStack = maxStack;
    }

    // helper تا هر جا لازم شد راحت مسیر بسازیم
    private static String r(String file) { return "recipes/" + file; }

    @Override public String id() { return id; }
    @Override public String iconPath() { return iconPath; }
    @Override public int maxStack() { return maxStack; }
    @Override public boolean stackable() { return true; }
}
