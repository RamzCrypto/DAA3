package daa.algorithms;

import daa.graph.*;
import daa.metrics.Metrics;
import java.util.*;

public class Kosaraju {
    public static int run(Graph g, Metrics m) {
        boolean[] visited = new boolean[g.n];
        int[] order = new int[g.n];
        int[] idx = {0};
        for (int i = 0; i < g.n; i++) {
            if (!visited[i]) iterativeDfs1(g, i, visited, order, idx, m);
        }
        Graph rev = g.reverse();
        Arrays.fill(visited, false);
        int components = 0;
        for (int i = g.n - 1; i >= 0; i--) {
            int v = order[i];
            if (!visited[v]) {
                iterativeDfs2(rev, v, visited, m);
                components++;
            }
        }
        return components;
    }

    private static void iterativeDfs1(Graph g, int start, boolean[] visited, int[] order, int[] idx, Metrics m) {
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[]{start, 0});
        visited[start] = true;
        int depth = 1;
        while (!stack.isEmpty()) {
            int[] top = stack.peek();
            int u = top[0];
            if (top[1] < g.adj.get(u).size()) {
                Edge e = g.adj.get(u).get(top[1]++);
                m.edgeScans++;
                if (!visited[e.v]) {
                    visited[e.v] = true;
                    stack.push(new int[]{e.v, 0});
                    depth = Math.max(depth, stack.size());
                }
            } else {
                stack.pop();
                order[idx[0]++] = u;
            }
        }
        m.maxDepth = Math.max(m.maxDepth, depth);
    }

    private static void iterativeDfs2(Graph g, int start, boolean[] visited, Metrics m) {
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(start);
        visited[start] = true;
        int depth = 1;
        while (!stack.isEmpty()) {
            int u = stack.pop();
            for (Edge e : g.adj.get(u)) {
                m.edgeScans++;
                if (!visited[e.v]) {
                    visited[e.v] = true;
                    stack.push(e.v);
                    depth = Math.max(depth, stack.size());
                }
            }
        }
        m.maxDepth = Math.max(m.maxDepth, depth);
    }
}