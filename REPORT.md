# DAA Assignment 3 — Report

**Author:** Bazaraly Ramazan
**Group:** SE-2526

## 1. Complexity table

| Algorithm / operation | Time | Extra space | Justification |
|---|---|---|---|
| DSU `find` | O(α(n)) amortised | O(1) | Path compression + union by rank/size gives inverse-Ackermann amortised cost |
| DSU `union` | O(α(n)) amortised | O(1) | Two finds + constant pointer update |
| DSU `components()` | O(1) | O(1) | Maintained counter, decremented on successful union |
| Kruskal | O(E log E) | O(V) | Sort dominates; DSU is near-linear |
| Prim | O((V + E) log V) | O(V + E) | Binary heap, each edge pushed once |
| Kosaraju | O(V + E) | O(V + E) | Two iterative DFS passes + reversed graph |
| Tarjan | O(V + E) | O(V) | Single iterative DFS with index/lowlink |
| Kahn | O(V + E) | O(V) | One pass, each edge relaxed once |
| DAG shortest paths | O(V + E) | O(V) | One relaxation pass in topological order |
| Condensation + longest path | O(V + E) | O(V + E) | SCC + build condensation DAG + one pass |

## 2. Counters vs formulas

Counters were taken directly from `results/results.csv` (inside the algorithms,
not computed from formulas). For each algorithm the ratio counter / formula is
essentially constant as `n` grows:

| Algorithm | Counter | Formula | Ratio at n=1000 | Ratio at n=100000 |
|---|---|---|---|---|
| Kosaraju | edgeScans | 2m | 2.00 | 2.00 |
| Tarjan | edgeScans | m | 1.00 | 1.00 |
| Kahn | edgeScans | m | 1.00 | 1.00 |
| DAG shortest paths | edgeScans | m | 1.00 | 1.00 |
| Prim | edgeScans | m | 1.00 | 1.00 |
| Kruskal | dsuOps | E | 2.29 | 2.75 |
| Kruskal | comparisons | E log E | 3.90 | 3.73 |

Flat ratios are the empirical confirmation of the complexity class. Kruskal's
`dsuOps / E` stays between 2 and 3 (two finds per edge + occasional unions),
and `comparisons / (E log E)` is roughly constant (log factor from sorting).

## 3. Correctness arguments

### 3.1 Cut property and MST

For any cut (S, V \ S) in a weighted connected graph, the lightest edge crossing
the cut belongs to some minimum spanning tree. **Kruskal** uses this directly:
edges are processed in increasing weight, and each edge (u, v) accepted iff
`find(u) != find(v)` — that is, the edge crosses the cut between the two current
components. Since it is the lightest edge still connecting two components, the
cut property guarantees it is safe. **Prim** grows a single tree from vertex 0
and, at each step, picks the lightest edge leaving the tree with a min-heap.
That edge again crosses the cut (tree, rest), so the cut property makes it safe.
Both algorithms therefore produce a minimum spanning forest; on a connected
graph they agree on the total weight (verified by `MSTTest` on 100 random
graphs and by the benchmark, which fails loudly if they differ).

### 3.2 Induction on the topological order

Claim: after processing vertices in topological order and relaxing their
outgoing edges, `dist[v]` equals the shortest distance from the virtual
super-source to `v`. **Base:** every in-degree-0 vertex starts at `dist = 0`,
which is the correct shortest distance (it is a source). **Step:** suppose all
predecessors of `v` in topological order already have correct `dist`. When the
last such predecessor `u` is processed, edge `(u, v)` is relaxed and
`dist[v] <= dist[u] + w(u, v)`. Any path to `v` enters via some predecessor, so
no shorter path exists. **Conclusion:** one pass suffices, giving O(V + E).
Negative edge weights are fine because every edge is relaxed exactly once in an
order that respects all dependencies — Bellman-Ford's repeated passes are needed
only to handle cycles, which do not exist in a DAG.

## 4. Counter vs reference values

Reference values from the assignment are reproduced **exactly** for
`n = 1000, m = 4000`:

| Algorithm | Reference | Measured |
|---|---|---|
| Kruskal / Prim MST | 147970 | 147970 |
| Kosaraju / Tarjan SCC | 27 | 27 |
| Kahn order length | 1000 | 1000 |
| DAG shortest path sum | 396274 | 396274 |

For larger sizes there is a 0.1–1 % deviation in MST / SCC / DagSP totals. The
deviation is not caused by the algorithms (they pass all cross-checks —
Kruskal == Prim, Kosaraju == Tarjan, DagSP == Bellman-Ford, Kahn valid, maxDepth
bounded). The cause is that the reference generator's exact consumption order
of `new Random(42)` could not be reconstructed from the textual description.
Different treatments of the `a == b` case (self-loop filter vs. keep vs. skip
without `i--`) shift the random stream and change all subsequent weights.
Since the algorithms are provably correct against brute-force references, the
project is submitted with the current generator and the deviation is documented
here.

## 5. Which algorithm when

| Situation | Algorithm | Why |
|---|---|---|
| Sparse undirected, MST | Kruskal | Edge list already exists; O(E log E) with E ≈ 4V is cheap |
| Dense undirected, MST | Prim | O((V + E) log V) with binary heap; avoids sorting E ≈ 20V |
| Disconnected graph, MST | Kruskal | Naturally returns a forest, just stop after E sorted |
| Directed, SCC needed | Kosaraju or Tarjan | Both O(V + E); Tarjan scans edges once, Kosaraju twice |
| Cycle detection in jobs | Kahn | If fewer than V vertices leave the queue, a cycle exists |
| Topological order | Kahn | Simple, iterative, in-degree based |
| Shortest path in a DAG, incl. negative weights | DAG relaxation | O(V + E), no Bellman-Ford overhead |
| Shortest path in a general graph | Dijkstra (non-neg) / Bellman-Ford (neg) | Not implemented here, but that is the standard choice |
| Longest path (critical path) | Condensation + DAG relaxation | Longest path is NP-hard in general graphs, easy in DAGs |

## 6. Slope check (log–log)

Fitting the `edgeScans` curves on log–log axes:
- Kosaraju, Tarjan, Kahn, DagSP, Prim: slope ≈ 1.00 (linear in edges).
- Kruskal `dsuOps`: slope ≈ 1.05, slightly steeper because of the log factor
  from sorting.

`scans_per_edge.png` shows flat lines at 1.0 (Prim, Kahn, DagSP), 2.0
(Kosaraju), confirming linear work per edge. `kruskal_share.png` shows Kruskal
scans almost all edges on sparse graphs (ratio near 1.0) and only a small
fraction on dense graphs (ratio 0.15–0.30), which explains why Prim wins on
dense inputs.

## 7. Time observations

Wall-clock times in `results.csv` were taken as the median of 5 runs. They are
noisy on the two smallest sizes (sub-millisecond) and grow smoothly on larger
ones. Time is reported for completeness only; the graded evidence is the
operation counters, which are identical on every machine because the generator
uses a fixed seed.

## 8. Plots

- `results/plots/ops_vs_n.png` — operations vs n, log–log, one line per algorithm and density.
- `results/plots/scans_per_edge.png` — edgeScans / m vs n; flat lines confirm linear work.
- `results/plots/kruskal_share.png` — Kruskal's share of scanned edges, sparse vs dense.