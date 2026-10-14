package daa.algorithms;

import daa.graph.*;
import daa.metrics.Metrics;
import java.util.*;

public class Condensation {
    public static long longestPath(Graph g, Metrics m) {
        int[] comp = new int[g.n];
        Arrays.fill(comp, -1);
        int nc = 0;
        boolean[] visited = new boolean[g.n];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < g.n; i++) {
            if (!visited[i]) {
                stack.push(i);
                visited[i] = true;
                while (!stack.isEmpty()) {
                    int u = stack.pop();
                    comp[u] = nc;
                    for (Edge e : g.adj.get(u)) {
                        m.edgeScans++;
                        if (!visited[e.v]) { visited[e.v] = true; stack.push(e.v); }
                    }
                }
                nc++;
            }
        }
        List<List<Edge>> cadj = new ArrayList<>();
        for (int i = 0; i < nc; i++) cadj.add(new ArrayList<>());
        for (int u = 0; u < g.n; u++) {
            for (Edge e : g.adj.get(u)) {
                if (comp[u] != comp[e.v]) {
                    cadj.get(comp[u]).add(new Edge(comp[u], comp[e.v], e.w));
                }
            }
        }
        Graph cg = new Graph(nc, true);
        for (int u = 0; u < nc; u++) for (Edge e : cadj.get(u)) cg.addEdge(e.u, e.v, e.w);
        int[] topo = Kahn.run(cg, m);
        if (topo == null) return -1;
        long[] dist = new long[nc];
        Arrays.fill(dist, 0);
        for (int u : topo) {
            for (Edge e : cg.adj.get(u)) {
                m.edgeScans++;
                if (dist[u] + e.w > dist[e.v]) dist[e.v] = dist[u] + e.w;
            }
        }
        long best = 0;
        for (long d : dist) best = Math.max(best, d);
        return best;
    }
}