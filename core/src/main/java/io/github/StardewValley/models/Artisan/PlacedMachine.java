package io.github.StardewValley.models.Artisan;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.StardewValley.controllers.GameController;
import io.github.StardewValley.models.*;
import io.github.StardewValley.models.enums.Types.MaterialType;

public class PlacedMachine {
    public enum State { IDLE, LOADED, WORKING, DONE }

    private final String mapId;
    private final int tx, ty;         // tile coords
    private final MachineType type;

    private State state = State.IDLE;

    // پیشرفت بر حسب «ثانیهٔ بازی»
    private float t = 0f;     // elapsed (game-seconds)
    private float total = 0f; // total   (game-seconds)

    // ورودی/خروجی
    private ItemType inputA;    // ورودی اصلی (مثلاً Wood یا Milk)
    private int      inQty;
    private ItemType aux;       // ورودی کمکی (برای کوره و ... اگر خواستی توسعه بده)
    private int      auxQty;

    private ItemType output;
    private int      outQty;

    // رسپی فعال (برای نمایش دقیق زمان/خروجی)
    private ArtisanRecipe activeRecipe;

    public PlacedMachine(String mapId, int tx, int ty, MachineType type) {
        this.mapId = mapId; this.tx = tx; this.ty = ty; this.type = type;
    }

    public String mapId()     { return mapId; }
    public int tx()           { return tx; }
    public int ty()           { return ty; }
    public MachineType type() { return type; }
    public State state()      { return state; }
    public float progress01() { return total <= 0 ? 0f : Math.min(1f, t/total); }

    // برای UI (MachineMenuView با رفلکشن صدا می‌زند)
    public ItemType getLoadedInput() { return inputA; }

    public void render(SpriteBatch batch) {
        Texture tex = GameAssetManager.getGameAssetManager().getTexture(type.iconPath());
        float w = GameController.TILE_SIZE * 0.9f;
        float h = GameController.TILE_SIZE * 0.9f;
        float wx = tx * GameController.TILE_SIZE + (GameController.TILE_SIZE - w) / 2f;
        float wy = ty * GameController.TILE_SIZE;
        batch.draw(tex, wx, wy, w, h);
    }

    // ---------- زمان‌بندی (فقط «ثانیهٔ بازی») ----------
    /** gameSeconds = ثانیهٔ بازی؛ بیرون از این کلاس تبدیل نشه. */
    public void update(float gameSeconds) {
        if (state != State.WORKING) return;
        t += gameSeconds;
        if (t >= total) { t = total; state = State.DONE; }
    }

    /** کمکی برای جلو بردن ساعتی (مثلاً وقتی World زمان را یک‌ساعته جلو می‌برد) */
    public void advanceGameHours(float hours) {
        update(hours * 3600f);
    }

    // ---------- API که منو/کنترلر ازش استفاده می‌کنن ----------
    /** یک ورودی ساده برای تست؛ اگر می‌خواهی همه ورودی‌ها (مثلاً Hops/… برای Keg) را پوشش بدهی، اینجا را جنریک کن. */
    public boolean loadFrom(Inventory inv) {
        if (state != State.IDLE) return false;

        switch (type) {
            case CHARCOAL_KILN:
                if (!inv.contains(MaterialType.Wood, 10)) return false;
                inv.remove(MaterialType.Wood, 10);
                inputA = MaterialType.Wood; inQty = 10;
                state = State.LOADED;
                return true;

            case CHEESE_PRESS:
                if (!inv.contains(MaterialType.Milk, 1)) return false;
                inv.remove(MaterialType.Milk, 1);
                inputA = MaterialType.Milk; inQty = 1;
                state = State.LOADED;
                return true;

            case KEG:
                // نمونه: فعلاً گندم → Beer (اگر بقیه ورودی‌ها را می‌خواهی، اینجا با ArtisanRecipeBook جنریک کن)
                if (!inv.contains(MaterialType.Wheat, 1)) return false;
                inv.remove(MaterialType.Wheat, 1);
                inputA = MaterialType.Wheat; inQty = 1;
                state = State.LOADED;
                return true;

            case LOOM:
                if (!inv.contains(MaterialType.Wool, 1)) return false;
                inv.remove(MaterialType.Wool, 1);
                inputA = MaterialType.Wool; inQty = 1;
                state = State.LOADED;
                return true;

            case MAYO_MACHINE:
                if (!inv.contains(MaterialType.Egg, 1)) return false;
                inv.remove(MaterialType.Egg, 1);
                inputA = MaterialType.Egg; inQty = 1;
                state = State.LOADED;
                return true;

            case PRESERVE_JAR:
                // نمونه: فعلاً Tomato → Pickles
                if (!inv.contains(MaterialType.Tomato, 1)) return false;
                inv.remove(MaterialType.Tomato, 1);
                inputA = MaterialType.Tomato; inQty = 1;
                state = State.LOADED;
                return true;

            default:
                return false;
        }
    }

    /** شروع با زمان و خروجی دقیقِ «رسپی» متناسب با ورودیِ لودشده */
    public boolean start() {
        if (state != State.LOADED) return false;

        // 1) رسپی متناسب با همین ورودی
        ArtisanRecipe r = (inputA != null)
                ? ArtisanRecipeBook.match(type, inputA)
                : null;

        // 2) برای ماشین‌های بدون ورودی (مثلاً Bee House) یکی را انتخاب کن
        if (r == null) {
            var list = ArtisanRecipeBook.forMachine(type);
            // اگر ماشینی بدون ورودی داریم، باید رسپیِ input=null داشته باشیم
            for (var rr : list) { if (rr.input == null && rr.inputTag == null) { r = rr; break; } }
            if (r == null && !list.isEmpty()) r = list.get(0); // fallback
        }
        if (r == null) return false;

        activeRecipe = r;

        // 3) خروجی و زمان دقیق از Recipe
        output = r.output;
        outQty = r.outputQty;
        total  = Math.max(0f, r.prepareSeconds + r.workSeconds); // همه بر حسب «ثانیهٔ بازی»

        // 4) شروع
        t = 0f;
        state = State.WORKING;

        // ورودی‌ها مصرف شدند
        inputA = null; inQty = 0; aux = null; auxQty = 0;
        return true;
    }

    public void cheatFinish() {
        if (state == State.WORKING) { t = total; state = State.DONE; }
    }

    public void cancelTo(Inventory inv) {
        if (state == State.LOADED) {
            if (inputA != null && inQty > 0) inv.add(inputA, inQty);
        }
        inputA = null; inQty = 0; aux = null; auxQty = 0;
        t = 0; total = 0; output = null; outQty = 0; activeRecipe = null;
        state = State.IDLE;
    }

    public boolean collectTo(Inventory inv) {
        if (state != State.DONE || output == null || outQty <= 0) return false;
        inv.add(output, outQty);
        output = null; outQty = 0;
        t = 0; total = 0; activeRecipe = null;
        state = State.IDLE;
        return true;
    }
}