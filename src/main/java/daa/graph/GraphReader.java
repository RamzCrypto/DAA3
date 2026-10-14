package daa.graph;

import java.io.*;
import java.util.*;

public class GraphReader {
    public static Graph read(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String[] first = br.readLine().trim().split("\\s+");
            int n = Integer.parseInt(first[0]);
            int m = Integer.parseInt(first[1]);
            boolean directed = first[2].equalsIgnoreCase("directed");
            Graph g = new Graph(n, directed);
            for (int i = 0; i < m; i++) {
                String line = br.readLine();
                if (line == null) break;
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\s+");
                g.addEdge(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            }
            return g;
        }
    }
}