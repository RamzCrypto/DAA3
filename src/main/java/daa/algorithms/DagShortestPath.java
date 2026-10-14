package daa.algorithms;

import daa.graph.*;
import daa.metrics.Metrics;
import java.util.*;

public class DagShortestPath {
    public static long run(Graph g, int[] topoOrder, Metrics m) {
        final long INF = Long.MAX_VALUE / 4;
        long[] dist = new long[g.n];
        int[] parent = new int[g.n];
        Arrays.fill(dist, INF);
        Arrays.fill(parent, -1);

        int[] inDeg = g.inDegrees();
        for (int i = 0; i < g.n; i++) if (inDeg[i] == 0) dist[i] = 0;

        for (int u : topoOrder) {
            if (dist[u] == INF) continue;
            for (Edge e : g.adj.get(u)) {
                m.edgeScans++;
                if (dist[u] + e.w < dist[e.v]) {
                    dist[e.v] = dist[u] + e.w;
                    parent[e.v] = u;
                }
            }
        }
        long sum = 0;
        for (long d : dist) if (d != INF) sum += d;
        return sum;
    }
}