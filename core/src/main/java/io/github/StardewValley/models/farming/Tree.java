// models/farming/Tree.java
package io.github.StardewValley.models.farming;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.StardewValley.models.GameAssetManager;
import io.github.StardewValley.models.ItemType;

public class Tree {
    private final TreeType type;
    private int daysGrown = 0;
    private boolean chopped = false;

    // ---- regrow after chop ----
    private boolean stump = false;

    private boolean wateredToday = false;
    private int     dryStreakDays = 0;
    private boolean dead = false;
    private boolean autoWaterForever = false;

    // fruit timing
    private int fruitCooldown = 0;
    private static final int FRUIT_PERIOD = 2;

    // ---- transient anim state ----
    private enum A { NONE, GROW, HARVEST, CHOP }
    private A anim = A.NONE;
    private float animTime = 0f;
    private Animation<TextureRegion> growAnim, harvestAnim, chopAnim;
    private boolean pendingChopPayout = false; // controller pays out after chop anim ends

    // cache current stage index so we can trigger grow anim when stage advances
    private int lastStageIndex = 0;

    public Tree(TreeType type) {
        this.type = type;
        this.lastStageIndex = stageIndexFor(daysGrown);
    }

    public void updateDaily(boolean autoWater) {
        if (dead) { wateredToday = false; return; }

        // ⛔ remove stump countdown block entirely

        if (chopped) { wateredToday = false; return; } // safety, but we'll reset chopped below anyway

        boolean effectiveAuto = autoWaterForever || autoWater;
        if (effectiveAuto) {
            if (!isMature()) daysGrown++;
            dryStreakDays = 0;
            wateredToday = false;
        } else if (wateredToday) {
            if (!isMature()) daysGrown++;
            dryStreakDays = 0;
            wateredToday = false;
        } else {
            dryStreakDays++;
            if (dryStreakDays >= 2) dead = true;
        }

        if (!dead && isMature() && type.producesFruit() && fruitCooldown > 0) fruitCooldown--;

        int si = stageIndexFor(daysGrown);
        if (si > lastStageIndex) { lastStageIndex = si; playGrow(); }
    }

    // call each frame from GameController.update(...)
    public void updateRealtime(float dt) {
        if (anim == A.NONE) return;
        animTime += dt;

        Animation<TextureRegion> a =
            (anim == A.GROW)    ? growAnim :
                (anim == A.HARVEST) ? harvestAnim :
                    (anim == A.CHOP)    ? chopAnim : null;

        if (a != null && animTime >= a.getAnimationDuration()) {
            // end of one-shot anim
            anim = A.NONE;
            animTime = 0f;
            if (pendingChopPayout) {
                chopped = true;     // mark done; controller will remove & give wood
            }
        }
    }

    public TreeType getType()  { return type; }
    public int getDaysGrown()  { return daysGrown; }
    public boolean isChopped() { return chopped; }

    // ---- products ----
    public boolean canShakeFruit() {
        return !stump && isMature() && type.producesFruit() && fruitCooldown == 0 && anim != A.CHOP;
    }
    public boolean isMature() { return daysGrown >= type.totalGrowthDays(); }

    // was: if (stump) return "trees/" + type.animKey() + "/stump.png";
    public String getCurrentSpritePath() {
        // If the tree is mature and it’s a fruit tree, use cooldown to choose the look
        if (isMature() && type.producesFruit()) {
            if (fruitCooldown > 0) {
                // right after harvest: show the very first png (e.g., .../0.png)
                return type.stageSprites()[0];
            } else {
                // fruit ready again: show the mature look (your normal last-stage sprite)
                return type.getSpriteForDay(daysGrown);
            }
        }
        // Otherwise: normal growth-stage sprite
        return type.getSpriteForDay(daysGrown);
    }


    public ItemType collectFruit() {
        if (!canShakeFruit()) return null;
        fruitCooldown = FRUIT_PERIOD;
        playHarvest();
        return type.fruitType();
    }

    // ---- render helpers ----
    public TextureRegion getRenderFrame() {
        // show anim frame if any
        Animation<TextureRegion> a =
            (anim == A.GROW)    ? growAnim :
                (anim == A.HARVEST) ? harvestAnim :
                    (anim == A.CHOP)    ? chopAnim : null;

        if (a != null) return a.getKeyFrame(animTime, false);

        // otherwise show current idle sprite for stage
        String path = getCurrentSpritePath();
        return new TextureRegion(GameAssetManager.getGameAssetManager().getTexture(path));
    }


    // ---- triggers from controller ----
    public void playHarvest() {
        ensureHarvestAnim();
        anim = A.HARVEST; animTime = 0f;
    }
    public void playGrow() {
        ensureGrowAnim();
        anim = A.GROW; animTime = 0f;
    }
    public void playChop() {
        ensureChopAnim();
        anim = A.CHOP; animTime = 0f;
        pendingChopPayout = true;
    }

    public boolean needsChopPayout() {
        return pendingChopPayout && chopped && anim == A.NONE;
    }
    public void clearChopPayout() { pendingChopPayout = false; }

    // ---- lazy-load anims ----
    private void ensureGrowAnim() {
        if (growAnim != null) return;
        var am = GameAssetManager.getGameAssetManager();
        growAnim = am.getFrameAnimation(
            "tree_grow_" + type.animKey(),     // cache key
            "trees/grow/" + type.animKey() + "/", // folder
            3, 0.10f, false
        );
    }

    private void ensureHarvestAnim() {
        if (harvestAnim != null) return;
        var am = GameAssetManager.getGameAssetManager();
        harvestAnim = am.getFrameAnimation(
            "tree_harvest_" + type.animKey(),
            "trees/harvest/" + type.animKey() + "/",
            8, 0.08f, false
        );
    }

    private void ensureChopAnim() {
        if (chopAnim != null) return;
        var am = GameAssetManager.getGameAssetManager();
        chopAnim = am.getFrameAnimation(
            "tree_chop_" + type.animKey(),
            "trees/chop/" + type.animKey() + "/",
            10, 0.08f, false
        );
    }

    private int stageIndexFor(int days) {
        int acc = 0;
        for (int i = 0; i < type.stageDays().length; i++) {
            acc += type.stageDays()[i];
            if (days < acc) return i;
        }
        return type.stageDays().length; // mature
    }

    public boolean isStump() { return stump; }

    public void water() { if (!dead && !chopped) wateredToday = true; }
    public void setAutoWaterForever(boolean v) { this.autoWaterForever = v; }
    public boolean isDead() { return dead; }

    // Called by controller right after wood is awarded
    public void onChopPayoutClaimedStartRegrow() {
        pendingChopPayout = false;
        chopped = false;        // tree will keep living
        dead = false;           // ensure not dead
        dryStreakDays = 0;      // clear drought
        daysGrown = 0;          // ⟵ reset to stage 0 (0.png)
        fruitCooldown = 0;
        stump = false;          // ignore stump behavior
        playGrow();             // nice little pop
    }
}
