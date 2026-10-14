package daa.algorithms;

import daa.graph.*;
import daa.metrics.Metrics;
import java.util.*;

public class Prim {
    public static long run(Graph g, Metrics m) {
        boolean[] inTree = new boolean[g.n];
        long total = 0;
        int added = 0;
        for (int start = 0; start < g.n; start++) {
            if (inTree[start]) continue;
            PriorityQueue<Edge> pq = new PriorityQueue<>((a, b) -> {
                m.comparisons++;
                return Integer.compare(a.w, b.w);
            });
            pq.add(new Edge(start, start, 0));
            while (!pq.isEmpty()) {
                Edge e = pq.poll();
                m.edgeScans++;
                int v = e.v;
                if (inTree[v]) continue;
                inTree[v] = true;
                total += e.w;
                added++;
                for (Edge ne : g.adj.get(v)) {
                    if (!inTree[ne.v]) pq.add(ne);
                }
            }
        }
        return total;
    }
}