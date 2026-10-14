package daa.algorithms;

import daa.graph.*;
import daa.metrics.Metrics;
import java.util.*;

public class Kahn {
    public static int[] run(Graph g, Metrics m) {
        int[] inDeg = g.inDegrees();
        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < g.n; i++) if (inDeg[i] == 0) queue.add(i);
        int[] order = new int[g.n];
        int idx = 0;
        while (!queue.isEmpty()) {
            int u = queue.poll();
            order[idx++] = u;
            for (Edge e : g.adj.get(u)) {
                m.edgeScans++;
                if (--inDeg[e.v] == 0) queue.add(e.v);
            }
        }
        if (idx < g.n) return null;
        return order;
    }
}