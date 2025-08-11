package io.github.StardewValley.models.Artisan;

import io.github.StardewValley.models.ItemType;


public class ArtisanRecipe {
    public final MachineType machine;
    public final ItemType input;
    
    public final int inputQty;
    public final ArtisanProductType output;
    public final int outputQty;
    public final float prepareSeconds;
    public final float workSeconds;
    /** برای آیکن‌های واریانت (ژله/آبمیوه/شراب رنگی) اختیاریه */
    public final String variantKey;

    public ArtisanRecipe(MachineType m, ItemType in, int inQty,
                            ArtisanProductType out, int outQty,
                            float prep, float work, String variantKey) {
        this.machine=m; this.input=in; this.inputQty=inQty;
        this.output=out; this.outputQty=outQty;
        this.prepareSeconds=prep; this.workSeconds=work; this.variantKey = variantKey;
    }
}
