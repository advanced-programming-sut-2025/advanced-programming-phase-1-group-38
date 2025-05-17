package controllers;

import models.GameMap;
import models.Tile;
import models.Position;

import java.util.*;

public class PathFinder {
    private final GameMap map;
    private static final int[][] DIRS = {
        { 0,  1}, { 1,  0}, { 0, -1}, {-1,  0},
        { 1,  1}, { 1, -1}, {-1,  1}, {-1, -1}
    };

    public PathFinder(GameMap map) {
        this.map = map;
    }

    public List<Position> findPath(Position start, Position goal) {
        var open = new PriorityQueue<Node>(Comparator.comparingInt(n -> n.f));
        Map<Position,Node> all = new HashMap<>();

        Node s = new Node(start, 0, heuristic(start,goal), null);
        open.add(s);
        all.put(start, s);

        while (!open.isEmpty()) {
            Node cur = open.poll();
            if (cur.pos.equals(goal)) {
                List<Position> path = new ArrayList<>();
                for (Node n = cur; n!=null; n=n.parent)
                    path.add(n.pos);
                Collections.reverse(path);
                return path;
            }
            for (var d : DIRS) {
                Position np = new Position(cur.pos.getX()+d[0], cur.pos.getY()+d[1]);
                Tile t = map.getTile(np);
                if (t == null || !t.isWalkable()) continue;

                int g2 = cur.g + 1;
                Node succ = all.get(np);
                if (succ == null || g2 < succ.g) {
                    int h2 = heuristic(np, goal);
                    Node nn = new Node(np, g2, h2, cur);
                    all.put(np, nn);
                    open.remove(succ);
                    open.add(nn);
                }
            }
        }

        return null;
    }

    private int heuristic(Position a, Position b) {
        return Math.abs(a.getX()-b.getX()) + Math.abs(a.getY()-b.getY());
    }

    private static class Node {
        final Position pos;
        final int g, f;
        final Node parent;
        Node(Position pos, int g, int h, Node parent) {
            this.pos = pos;
            this.g = g;
            this.f = g+h;
            this.parent = parent;
        }
    }
}
