package io.github.StardewValley.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import io.github.StardewValley.Main;
import io.github.StardewValley.controllers.WorldController;

import java.util.ArrayList;
import java.util.List;

public class WorldView implements Screen {
    private final OrthographicCamera camera;
    private final Screen previous;
    private final WorldController worldController;

    // One slice per player (these should be their fixed outdoor/home maps)
    private final List<PlayerWorldSlice> worlds;
    private final List<OrthogonalTiledMapRenderer> renderers = new ArrayList<>();

    // Center NPC slice
    private final PlayerWorldSlice npcSlice;
    private final OrthogonalTiledMapRenderer npcRenderer;

    private final SpriteBatch batch;

    private static final float TILE = 16f;

    public WorldView(Screen previous,
                     List<PlayerWorldSlice> worlds,
                     PlayerWorldSlice npcSlice,
                     WorldController worldController) {
        this.previous = previous;
        this.worlds = worlds;
        this.npcSlice = npcSlice;
        this.npcRenderer = npcSlice.getRenderer();
        this.worldController = worldController;

        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
        camera.zoom = 3.5f;

        // Pre-cache the renderers for the four slices
        for (PlayerWorldSlice w : worlds) {
            renderers.add(w.getRenderer());
        }

        this.batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        // Back out to previous screen
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            Main.getMain().setScreen(previous);
            return;
        }

        // Advance simulation (only active player acts, per your WorldController)
        if (worldController != null) {
            worldController.updateAll(delta);
        }

        final int sw = Gdx.graphics.getWidth();
        final int sh = Gdx.graphics.getHeight();

        // Layout: a centered 2x2 grid of panels, plus a center overlay for NPC map when needed
        final int GAP   = 40;                  // horizontal/vertical gap between panels
        final int CELLW = (int)(sw * 0.9f);   // panel width
        final int CELLH = (int)(sh * 0.9f);   // panel height

        final int GRIDW = CELLW * 2 + GAP;
        final int GRIDH = CELLH * 2 + GAP;

        final int gx = (sw - GRIDW) / 2;      // top-left X of the grid
        final int gy = (sh - GRIDH) / 2;      // top-left Y of the grid

        Gdx.gl.glClearColor(0.74f, 0.91f, 0.95f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);

        // Viewport rectangles (x, y, w, h). LibGDX origin is bottom-left.
        int[][] quads = new int[][]{
            { gx + 550 ,             gy + 360 + CELLH + GAP, CELLW, CELLH }, // top-left
            { gx + 720 + CELLW + GAP,gy + 360 + CELLH + GAP, CELLW, CELLH }, // top-right
            { gx + 550,              gy + 360,               CELLW, CELLH }, // bottom-left
            { gx + 720 + CELLW + GAP,gy + 360,               CELLW, CELLH }  // bottom-right
        };

        boolean npcMapOccupied = false; // track if any player is currently in NPC map

        for (int i = 0; i < 4; i++) {
            PlayerWorldSlice slice = worlds.get(i);
            OrthogonalTiledMapRenderer r = renderers.get(i);

            io.github.StardewValley.controllers.GameController gc = worldController.getAllControllers()[i];
            String curPath = gc.getCurrentMapPath();

            // true only when the player is actually on the fixed map this slice represents
            boolean onThisSlice = (curPath != null && curPath.equals(slice.getMapId()));

            // NPC map check (optional if you also have a dedicated center panel)
            boolean inNpc = (curPath != null && curPath.endsWith("npcMap.tmx"));

            // we draw the player here ONLY when they are on their outdoor/home slice
            boolean drawPlayerHere = onThisSlice && !inNpc;

            // ----- panel viewport/scissor (unchanged) -----
            int vx = quads[i][0], vy = quads[i][1], vw = quads[i][2], vh = quads[i][3];
            Gdx.gl.glViewport(vx, vy, vw, vh);
            Gdx.gl.glScissor(vx, vy, vw, vh);

            float mapW = slice.getMapWidthTiles() * TILE;
            float mapH = slice.getMapHeightTiles() * TILE;
            float sx = vw / mapW, sy = vh / mapH;
            float scale  = Math.min(sx, sy);
            float worldW = vw / scale, worldH = vh / scale;
            float viewX  = (mapW - worldW) * 0.5f, viewY = (mapH - worldH) * 0.5f;

            com.badlogic.gdx.math.Matrix4 proj =
                new com.badlogic.gdx.math.Matrix4().setToOrtho2D(viewX, viewY, worldW, worldH);

            r.setView(proj, viewX, viewY, worldW, worldH);
            r.render();

            batch.setProjectionMatrix(proj);
            batch.begin();
            slice.renderEntities(batch, drawPlayerHere); // <- key change
            batch.end();
        }

        // Center NPC panel: draw only if no one is currently in the NPC map
        int npcW = sw;
        int npcH = sh;
        int npcX = (sw - npcW) / 2 + 635;
        int npcY = (sh - npcH) / 2 + 360;

        Gdx.gl.glViewport(npcX, npcY, npcW, npcH);
        Gdx.gl.glScissor(npcX, npcY, npcW, npcH);

        float npcMapW = npcSlice.getMapWidthTiles() * TILE;
        float npcMapH = npcSlice.getMapHeightTiles() * TILE;

        float npcScaleX = npcW / npcMapW, npcScaleY = npcH / npcMapH;
        float npcScale = Math.min(npcScaleX, npcScaleY);
        float npcWorldW = npcW / npcScale;
        float npcWorldH = npcH / npcScale;
        float npcViewX = (npcMapW - npcWorldW) * 0.5f;
        float npcViewY = (npcMapH - npcWorldH) * 0.5f;

        com.badlogic.gdx.math.Matrix4 npcProj =
            new com.badlogic.gdx.math.Matrix4().setToOrtho2D(npcViewX, npcViewY, npcWorldW, npcWorldH);

        npcRenderer.setView(npcProj, npcViewX, npcViewY, npcWorldW, npcWorldH);
        npcRenderer.render();

        // Draw any players who are (currently) in the NPC map here
        batch.setProjectionMatrix(npcProj);
        batch.begin();

        worldController.npc().renderOn(batch, "maps/npcMap.tmx");

        for (io.github.StardewValley.controllers.GameController gc : worldController.getAllControllers()) {
            String cur = gc.getCurrentMapPath();
            if (cur != null && cur.endsWith("npcMap.tmx")) {
                gc.render(batch);
            }
        }
        batch.end();

        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glViewport(0, 0, sw, sh);
    }

    @Override public void resize(int w, int h) {
        camera.setToOrtho(false, w, h);
        camera.update();
    }

    @Override public void dispose() {
        for (OrthogonalTiledMapRenderer r : renderers) r.dispose();
        npcRenderer.dispose();
        batch.dispose();
    }

    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
}
