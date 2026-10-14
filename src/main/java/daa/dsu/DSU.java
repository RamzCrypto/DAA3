package daa.dsu;

import daa.metrics.Metrics;

public class DSU {
    private final int[] parent;
    private final int[] rank;
    private final int[] size;
    private int components;
    private final Metrics metrics;
    private final boolean useSize;

    public DSU(int n, Metrics metrics, boolean useSize) {
        this.parent = new int[n];
        this.rank = new int[n];
        this.size = new int[n];
        this.components = n;
        this.metrics = metrics;
        this.useSize = useSize;
        for (int i = 0; i < n; i++) { parent[i] = i; rank[i] = 0; size[i] = 1; }
    }

    public int find(int x) {
        metrics.dsuOps++;
        int root = x;
        while (parent[root] != root) root = parent[root];
        while (parent[x] != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    public boolean union(int x, int y) {
        metrics.dsuOps++;
        int rx = find(x), ry = find(y);
        if (rx == ry) return false;
        if (useSize) {
            if (size[rx] < size[ry]) { int t = rx; rx = ry; ry = t; }
            parent[ry] = rx;
            size[rx] += size[ry];
        } else {
            if (rank[rx] < rank[ry]) { int t = rx; rx = ry; ry = t; }
            parent[ry] = rx;
            if (rank[rx] == rank[ry]) rank[rx]++;
        }
        components--;
        metrics.successfulUnions++;
        return true;
    }

    public int components() { return components; }
}