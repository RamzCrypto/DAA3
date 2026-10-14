package daa;

import daa.algorithms.Kahn;
import daa.graph.Edge;
import daa.graph.Graph;
import daa.metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class TopoTest {

    @Test
    public void validOrderOnDag() {
        Random rnd = new Random(31);
        for (int trial = 0; trial < 30; trial++) {
            int n = 5 + rnd.nextInt(40);
            int m = n * 2;
            int[] rank = new int[n];
            for (int i = 0; i < n; i++) rank[i] = i;
            for (int i = n - 1; i > 0; i--) {
                int j = rnd.nextInt(i + 1);
                int t = rank[i]; rank[i] = rank[j]; rank[j] = t;
            }
            Graph g = new Graph(n, true);
            for (int k = 0; k < m; k++) {
                int a = rnd.nextInt(n), b = rnd.nextInt(n);
                if (a == b) continue;
                if (rank[a] > rank[b]) { int t = a; a = b; b = t; }
                g.addEdge(a, b, 1 + rnd.nextInt(10));
            }

            int[] order = Kahn.run(g, new Metrics());
            assertNotNull(order);
            assertEquals(n, order.length);

            int[] pos = new int[n];
            for (int i = 0; i < n; i++) pos[order[i]] = i;
            for (int u = 0; u < n; u++) {
                for (Edge e : g.adj.get(u)) {
                    assertTrue(pos[u] < pos[e.v]);
                }
            }
        }
    }

    @Test
    public void cycleIsDetected() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);
        g.addEdge(2, 3, 1);

        int[] order = Kahn.run(g, new Metrics());
        assertNull(order);
    }
}