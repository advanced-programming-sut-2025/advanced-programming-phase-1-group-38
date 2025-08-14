package io.github.StardewValley.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.StardewValley.controllers.GameController;
import io.github.StardewValley.models.Artisan.ArtisanRecipe;
import io.github.StardewValley.models.Artisan.ArtisanRecipeBook;
import io.github.StardewValley.models.Artisan.PlacedMachine;
import io.github.StardewValley.models.GameAssetManager;
import io.github.StardewValley.models.Inventory;
import io.github.StardewValley.models.ItemStack;
import io.github.StardewValley.models.ItemType;

import java.lang.reflect.Method;

public class MachineMenuView {
    private final GameController controller;
    private final Inventory inv;

    private PlacedMachine machine;
    private boolean visible = false;

    private final Texture panelBg;
    private final Texture slot;
    private Texture machineIcon;

    private final BitmapFont big;
    private final BitmapFont small;

    private ArtisanRecipe currentRecipe;
    private Texture inputIcon, outputIcon;
    private String  inputName = "", outputName = "";

    private final UIButton btnLoad    = new UIButton("Load");
    private final UIButton btnStart   = new UIButton("Start");
    private final UIButton btnCollect = new UIButton("Collect");
    private final UIButton btnCancel  = new UIButton("Cancel");
    private final UIButton btnCheat   = new UIButton("Cheat");
    private final UIButton btnClose   = new UIButton("Close");

    public MachineMenuView(GameController c) {
        this.controller = c;
        this.inv = c.getPlayer().getInventory();

        this.panelBg = GameAssetManager.getGameAssetManager().getTexture("inventory/panel_bg.png");
        this.slot    = GameAssetManager.getGameAssetManager().getTexture("inventory/slot.png");

        this.big   = GameAssetManager.getGameAssetManager().getBigFont();
        this.small = GameAssetManager.getGameAssetManager().getSmallFont();

        btnLoad.onClick(()    -> { if (machine != null) { machine.loadFrom(inv);   refreshIO(); }});
        btnStart.onClick(()   -> { if (machine != null) { machine.start();         refreshIO(); }});
        btnCollect.onClick(() -> { if (machine != null) { machine.collectTo(inv);  refreshIO(); }});
        btnCancel.onClick(()  -> { if (machine != null) { machine.cancelTo(inv);   refreshIO(); }});
        // ---- Cheat خودکفا
        btnCheat.onClick(this::doCheatFinish);
        btnClose.onClick(this::close);
    }

    private void doCheatFinish() {
        if (machine == null) return;
        String st = String.valueOf(machine.state());
        // اگر خالیه، تا جایی که میشه خودکار Load و Start کن
        if ("IDLE".equals(st)) {
            // تلاش برای لود از اینونتوری
            machine.loadFrom(inv);
            st = String.valueOf(machine.state());
        }
        if ("LOADED".equals(st)) {
            machine.start();
        }
        // حالا سریع تمام کن
        machine.cheatFinish();

        // درجا منطق دستگاه‌ها را به‌روزرسانی کن تا DONE شود
        controller.updateMachines(0f);

        // UI را تازه کن
        refreshIO();
    }

    public void open(PlacedMachine m) {
        this.machine = m;
        this.visible = true;

        this.machineIcon = null;
        if (machine != null && machine.type() != null) {
            String ip = machine.type().iconPath();
            if (ip != null) machineIcon = GameAssetManager.getGameAssetManager().getTexture(ip);
        }
        refreshIO();
    }

    public void close() { visible = false; machine = null; }
    public boolean isVisible() { return visible; }

    public void render(SpriteBatch batch, float x, float y, float w, float h) {
        if (!visible || machine == null) return;
        if (Gdx.input.isKeyJustPressed(Input.Keys.E) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            close(); return;
        }

        batch.draw(panelBg, x, y, w, h);

        String title = machine.type().id();
        GlyphLayout tl = new GlyphLayout(big, title);
        big.draw(batch, tl, x + (w - tl.width)/2f, y + h - 12f);

        if (machineIcon != null) {
            float ico = 48f;
            batch.draw(machineIcon, x + 14f, y + h - ico - 24f, ico, ico);
        }

        float cell = 40f, ioGap = 18f;
        float outX = x + w - 14f - cell;
        float inX  = outX - cell - ioGap;
        float ioY  = y + h - cell - 28f;

        small.draw(batch, "Input",  inX,  ioY + cell + 14f);
        small.draw(batch, "Output", outX, ioY + cell + 14f);

        batch.setColor(1,1,1,0.9f);
        batch.draw(slot, inX, ioY, cell, cell);
        if (inputIcon != null) {
            float pad = 4f; batch.setColor(1,1,1,1);
            batch.draw(inputIcon, inX + pad, ioY + pad, cell - 2*pad, cell - 2*pad);
        }
        if (!inputName.isEmpty()) {
            GlyphLayout gl = new GlyphLayout(small, inputName);
            small.draw(batch, gl, inX + (cell - gl.width)/2f, ioY - 4f);
        }

        batch.setColor(1,1,1,0.9f);
        batch.draw(slot, outX, ioY, cell, cell);
        if (outputIcon != null) {
            float pad = 4f; batch.setColor(1,1,1,1);
            batch.draw(outputIcon, outX + pad, ioY + pad, cell - 2*pad, cell - 2*pad);
        }
        if (!outputName.isEmpty()) {
            GlyphLayout gl = new GlyphLayout(small, outputName);
            small.draw(batch, gl, outX + (cell - gl.width)/2f, ioY - 4f);
        }
        batch.setColor(1,1,1,1);

        float bx = x + 16f, by = y + 64f, barW = w - 32f, barH = 14f;
        batch.draw(slot, bx, by, barW, barH);
        float p = machine.progress01();
        batch.draw(slot, bx, by, Math.max(0, Math.min(1f, p)) * barW, barH);

        small.draw(batch, "State: " + machine.state(), bx, by + barH + 16f);
        small.draw(batch, String.format("Progress: %d%%", Math.round(p * 100f)),
                bx + barW - 86f, by + barH + 16f);

        final int count = 5;
        float gap = 12f, bw = (w - 32f - gap * (count - 1)) / count, bh = 28f;
        float byBtn = y + 16f, b1x = x + 16f;

        btnLoad .setBounds (b1x + (bw + gap) * 0, byBtn, bw, bh);
        btnStart.setBounds (b1x + (bw + gap) * 1, byBtn, bw, bh);
        btnCollect.setBounds(b1x + (bw + gap) * 2, byBtn, bw, bh);
        btnCancel.setBounds (b1x + (bw + gap) * 3, byBtn, bw, bh);
        btnCheat.setBounds  (b1x + (bw + gap) * 4, byBtn, bw, bh);

        float cW = 64f, cH = 24f;
        btnClose.setBounds(x + w - cW - 10f, y + h - cH - 10f, cW, cH);

        String st = String.valueOf(machine.state());
        boolean done = (p >= 1f) || "DONE".equals(st);
        btnLoad.setEnabled("IDLE".equals(st));
        btnStart.setEnabled("LOADED".equals(st));
        btnCancel.setEnabled("LOADED".equals(st) || "RUNNING".equals(st));
        btnCollect.setEnabled(done);
        // روی IDLE/LOADED/RUNNING فعال است تا خودش Load/Start کند
        btnCheat.setEnabled(!done);

        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();
        boolean justClick = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);

        btnLoad.draw(batch, small, slot, mx, my, justClick);
        btnStart.draw(batch, small, slot, mx, my, justClick);
        btnCollect.draw(batch, small, slot, mx, my, justClick);
        btnCancel.draw(batch, small, slot, mx, my, justClick);
        btnCheat.draw(batch, small, slot, mx, my, justClick);
        btnClose.draw(batch, small, slot, mx, my, justClick);
    }

    // ====== آیکن‌ها/نام‌ها ======
    private ItemType getLoadedInputTypeOrNull() {
        Object obj = callNoArg(machine, "getLoadedInput", "loadedInput", "getInput");
        if (obj instanceof ItemType) return (ItemType) obj;
        if (obj instanceof ItemStack) return ((ItemStack) obj).getType();
        return null;
    }
    private ArtisanRecipe pickRecipe() {
        if (machine == null || machine.type() == null) return null;
        ItemType loaded = getLoadedInputTypeOrNull();
        if (loaded != null) {
            ArtisanRecipe m = ArtisanRecipeBook.match(machine.type(), loaded);
            if (m != null) return m;
        }
        var list = ArtisanRecipeBook.forMachine(machine.type());
        return list.isEmpty() ? null : list.get(0);
    }
    private void refreshIO() {
        currentRecipe = pickRecipe();
        ItemType loaded = getLoadedInputTypeOrNull();

        inputIcon  = (currentRecipe != null) ? ArtisanRecipeBook.inputIcon(currentRecipe, loaded) : null;
        outputIcon = (currentRecipe != null) ? ArtisanRecipeBook.outputIcon(currentRecipe)        : null;

        inputName  = (loaded != null) ? loaded.id()
                : (currentRecipe != null ? currentRecipe.uiInputDisplayName() : "");
        outputName = (currentRecipe != null) ? currentRecipe.output.id() : "";

        if (inputIcon == null)  Gdx.app.log("MACHINE_MENU","Input icon is NULL for "+inputName);
        if (outputIcon == null) Gdx.app.log("MACHINE_MENU","Output icon is NULL for "+outputName);
    }

    private static Object callNoArg(Object target, String... names) {
        if (target == null) return null;
        for (String m : names) {
            try {
                Method md = target.getClass().getMethod(m);
                md.setAccessible(true);
                Object v = md.invoke(target);
                if (v != null) return v;
            } catch (Throwable ignored) {}
        }
        return null;
    }

    private static class UIButton {
        private String label;
        private float x, y, w, h;
        private boolean enabled = true;
        private Runnable onClick;

        UIButton(String label) { this.label = label; }

        void setBounds(float x, float y, float w, float h) { this.x = x; this.y = y; this.w = w; this.h = h; }
        void setEnabled(boolean e) { this.enabled = e; }
        void onClick(Runnable r) { this.onClick = r; }

        void draw(SpriteBatch batch, BitmapFont font, Texture bg, float mx, float my, boolean justPressed) {
            boolean hover = enabled && mx >= x && mx <= x + w && my >= y && my <= y + h;
            if (!enabled)      batch.setColor(1f, 1f, 1f, 0.35f);
            else if (hover)    batch.setColor(1f, 1f, 1f, 0.95f);
            else               batch.setColor(1f, 1f, 1f, 0.75f);
            batch.draw(bg, x, y, w, h);
            batch.setColor(1f, 1f, 1f, 1f);

            GlyphLayout gl = new GlyphLayout(font, label);
            float tx = x + (w - gl.width)/2f;
            float ty = y + (h + gl.height)/2f - 2f;
            font.draw(batch, gl, tx, ty);

            if (enabled && hover && justPressed && onClick != null) onClick.run();
        }
    }
}