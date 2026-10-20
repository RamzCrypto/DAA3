package daa.benchmark;

import daa.algorithms.*;
import daa.graph.*;
import daa.metrics.Metrics;
import java.io.*;
import java.util.*;

public class Benchmark {
    public static void main(String[] args) throws Exception {
        int[] sizes = {1000, 3162, 10000, 31623, 100000};
        double[] dens = {4, 20};
        new File("results").mkdirs();
        new File("results/plots").mkdirs();
        PrintWriter csv = new PrintWriter(new FileWriter("results/results.csv"));
        csv.println("algorithm,graph,n,m,time_ms,dsu_ops,edge_scans,comparisons,max_depth,result");

        for (int n : sizes) {
            for (double d : dens) {
                int m = (int) (n * d);
                // Undirected MST
                Graph ug = GraphGenerator.undirected(n, m);
                runMST(csv, "Kruskal", "undirected", ug, n, m);
                runMST(csv, "Prim", "undirected", ug, n, m);

                // Directed SCC
                Graph dg = GraphGenerator.directed(n, m);
                runSCC(csv, "Kosaraju", "directed", dg, n, m);
                runSCC(csv, "Tarjan", "directed", dg, n, m);

                // DAG
                Graph dag = GraphGenerator.dag(n, m);
                runKahn(csv, "Kahn", "dag", dag, n, m);
                runDagSP(csv, "DagShortestPath", "dag", dag, n, m);
            }
        }
        csv.close();
        System.out.println("Benchmark complete. See results/results.csv");
        Plotter.plot();
    }

    static void runMST(PrintWriter csv, String name, String kind, Graph g, int n, int m) {
        List<Long> times = new ArrayList<>();
        long result = 0; Metrics metrics = new Metrics();
        for (int r = 0; r < 5; r++) {
            metrics.reset();
            long t0 = System.nanoTime();
            if (name.equals("Kruskal")) result = Kruskal.run(g, metrics);
            else result = Prim.run(g, metrics);
            long t1 = System.nanoTime();
            times.add((t1 - t0) / 1_000_000);
        }
        Collections.sort(times);
        csv.printf("%s,%s,%d,%d,%d,%d,%d,%d,%d,%d%n",
                name, kind, n, m, times.get(2), metrics.dsuOps, metrics.edgeScans,
                metrics.comparisons, metrics.maxDepth, result);
    }

    static void runSCC(PrintWriter csv, String name, String kind, Graph g, int n, int m) {
        List<Long> times = new ArrayList<>();
        int result = 0; Metrics metrics = new Metrics();
        for (int r = 0; r < 5; r++) {
            metrics.reset();
            long t0 = System.nanoTime();
            if (name.equals("Kosaraju")) result = Kosaraju.run(g, metrics);
            else result = Tarjan.run(g, metrics);
            long t1 = System.nanoTime();
            times.add((t1 - t0) / 1_000_000);
        }
        Collections.sort(times);
        csv.printf("%s,%s,%d,%d,%d,%d,%d,%d,%d,%d%n",
                name, kind, n, m, times.get(2), metrics.dsuOps, metrics.edgeScans,
                metrics.comparisons, metrics.maxDepth, result);
    }

    static void runKahn(PrintWriter csv, String name, String kind, Graph g, int n, int m) {
        List<Long> times = new ArrayList<>();
        int result = 0; Metrics metrics = new Metrics();
        for (int r = 0; r < 5; r++) {
            metrics.reset();
            long t0 = System.nanoTime();
            int[] order = Kahn.run(g, metrics);
            result = order == null ? 0 : order.length;
            long t1 = System.nanoTime();
            times.add((t1 - t0) / 1_000_000);
        }
        Collections.sort(times);
        csv.printf("%s,%s,%d,%d,%d,%d,%d,%d,%d,%d%n",
                name, kind, n, m, times.get(2), metrics.dsuOps, metrics.edgeScans,
                metrics.comparisons, metrics.maxDepth, result);
    }

    static void runDagSP(PrintWriter csv, String name, String kind, Graph g, int n, int m) {
        List<Long> times = new ArrayList<>();
        long result = 0; Metrics metrics = new Metrics();
        for (int r = 0; r < 5; r++) {
            metrics.reset();
            long t0 = System.nanoTime();
            int[] topo = Kahn.run(g, new Metrics());
            result = DagShortestPath.run(g, topo, metrics);
            long t1 = System.nanoTime();
            times.add((t1 - t0) / 1_000_000);
        }
        Collections.sort(times);
        csv.printf("%s,%s,%d,%d,%d,%d,%d,%d,%d,%d%n",
                name, kind, n, m, times.get(2), metrics.dsuOps, metrics.edgeScans,
                metrics.comparisons, metrics.maxDepth, result);
    }
}