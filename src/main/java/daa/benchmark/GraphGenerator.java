package daa.benchmark;

import daa.graph.Graph;
import java.util.*;

public class GraphGenerator {

    public static Graph undirected(int n, int m) {
        Random rnd = new Random(42);
        int[] perm = new int[n];
        for (int i = 0; i < n; i++) perm[i] = i;
        for (int i = n - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int t = perm[i]; perm[i] = perm[j]; perm[j] = t;
        }
        Graph g = new Graph(n, false);
        for (int i = 1; i < n; i++) {
            int a = perm[rnd.nextInt(i)];
            int b = perm[i];
            g.addEdge(a, b, 1 + rnd.nextInt(1000));
        }
        for (int i = n - 1; i < m; i++) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);
            g.addEdge(a, b, 1 + rnd.nextInt(1000));
        }
        return g;
    }

    public static Graph directed(int n, int m) {
        Random rnd = new Random(42);
        Graph g = new Graph(n, true);
        for (int i = 0; i < m; i++) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);
            g.addEdge(a, b, 1 + rnd.nextInt(1000));
        }
        return g;
    }

    public static Graph dag(int n, int m) {
        Random rnd = new Random(42);
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) rank[i] = i;
        for (int i = n - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int t = rank[i]; rank[i] = rank[j]; rank[j] = t;
        }
        Graph g = new Graph(n, true);
        for (int i = 0; i < m; i++) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);
            if (a == b) continue;
            if (rank[a] > rank[b]) { int t = a; a = b; b = t; }
            g.addEdge(a, b, 1 + rnd.nextInt(1000));
        }
        return g;
    }
}