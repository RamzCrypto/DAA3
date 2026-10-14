package daa.algorithms;

import daa.dsu.DSU;
import daa.graph.*;
import daa.metrics.Metrics;
import java.util.*;

public class Kruskal {
    public static long run(Graph g, Metrics m) {
        List<Edge> edges = new ArrayList<>(g.edges);
        m.comparisons += edges.size();
        edges.sort((a, b) -> { m.comparisons++; return Integer.compare(a.w, b.w); });
        DSU dsu = new DSU(g.n, m, true);
        long total = 0;
        int used = 0;
        for (Edge e : edges) {
            m.edgeScans++;
            if (dsu.find(e.u) != dsu.find(e.v)) {
                dsu.union(e.u, e.v);
                total += e.w;
                used++;
                if (used == g.n - 1) break;
            }
        }
        return total;
    }
}