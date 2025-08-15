// models/BarnPen.java
package io.github.StardewValley.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class BarnPen {
    public final String mapId;
    public final Rectangle bounds;   // full outer rectangle (pixels)
    public final Rectangle gate;     // small clickable gate rect (pixels)
    public boolean gateOpen;
    private final String textureKey; // e.g. "barn.png"

    public BarnPen(String mapId,
                   float x, float y, float w, float h,
                   float gateX, float gateY, float gateW, float gateH,
                   String textureKey,
                   boolean gateOpen) {
        this.mapId = mapId;
        this.bounds = new Rectangle(x, y, w, h);
        this.gate   = new Rectangle(gateX, gateY, gateW, gateH);
        this.textureKey = textureKey;
        this.gateOpen = gateOpen;
    }

    public void render(SpriteBatch batch) {
        Texture tex = GameAssetManager.getGameAssetManager().getTexture(textureKey);
        // The PNG should be a rectangle frame with transparent interior; scale it to bounds.
        batch.draw(tex, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public boolean contains(float x, float y) { return bounds.contains(x, y); }
    public boolean hitGate(float x, float y)  { return gate.contains(x, y); }
}
