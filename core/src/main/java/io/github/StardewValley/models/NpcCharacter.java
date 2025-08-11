package io.github.StardewValley.models;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class NpcCharacter {
    public final String id, name;
    private final String mapPath;
    private float x, y;

    private Animation<TextureRegion> currentAnim;
    private String currentKey = "";
    private float animTime = 0f;
    private boolean looping = true;

    public NpcCharacter(String id, String name, String mapPath, float x, float y) {
        this.id = id; this.name = name; this.mapPath = mapPath; this.x = x; this.y = y;
    }

    // keep your existing ctor that took TextureRegion if you still use it elsewhere

    public void setAnimation(String key, String basePath, int frames, float frameDuration, boolean loop) {
        if (!key.equals(currentKey)) {
            String cacheKey = "npc/" + id + "/" + key; // unique per NPC+state
            currentAnim = GameAssetManager.getGameAssetManager()
                .getFrameAnimation(cacheKey, basePath, frames, frameDuration, loop);
            currentKey = key;
            animTime = 0f;
            looping = loop;
        }
    }

    public void update(float dt) { animTime += dt; }

    public void render(SpriteBatch batch) {
        if (currentAnim == null) return;
        TextureRegion frame = currentAnim.getKeyFrame(animTime, looping);
        batch.draw(frame, x, y);
    }

    public TextureRegion getCurrentFrame() {
        return (currentAnim == null) ? null : currentAnim.getKeyFrame(animTime, looping);
    }

    public String getMapPath() { return mapPath; }
    public float getX() { return x; }
    public float getY() { return y; }
}
