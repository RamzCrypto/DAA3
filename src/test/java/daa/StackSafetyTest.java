package daa;

import daa.algorithms.Kosaraju;
import daa.algorithms.Tarjan;
import daa.graph.Graph;
import daa.metrics.Metrics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StackSafetyTest {

    @Test
    public void kosarajuOnLongPathDoesNotOverflow() {
        int n = 100_000;
        Graph g = new Graph(n, true);
        for (int i = 0; i + 1 < n; i++) g.addEdge(i, i + 1, 1);

        Metrics m = new Metrics();
        int components = assertDoesNotThrow(() -> Kosaraju.run(g, m));
        assertEquals(n, components);
        assertTrue(m.maxDepth > 0 && m.maxDepth <= n);
    }

    @Test
    public void tarjanOnLongPathDoesNotOverflow() {
        int n = 100_000;
        Graph g = new Graph(n, true);
        for (int i = 0; i + 1 < n; i++) g.addEdge(i, i + 1, 1);

        Metrics m = new Metrics();
        int components = assertDoesNotThrow(() -> Tarjan.run(g, m));
        assertEquals(n, components);
        assertTrue(m.maxDepth > 0 && m.maxDepth <= n);
    }

    @Test
    public void oneGiantSccDoesNotOverflow() {
        int n = 100_000;
        Graph g = new Graph(n, true);
        for (int i = 0; i < n; i++) g.addEdge(i, (i + 1) % n, 1);

        Metrics m = new Metrics();
        int kos = assertDoesNotThrow(() -> Kosaraju.run(g, m));
        assertEquals(1, kos);

        Metrics m2 = new Metrics();
        int tar = assertDoesNotThrow(() -> Tarjan.run(g, m2));
        assertEquals(1, tar);
    }
}