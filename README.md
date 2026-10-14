# DAA Assignment 3 — City Network & Job Scheduler

**Author:** Bazaraly Ramazan
**Group:** SE-2526
**Tag:** v1.0

## Overview

Graph library and algorithms implemented from scratch for:
- Disjoint Set Union (union by rank/size + path compression)
- Minimum Spanning Tree (Kruskal, Prim)
- Strongly Connected Components (Kosaraju, Tarjan)
- Topological Order (Kahn)
- Shortest paths in a DAG
- Condensation graph and longest path (bonus)

All six algorithms work on a single `Graph` class (adjacency list, directed or
undirected, weighted). Counters (`dsuOps`, `edgeScans`, `comparisons`, `maxDepth`)
are incremented inside the algorithms.

## Build

```bash
mvn clean compile
Requires JDK 17+ and Maven 3.9+.

Run tests
bash
mvn test
11 JUnit 5 tests across 6 test classes: DSU against reference, Kruskal == Prim,
Kosaraju/Tarjan against brute force, Kahn validation + cycle detection,
DAG shortest paths against Bellman-Ford (with negative weights), stack safety
at 100 000 vertices.

Run benchmark
bash
mvn compile exec:java
Or run daa.benchmark.Benchmark from the IDE. Writes:

results/results.csv — one row per algorithm and graph

results/plots/ops_vs_n.png

results/plots/scans_per_edge.png

results/plots/kruskal_share.png

The benchmark uses new Random(42) in GraphGenerator, so results are
reproducible on any machine.

Project layout
text
src/main/java/daa/
├── graph/        Graph, Edge, GraphReader
├── dsu/          DSU
├── algorithms/   Kruskal, Prim, Kosaraju, Tarjan, Kahn, DagShortestPath, Condensation
├── metrics/      Metrics
└── benchmark/    GraphGenerator, Benchmark, Plotter

src/test/java/daa/  6 JUnit 5 test classes
data/               3 small graphs (disconnected, cyclic, undirected)
results/            CSV and plots produced by the benchmark
Reference values
For n=1000, m=4000 all reference values from the assignment are reproduced
exactly: MST=147970, SCC=27, Kahn=1000, DagSP=396274. For larger sizes the
generator's exact Random(42) consumption could not be reconstructed from the
textual description (see REPORT.md §4).

