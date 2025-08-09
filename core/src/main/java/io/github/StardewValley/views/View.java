package io.github.StardewValley.views;

public interface View {
    void show();
    void hide();
    void act(float delta);
    void dispose();
}
