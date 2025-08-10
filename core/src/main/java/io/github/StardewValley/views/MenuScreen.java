package io.github.StardewValley.views;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen extends ScreenAdapter {
    private final Stage stage = new Stage(new ScreenViewport());
    private final Skin  skin  = new Skin(Gdx.files.internal("uiskin.json"));
    private View current;

    public void setView(View v) {
        if (current != null) current.hide();
        current = v;
        current.show();
    }
    public Stage getStage() { return stage; }
    public Skin  getSkin()  { return skin; }

    @Override public void render(float delta) {
        ScreenUtils.clear(0,0,0,1);
        if (current != null) current.act(delta);
        stage.act(delta);
        stage.draw();
    }
    @Override public void dispose() { stage.dispose(); skin.dispose(); }
}
