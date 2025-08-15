// controllers/AnimalController.java (GC-local)
package io.github.StardewValley.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.StardewValley.models.Animal;
import io.github.StardewValley.models.AnimalSpecies;
import io.github.StardewValley.models.BarnPen;

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

    // Call to make all cows head inside the first barn on this map (if gate open)
    public void sendCowsToBarn(String mapId, java.util.List<BarnPen> barns) {
        if (barns == null || barns.isEmpty()) return;
        BarnPen pen = barns.get(0);
        if (!pen.gateOpen) return;

        float cx = pen.bounds.x + pen.bounds.width  * 0.5f;
        float cy = pen.bounds.y + pen.bounds.height * 0.5f;

        for (Animal a : allOn(mapId)) {
            if (a.getSpecies() == AnimalSpecies.COW) {
                a.moveTo(cx, cy);
            }
        }
    }

    // Light containment: if gate is closed, clamp animals that are inside to the pen bounds
    private void confineIfNeeded(Animal a, BarnPen pen) {
        if (pen == null || pen.gateOpen) return;
        if (!pen.contains(a.getX(), a.getY())) return; // already outside; do nothing

        float margin = 8f; // keep a tiny margin from fence texture
        float nx = Math.max(pen.bounds.x + margin,
            Math.min(a.getX(), pen.bounds.x + pen.bounds.width  - margin));
        float ny = Math.max(pen.bounds.y + margin,
            Math.min(a.getY(), pen.bounds.y + pen.bounds.height - margin));
        if (nx != a.getX() || ny != a.getY()) a.setPosition(nx, ny);
    }

    // Option A: a new update that knows barns
    public void updateWithBarns(String mapId, float dt, java.util.List<BarnPen> barns) {
        var list = byMap.get(mapId); if (list == null) return;
        BarnPen pen = (barns == null || barns.isEmpty()) ? null : barns.get(0);
        for (var a : list) {
            a.update(dt);
            if (pen != null) confineIfNeeded(a, pen);
        }
    }

    // AnimalController.java
    public void sendCowsOutOfBarn(String mapId, java.util.List<BarnPen> barns) {
        if (barns == null || barns.isEmpty()) return;
        BarnPen pen = barns.get(0);
        if (!pen.gateOpen) return; // only if gate is open

        // target point just OUTSIDE the gate
        float outX = pen.gate.x + pen.gate.width * 0.5f;
        float outY = pen.gate.y - 12f; // a bit below the bottom edge

        for (Animal a : allOn(mapId)) {
            if (a.getSpecies() == AnimalSpecies.COW && pen.contains(a.getX(), a.getY())) {
                a.moveTo(outX, outY);
            }
        }
    }


    public List<Animal> allOn(String mapId) { return byMap.getOrDefault(mapId, List.of()); }
}
