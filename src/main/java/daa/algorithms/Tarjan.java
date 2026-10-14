package daa.algorithms;

import daa.graph.*;
import daa.metrics.Metrics;
import java.util.*;

public class Tarjan {
    public static int run(Graph g, Metrics m) {
        int[] index = new int[g.n];
        int[] lowlink = new int[g.n];
        boolean[] onStack = new boolean[g.n];
        Arrays.fill(index, -1);
        Deque<Integer> stack = new ArrayDeque<>();
        int[] counter = {0};
        int[] components = {0};

        for (int i = 0; i < g.n; i++) {
            if (index[i] == -1) {
                strongConnect(g, i, index, lowlink, onStack, stack, counter, components, m);
            }
        }
        return components[0];
    }

    private static void strongConnect(Graph g, int start, int[] index, int[] lowlink,
                                      boolean[] onStack, Deque<Integer> stack, int[] counter,
                                      int[] components, Metrics m) {
        Deque<int[]> callStack = new ArrayDeque<>();
        callStack.push(new int[]{start, 0});
        index[start] = lowlink[start] = counter[0]++;
        stack.push(start);
        onStack[start] = true;
        int depth = 1;

        while (!callStack.isEmpty()) {
            int[] top = callStack.peek();
            int u = top[0];
            if (top[1] < g.adj.get(u).size()) {
                Edge e = g.adj.get(u).get(top[1]++);
                m.edgeScans++;
                int v = e.v;
                if (index[v] == -1) {
                    index[v] = lowlink[v] = counter[0]++;
                    stack.push(v);
                    onStack[v] = true;
                    callStack.push(new int[]{v, 0});
                    depth = Math.max(depth, callStack.size());
                } else if (onStack[v]) {
                    lowlink[u] = Math.min(lowlink[u], index[v]);
                }
            } else {
                callStack.pop();
                if (!callStack.isEmpty()) {
                    int parent = callStack.peek()[0];
                    lowlink[parent] = Math.min(lowlink[parent], lowlink[u]);
                }
                if (lowlink[u] == index[u]) {
                    while (true) {
                        int w = stack.pop();
                        onStack[w] = false;
                        if (w == u) break;
                    }
                    components[0]++;
                }
            }
        }
        m.maxDepth = Math.max(m.maxDepth, depth);
    }
}