// core/src/main/java/io/github/StardewValley/views/ControlsOverlay.java
package io.github.StardewValley.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.LinkedHashMap;
import java.util.Map;

/** Minimal keybinds/help overlay (toggle with H). */
public class ControlsOverlay {
    private boolean visible = false;

    private final Texture panelBg;    // optional, if you have a bg panel
    private final Texture px;         // 1x1 white for tint rects
    private final Map<String, String> lines = new LinkedHashMap<>();

    public ControlsOverlay() {
        // try use your panel; fallback to null
        Texture tmp;
        try { tmp = new Texture("inventory/panel_bg.png"); }
        catch (Exception e) { tmp = null; }
        panelBg = tmp;

        Pixmap pm = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE); pm.fill();
        px = new Texture(pm); pm.dispose();

        // Keybinds (مطابق کد فعلی پروژه)
        lines.put("WASD / Arrow Keys", "");
        lines.put("Mouse Left",        "");
        lines.put("Mouse Wheel",       "");
        lines.put("E یا ESC",          "Inventory");
        lines.put("B",                 "Crafting");
        lines.put("C (in house)", "Cooking");
        lines.put("F",                 "Journal");
        lines.put("K",                 "Shop");
        lines.put("R",                 "");
        lines.put("H",                 "help");
    }

    public void toggle() { visible = !visible; }
    public boolean isVisible() { return visible; }

    public void render(SpriteBatch batch, BitmapFont titleFont, BitmapFont textFont,
                       float screenW, float screenH) {
        if (!visible) return;

        float w = 520, h = 360;
        float x = screenW - w - 24;
        float y = screenH - h - 24;

        // panel
        if (panelBg != null) batch.draw(panelBg, x, y, w, h);
        // subtle glass bg
        tint(batch, x, y, w, h, 0f,0f,0f, panelBg==null?0.55f:0.15f);

        // title
        GlyphLayout tl = new GlyphLayout(titleFont, "Controls");
        titleFont.draw(batch, tl, x + 20, y + h - 18);

        // items
        float rowY = y + h - 54;
        for (Map.Entry<String, String> e : lines.entrySet()) {
            String left  = e.getKey();
            String right = e.getValue();
            // left
            textFont.draw(batch, left, x + 24, rowY);
            // right (faded)
            textFont.setColor(0.9f, 0.95f, 1f, 0.9f);
            textFont.draw(batch, "— " + right, x + 260, rowY);
            textFont.setColor(1f,1f,1f,1f);
            rowY -= 26;
        }
    }

    private void tint(SpriteBatch b, float x, float y, float w, float h, float r,float g,float bl,float a) {
        Color old = b.getColor();
        b.setColor(r,g,bl,a);
        b.draw(px, x, y, w, h);
        b.setColor(old);
    }

    public void dispose() { if (panelBg != null) panelBg.dispose(); px.dispose(); }
}