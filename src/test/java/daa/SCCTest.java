package daa;

import daa.algorithms.Kosaraju;
import daa.algorithms.Tarjan;
import daa.graph.Graph;
import daa.metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SCCTest {

    @Test
    public void kosarajuMatchesBruteForce() {
        Random rnd = new Random(11);
        for (int trial = 0; trial < 30; trial++) {
            int n = 5 + rnd.nextInt(50);
            Graph g = new Graph(n, true);
            int m = n * 2;
            for (int k = 0; k < m; k++) {
                int a = rnd.nextInt(n);
                int b = rnd.nextInt(n);
                g.addEdge(a, b, 1);
            }
            int brute = bruteForce(g);
            int kos = Kosaraju.run(g, new Metrics());
            int tar = Tarjan.run(g, new Metrics());
            assertEquals(brute, kos);
            assertEquals(brute, tar);
        }
    }

    private int bruteForce(Graph g) {
        int n = g.n;
        int[] comp = new int[n];
        Arrays.fill(comp, -1);
        int c = 0;
        for (int s = 0; s < n; s++) {
            if (comp[s] != -1) continue;
            List<Integer> fwd = reach(g, s, false);
            List<Integer> bwd = reach(g, s, true);
            Set<Integer> compSet = new HashSet<>(fwd);
            compSet.retainAll(new HashSet<>(bwd));
            if (!compSet.isEmpty()) {
                for (int v : compSet) comp[v] = c;
                c++;
            }
        }
        return c;
    }

    private List<Integer> reach(Graph g, int start, boolean reverse) {
        boolean[] vis = new boolean[g.n];
        Deque<Integer> stack = new ArrayDeque<>();
        List<Integer> out = new ArrayList<>();
        stack.push(start);
        vis[start] = true;
        while (!stack.isEmpty()) {
            int u = stack.pop();
            out.add(u);
            if (reverse) {
                for (int w = 0; w < g.n; w++) {
                    for (var e : g.adj.get(w)) {
                        if (e.v == u && !vis[w]) {
                            vis[w] = true;
                            stack.push(w);
                        }
                    }
                }
            } else {
                for (var e : g.adj.get(u)) {
                    if (!vis[e.v]) {
                        vis[e.v] = true;
                        stack.push(e.v);
                    }
                }
            }
        }
        return out;
    }
}