package daa.graph;

import java.util.*;

public class Graph {
    public final int n;
    public final boolean directed;
    public final List<List<Edge>> adj;
    public final List<Edge> edges;

    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        this.adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        this.edges = new ArrayList<>();
    }

    public void addEdge(int u, int v, int w) {
        adj.get(u).add(new Edge(u, v, w));
        if (!directed) adj.get(v).add(new Edge(v, u, w));
        edges.add(new Edge(u, v, w));
    }

    public Graph reverse() {
        Graph r = new Graph(n, directed);
        for (int u = 0; u < n; u++) {
            for (Edge e : adj.get(u)) {
                r.adj.get(e.v).add(new Edge(e.v, e.u, e.w));
            }
        }
        for (Edge e : edges) r.edges.add(new Edge(e.v, e.u, e.w));
        return r;
    }

    public int[] inDegrees() {
        int[] deg = new int[n];
        for (int u = 0; u < n; u++)
            for (Edge e : adj.get(u)) deg[e.v]++;
        return deg;
    }
}