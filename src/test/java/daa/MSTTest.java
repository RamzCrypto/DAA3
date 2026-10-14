package daa;

import daa.algorithms.*;
import daa.graph.*;
import daa.metrics.Metrics;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class MSTTest {
    @Test
    public void kruskalAndPrimAgree() {
        Random rnd = new Random(7);
        for (int trial = 0; trial < 100; trial++) {
            int n = 5 + rnd.nextInt(50);
            Graph g = new Graph(n, false);
            for (int i = 1; i < n; i++) g.addEdge(rnd.nextInt(i), i, rnd.nextInt(100));
            for (int k = 0; k < n; k++) {
                int a = rnd.nextInt(n), b = rnd.nextInt(n);
                if (a != b) g.addEdge(a, b, rnd.nextInt(100));
            }
            long k = Kruskal.run(g, new Metrics());
            long p = Prim.run(g, new Metrics());
            assertEquals(k, p);
        }
    }
}