package io.github.StardewValley.models.Artisan;

import io.github.StardewValley.models.Inventory;
import io.github.StardewValley.models.ItemType;

public class MachineInstance {
    public enum State { IDLE, PREPARING, WORKING, DONE }

    public final MachineType type;
    public final int tileX, tileY;

    private State state = State.IDLE;
    private ArtisanRecipe recipe;
    private ItemType inputType;
    private int inputQty;
    private float remaining;

    // به جای Stack خروجی را به صورت نوع+تعداد نگه می‌داریم
    private ItemType outputType;
    private int outputQty;

    public MachineInstance(MachineType type, int tileX, int tileY) {
        this.type = type;
        this.tileX = tileX;
        this.tileY = tileY;
    }

    public State state()   { return state; }
    public ArtisanRecipe recipe() { return recipe; }

    /** تلاش برای شروع پردازش با آیتم بازیکن */
    public boolean tryStart(Inventory inv, ItemType heldType, int heldQty) {
        if (state != State.IDLE) return false;

        ArtisanRecipe r = ProcessingCatalog.find(type, heldType, heldQty);
        if (r == null) return false;

        // remove() در کد تو int برمی‌گردونه (تعداد حذف‌شده)
        int removed = inv.remove(heldType, r.inputQty);
        if (removed < r.inputQty) {
            // اگر کمتر برداشت، رول‌بک کن و شروع نکن
            if (removed > 0) inv.add(heldType, removed);
            return false;
        }

        this.recipe    = r;
        this.inputType = heldType;
        this.inputQty  = r.inputQty;
        this.remaining = r.prepareSeconds;   // ← مطابق کلاس ArtisanRecipe خودت
        this.state     = State.PREPARING;
        return true;
    }

    public void update(float delta) {
        switch (state) {
            case PREPARING:
                remaining -= delta;
                if (remaining <= 0f) {
                    state = State.WORKING;
                    remaining = recipe.workSeconds;   // ← مطابق فیلدهای Recipe خودت
                }
                break;

            case WORKING:
                remaining -= delta;
                if (remaining <= 0f) {
                    // ساخت خروجی: مستقیماً نوع و تعداد را ذخیره کن
                    outputType = recipe.output;   // توجه: در ArtisanRecipe خروجی باید ItemType باشد
                    outputQty  = recipe.outputQty;
                    state = State.DONE;
                    remaining = 0f;
                }
                break;

            default:
                // IDLE / DONE نیازی به آپدیت ندارند
                break;
        }
    }

    public boolean collect(Inventory inv) {
        if (state != State.DONE || outputType == null || outputQty <= 0) return false;

        inv.add(outputType, outputQty);
        clear();
        return true;
    }

    public boolean cancel(Inventory inv) {
        if (state == State.IDLE) return false;

        // ورودی را برگردان
        if (recipe != null && inputType != null && inputQty > 0) {
            inv.add(inputType, inputQty);
        }
        clear();
        return true;
    }

    public float progress01() {
        if (state == State.PREPARING) return 1f - (remaining / recipe.prepareSeconds);
        if (state == State.WORKING)   return 1f - (remaining / recipe.workSeconds);
        return state == State.DONE ? 1f : 0f;
    }

    private void clear() {
        recipe = null;
        inputType = null;
        inputQty = 0;
        outputType = null;
        outputQty = 0;
        remaining = 0f;
        state = State.IDLE;
    }
}