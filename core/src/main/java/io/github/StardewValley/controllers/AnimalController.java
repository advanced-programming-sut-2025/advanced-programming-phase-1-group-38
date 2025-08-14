// controllers/AnimalController.java (GC-local)
package io.github.StardewValley.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.StardewValley.models.Animal;

import java.util.*;

public class AnimalController {
    private final Map<String, List<Animal>> byMap = new HashMap<>();

    public void add(Animal a) {
        byMap.computeIfAbsent(a.getMapId(), k -> new ArrayList<>()).add(a);
    }
    public void remove(Animal a) {
        var list = byMap.get(a.getMapId());
        if (list != null) list.remove(a);
    }
    public void update(float dt) {
        for (var list : byMap.values()) for (var a : list) a.update(dt);
    }
    public void renderOn(SpriteBatch batch, String mapId) {
        var list = byMap.get(mapId); if (list == null) return;
        for (var a : list) a.render(batch);
    }
    public Animal closestOn(String mapId, float x, float y, float r) {
        var list = byMap.get(mapId); if (list == null) return null;
        float r2 = r*r; Animal best=null; float bestD2=r2;
        for (var a : list) {
            float dx=a.getX()-x, dy=a.getY()-y, d2=dx*dx+dy*dy;
            if (d2<=bestD2){bestD2=d2; best=a;}
        }
        return best;
    }

    public void advanceAll(float hours) {
        if (hours <= 0f) return;
        for (var list : byMap.values()) {
            for (var a : list) a.advanceGameHours(hours);
        }
    }

    public List<Animal> allOn(String mapId) { return byMap.getOrDefault(mapId, List.of()); }
}
