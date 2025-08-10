package io.github.StardewValley.models.enums.Types;

import io.github.StardewValley.models.ItemType;

public enum MaterialType implements MaterialTypes, ItemType {

    Egg("egg"),
    Milk("milk"),
    Cheese("cheese"),
    Sugar("sugar"),
    Oil("oil"),

    // Crops
    Wheat("Wheat"),
    WheatFlour("Wheat flour"),
    Pumpkin("Pumpkin"),
    Tomato("Tomato"),
    Corn("Corn"),
    Potato("Potato"),
    Blueberry("Blueberry"),
    Melon("Melon"),
    Apricot("Apricot"),
    RedCabbage("Red cabbage"),
    Radish("Radish"),
    Amaranth("Amaranth"),
    Kale("Kale"),
    Beet("Beet"),
    Parsnip("Parsnip"),
    Carrot("Carrot"),
    Eggplant("Eggplant"),
    Leek("Leek"),

    // Fish
    Sardine("Sardine"),
    Salmon("Salmon"),
    AnyFish("Any fish"),
    Flounder("Flounder"),
    MidnightCarp("Midnight carp"),

    // Foraged Type
    Dandelion("Dandelion"),
    Coffee("Coffee"),

    // Processed Type
    Rice("Rice"),
    Bread("Bread"),
    HashBrowns("Hash browns"),
    Omelet("Omelet"),

    // Handicrafts Type
    CopperOre("Copper ore"),
    IronOre("Iron ore"),
    GoldOre("Gold ore"),
    IridiumOre("Iridium ore"),
    CopperBar("Copper bar"),
    IronBar("Iron bar"),
    GoldBar("Gold bar"),
    IridiumBar("Iridium bar"),
    Wood("Wood"),
    Stone("Stone"),
    Coal("Coal"),
    Fiber("Fiber"),
    Iron("Iron"),
    HardWood("Hard wood"),
    GoldCoin("Gold coin"),
    Diamond("Diamond"),

    // pierre's remainings
    Bouquet("Bouquet"),
    WeddingRing("Wedding Ring"),
    DehydratorRecipe("Dehydrator recipe"),
    GrassStarterRecipe("Grass Starter recipe"),
    Vinegar("Vinegar"),
    DeluxeRetainingSoil("Deluxe retaining Soil"),
    GrassStarter("Grass Starter"),
    SpeedGro("Speed-Gro"),
    BasicRetainingSoil("Basic retaining Soil"),
    QualityRetainingSoil("Quality-Retaining Soil"),

    // Joja's remainings
    JojaCola("Joja Cola"),

    // Carpenter's items
    Barn("Barn"),
    BigBarn("Big Barn"),
    DeluxeBarn("Deluxe Barn"),
    Coop("Coop"),
    BigCoop("Big Coop"),
    DeluxeCoop("Deluxe Coop"),
    Well("Well"),
    ShippingBin("Shipping Bin"),

    // StarDrop Saloon
    Beer("Beer"),
    Salad("Salad"),
    Spaghetti("Spaghetti"),
    Pizza("Pizza"),

    // Marine's Ranch items:
    Hay("Hay"),
    MilkPail("Milk Pail"),
    Shears("Shears"),

    // Fishing poles :
    FishSmokerRecipe( "Fish Smoker recipe"),
    TroutSoup( "Trout Soup"),
    BambooPole( "Bamboo Pole"),
    TrainingRod("Training Rod"),
    FiberglassRod("Fiberglass Rod"),
    IridiumRod("Iridium Rod");

    private final String name;

    MaterialType(String name) {
        this.name = name;
    }

    // ========= ItemType =========
    @Override public boolean isTool() { return false; }

    @Override public String getName() { return this.name; } // legacy

    @Override public String id() { return this.name; }

    @Override public String iconPath() {
        // همه‌ی فایل‌ها طبق لیستت داخل یک پوشه هستند؛ اگر نیست، base را تغییر بده.
        final String base = "items/";

        // TitleCase + space→_ + apostrophe→%27  (مثل Tub_o%27_Flowers.png)
        String s = this.name.trim().replaceAll("\\s+", " ");
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (i == 0 || s.charAt(i - 1) == ' ' || s.charAt(i - 1) == '-') {
                out.append(Character.toUpperCase(c));
            } else {
                out.append(c);
            }
        }
        String fileBase = out.toString()
                .replace(" ", "_")
                .replace("'", "%27");
        return base + fileBase + ".png";
    }

    @Override public int maxStack() { return 999; }
}