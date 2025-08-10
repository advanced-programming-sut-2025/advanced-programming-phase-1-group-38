package io.github.StardewValley.views;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** Very lightweight journal overlay (F) with placeholder text and a heading. */
public class JournalOverlay {
    private boolean visible = false;

    public void toggle() { visible = !visible; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean v) { visible = v; }

    public void render(SpriteBatch batch, BitmapFont big, BitmapFont small, float screenW, float screenH) {
        if (!visible) return;
        String title = "Journal";
        GlyphLayout t = new GlyphLayout(big, title);
        big.draw(batch, t, (screenW - t.width) / 2f, screenH - 40);

        String body = "- Quests coming soon\n- Daily tasks coming soon";
        GlyphLayout b = new GlyphLayout(small, body);
        small.draw(batch, b, (screenW - b.width) / 2f, screenH - 90);
    }
}
