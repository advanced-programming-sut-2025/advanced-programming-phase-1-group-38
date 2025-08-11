package io.github.StardewValley.views;

import com.badlogic.gdx.graphics.Texture;
import io.github.StardewValley.models.Artisan.MachineInstance;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.StardewValley.models.GameAssetManager;


public class ArtisanOverlay {
    private boolean visible = false;
    private MachineInstance target;

    private final Texture panelBg;
    private final Texture px;

    public ArtisanOverlay() {
        Texture t;
        try { t = new Texture("inventory/panel_bg.png"); }
        catch (Exception e) { t = null; }
        panelBg = t;

        Pixmap pm = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE); pm.fill();
        px = new Texture(pm); pm.dispose();
    }

    public void show(MachineInstance m) { target = m; visible = true; }
    public void hide() { visible = false; target = null; }
    public boolean isVisible() { return visible; }

    public void render(SpriteBatch batch, int screenW, int screenH) {
        if (!visible || target == null) return;

        float w = 360, h = 140;
        float x = screenW - w - 20, y = 20;

        if (panelBg != null) batch.draw(panelBg, x, y, w, h);
        tint(batch, x, y, w, h, 0,0,0, panelBg==null?0.55f:0.15f);

        BitmapFont big = GameAssetManager.getGameAssetManager().getBigFont();
        BitmapFont small = GameAssetManager.getGameAssetManager().getSmallFont();

        String title = target.type.id() + " — " + target.state();
        GlyphLayout gl = new GlyphLayout(big, title);
        big.draw(batch, gl, x + 16, y + h - 12);

        float bx = x + 16, by = y + 60, bw = w - 32, bh = 12;
        // bar bg
        tint(batch, bx, by, bw, bh, 0.1f,0.12f,0.14f, 0.9f);
        // bar fill
        float p = target.progress01();
        tint(batch, bx+2, by+2, (bw-4)*p, bh-4, 0.25f,0.85f,0.35f, 1f);

        String hint = "Enter: Collect   X: Cancel   G: Cheat (finish)";
        small.draw(batch, hint, x + 16, y + 30);
    }

    private void tint(SpriteBatch b, float x,float y,float w,float h,float r,float g,float bl,float a){
        Color old = b.getColor(); b.setColor(r,g,bl,a); b.draw(px,x,y,w,h); b.setColor(old);
    }
}
