package daa.benchmark;

import org.jfree.chart.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.io.*;
import java.util.*;

public class Plotter {
    public static void plot() throws Exception {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("results/results.csv"))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) rows.add(line.split(","));
        }

        XYSeriesCollection opsDataset = new XYSeriesCollection();
        XYSeriesCollection scanPerEdge = new XYSeriesCollection();
        XYSeriesCollection kruskalShare = new XYSeriesCollection();

        Map<String, XYSeries> seriesMap = new LinkedHashMap<>();
        Map<String, XYSeries> scanMap = new LinkedHashMap<>();
        XYSeries kSparse = new XYSeries("Kruskal sparse");
        XYSeries kDense = new XYSeries("Kruskal dense");

        for (String[] r : rows) {
            String alg = r[0], kind = r[1];
            int n = Integer.parseInt(r[2]);
            int m = Integer.parseInt(r[3]);
            long scans = Long.parseLong(r[6]);
            String key = alg + " " + kind;
            seriesMap.computeIfAbsent(key, k -> new XYSeries(k)).add(n, scans);
            scanMap.computeIfAbsent(key, k -> new XYSeries(k)).add(n, (double) scans / m);
            if (alg.equals("Kruskal")) {
                if (m / n == 4) kSparse.add(n, (double) scans / m);
                else kDense.add(n, (double) scans / m);
            }
        }
        for (XYSeries s : seriesMap.values()) opsDataset.addSeries(s);
        for (XYSeries s : scanMap.values()) scanPerEdge.addSeries(s);
        kruskalShare.addSeries(kSparse);
        kruskalShare.addSeries(kDense);

        saveChart(opsDataset, "Operations vs n", "n (log)", "operations (log)", true, "results/plots/ops_vs_n.png");
        saveChart(scanPerEdge, "Scans per edge vs n", "n (log)", "edgeScans / m", true, "results/plots/scans_per_edge.png");
        saveChart(kruskalShare, "Kruskal share of edges", "n (log)", "edgeScans / m", true, "results/plots/kruskal_share.png");
    }

    private static void saveChart(XYSeriesCollection ds, String title, String x, String y,
                                  boolean log, String path) throws IOException {
        JFreeChart chart = ChartFactory.createXYLineChart(title, x, y, ds);
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);
        plot.setRenderer(renderer);
        if (log) {
            plot.setDomainAxis(new org.jfree.chart.axis.LogarithmicAxis(x));
            plot.setRangeAxis(new org.jfree.chart.axis.LogarithmicAxis(y));
        }
        ChartUtils.saveChartAsPNG(new File(path), chart, 900, 600);
    }
}