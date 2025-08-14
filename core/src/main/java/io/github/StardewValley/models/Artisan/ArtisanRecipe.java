package io.github.StardewValley.models.Artisan;

import io.github.StardewValley.models.ItemType;

/**
 * تعریف یک رسپی برای دستگاه‌های صنایع‌دستی.
 * ورودی می‌تواند «آیتم مشخص» یا «دسته‌ای (Tag)» باشد.
 * ورودی کمکی/سوخت اختیاری است (مثل Coal در Furnace).
 * خروجی جنریک (ItemType) تا با MaterialType یا ArtisanProductType یکی شود.
 */
public final class ArtisanRecipe {

    /** برچسب‌های ورودی دسته‌ای/خاص برای تعریف راحت رسپی‌ها. */
    public enum InputTag {
        // عمومی
        ANY_FRUIT, ANY_VEGETABLE, ANY_MUSHROOM, ANY_FISH, ANY_ORE, ANY_EGG, ANY_MILK, ANY_FLOWER,
        // موارد خاص
        HOPS, WHEAT, COFFEE_BEAN, RICE, HONEY,
        MILK, GOAT_MILK, DUCK_EGG, VOID_EGG, DINO_EGG,
        WOOL, CORN, SUNFLOWER_SEEDS, TRUFFLE,
        WOOD, COPPER_ORE, IRON_ORE, GOLD_ORE, IRIDIUM_ORE
    }

    // دستگاه هدف
    public final MachineType machine;

    // ورودی اصلی (یکی از دو حالت: input یا inputTag)
    public final ItemType input;           // nullable
    public final int inputQty;
    public final InputTag inputTag;        // nullable
    public final String uiInputIconPath;   // nullable (برای نمایش وقتی دسته‌ای است)
    public final String uiInputName;       // nullable

    // ورودی کمکی/سوخت (اختیاری؛ اینجا دقیقِ آیتم را نگه می‌داریم)
    public final ItemType helper;          // nullable
    public final int helperQty;

    // خروجی (الزاماً جنریک)
    public final ItemType output;
    public final int outputQty;

    // زمان‌بندی (ثانیهٔ بازی)
    public final float prepareSeconds;
    public final float workSeconds;

    // کلید واریانت (برای ژله/آبمیوه/شراب رنگی و ...)
    public final String variantKey;

    // ───────── سازندهٔ پایه (private) ─────────
    private ArtisanRecipe(
            MachineType m,
            ItemType in, int inQty,
            InputTag tag, String uiIcon, String uiName,
            ItemType helper, int helperQty,
            ItemType out, int outQty,
            float prep, float work, String variantKey
    ) {
        this.machine = m;
        this.input = in;
        this.inputQty = inQty;
        this.inputTag = tag;
        this.uiInputIconPath = uiIcon;
        this.uiInputName = uiName;
        this.helper = helper;
        this.helperQty = helperQty;
        this.output = out;
        this.outputQty = outQty;
        this.prepareSeconds = prep;
        this.workSeconds = work;
        this.variantKey = variantKey;
    }

    // ───────── کارخانه‌ها (factory methods) ─────────

    /** ورودی دقیق + خروجی جنریک (ترجیحی) */
    public static ArtisanRecipe fixed(
            MachineType m, ItemType in, int inQty,
            ItemType out, int outQty,
            float prep, float work, String variantKey
    ) {
        return new ArtisanRecipe(m, in, inQty, null, null, null, null, 0,
                out, outQty, prep, work, variantKey);
    }

    /** ورودی دقیق + helper دقیق + خروجی جنریک */
    public static ArtisanRecipe of2(
            MachineType m, ItemType in, int inQty,
            ItemType helper, int helperQty,
            ItemType out, int outQty,
            float prep, float work, String variantKey
    ) {
        return new ArtisanRecipe(m, in, inQty, null, null, null, helper, helperQty,
                out, outQty, prep, work, variantKey);
    }

    /** ورودی دسته‌ای (Tag) + خروجی جنریک */
    public static ArtisanRecipe tagged(
            MachineType m, InputTag tag, int inQty,
            ItemType out, int outQty,
            float prep, float work, String variantKey,
            String uiIconPath, String uiName
    ) {
        return new ArtisanRecipe(m, null, inQty, tag, uiIconPath, uiName, null, 0,
                out, outQty, prep, work, variantKey);
    }

    /** ورودی دسته‌ای + helper دقیق + خروجی جنریک */
    public static ArtisanRecipe tagged2(
            MachineType m, InputTag tag, int inQty,
            ItemType helper, int helperQty,
            ItemType out, int outQty,
            float prep, float work, String variantKey,
            String uiIconPath, String uiName
    ) {
        return new ArtisanRecipe(m, null, inQty, tag, uiIconPath, uiName, helper, helperQty,
                out, outQty, prep, work, variantKey);
    }

    // ───── نسخه‌های سازگار با عقب برای خروجی از نوع ArtisanProductType ─────

    public static ArtisanRecipe fixed(
            MachineType m, ItemType in, int inQty,
            ArtisanProductType out, int outQty,
            float prep, float work, String variantKey
    ) {
        return fixed(m, in, inQty, (ItemType) out, outQty, prep, work, variantKey);
    }

    public static ArtisanRecipe of2(
            MachineType m, ItemType in, int inQty,
            ItemType helper, int helperQty,
            ArtisanProductType out, int outQty,
            float prep, float work, String variantKey
    ) {
        return of2(m, in, inQty, helper, helperQty, (ItemType) out, outQty, prep, work, variantKey);
    }

    public static ArtisanRecipe tagged(
            MachineType m, InputTag tag, int inQty,
            ArtisanProductType out, int outQty,
            float prep, float work, String variantKey,
            String uiIconPath, String uiName
    ) {
        return tagged(m, tag, inQty, (ItemType) out, outQty, prep, work, variantKey, uiIconPath, uiName);
    }

    public static ArtisanRecipe tagged2(
            MachineType m, InputTag tag, int inQty,
            ItemType helper, int helperQty,
            ArtisanProductType out, int outQty,
            float prep, float work, String variantKey,
            String uiIconPath, String uiName
    ) {
        return tagged2(m, tag, inQty, helper, helperQty, (ItemType) out, outQty, prep, work, variantKey, uiIconPath, uiName);
    }

    // ───────── کمکی‌های UI ─────────
    /** آیا این رسپی به ورودی کمکی نیاز دارد؟ */
    public boolean usesHelper() { return helper != null && helperQty > 0; }

    /** آیکن ورودی برای UI (اگر دسته‌ای است، از uiInputIconPath استفاده می‌شود). */
    public String uiInputIconPathFallbackToItem() {
        if (inputTag != null) return uiInputIconPath;
        return input != null ? input.iconPath() : null;
    }

    /** نام ورودی برای UI. */
    public String uiInputDisplayName() {
        if (inputTag != null)
            return (uiInputName != null && !uiInputName.isEmpty()) ? uiInputName : inputTag.name();
        return input != null ? input.id() : "";
    }
}