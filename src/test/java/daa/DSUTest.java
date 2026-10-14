package daa;

import daa.dsu.DSU;
import daa.metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DSUTest {

    @Test
    public void randomUnionsMatchReference() {
        Random rnd = new Random(123);
        for (int trial = 0; trial < 20; trial++) {
            int n = 50 + rnd.nextInt(100);
            DSU dsu = new DSU(n, new Metrics(), true);
            int[] parent = new int[n];
            int[] size = new int[n];
            for (int i = 0; i < n; i++) { parent[i] = i; size[i] = 1; }

            for (int k = 0; k < 200; k++) {
                int a = rnd.nextInt(n);
                int b = rnd.nextInt(n);
                dsu.union(a, b);

                int ra = find(parent, a);
                int rb = find(parent, b);
                if (ra != rb) {
                    if (size[ra] < size[rb]) { int t = ra; ra = rb; rb = t; }
                    parent[rb] = ra;
                    size[ra] += size[rb];
                }
            }

            for (int i = 0; i < n; i++) {
                assertEquals(find(parent, i), dsu.find(i),
                        "find mismatch at trial " + trial + ", vertex " + i);
            }

            Set<Integer> roots = new HashSet<>();
            for (int i = 0; i < n; i++) roots.add(dsu.find(i));
            assertEquals(roots.size(), dsu.components());
        }
    }

    @Test
    public void unionBySizeAndRankBothWork() {
        for (boolean useSize : new boolean[]{true, false}) {
            DSU dsu = new DSU(10, new Metrics(), useSize);
            dsu.union(0, 1);
            dsu.union(2, 3);
            dsu.union(0, 2);
            dsu.union(5, 6);
            dsu.union(7, 8);
            dsu.union(5, 7);
            assertEquals(4, dsu.components());
            assertEquals(dsu.find(0), dsu.find(3));
            assertEquals(dsu.find(5), dsu.find(8));
            assertNotEquals(dsu.find(0), dsu.find(5));
            assertNotEquals(dsu.find(0), dsu.find(9));
        }
    }

    private int find(int[] parent, int x) {
        while (parent[x] != x) x = parent[x];
        return x;
    }
}