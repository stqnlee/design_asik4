package graph.app;

import graph.model.TaskGraph;
import graph.scc.TarjanSCC;
import graph.scc.Condensation;
import graph.topo.KahnTopoSort;
import graph.dagsp.DagShortestLongest;
import graph.util.GraphIO;
import graph.util.Metrics;

import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Usage: java -jar ... <path-to-json>");
            System.exit(1);
        }
        File f = new File(args[0]);
        TaskGraph tg = GraphIO.readFromFile(f);

        System.out.println("Loaded graph: n=" + tg.n + ", edges=" + tg.edges.size() + ", source=" + tg.source);

        Metrics sccMetrics = new Metrics();
        TarjanSCC tarjan = new TarjanSCC(tg, sccMetrics);
        List<List<Integer>> comps = tarjan.run();
        System.out.println("\nSCC count: " + comps.size());
        for (int i = 0; i < comps.size(); i++) {
            System.out.println(" comp " + i + " size=" + comps.get(i).size() + " nodes=" + comps.get(i));
        }
        System.out.println("SCC metrics: " + sccMetrics);

        Condensation cond = new Condensation(tg, comps);
        System.out.println("\nCondensation: compCount=" + cond.compCount);
        int totalEdges = 0;
        for (int i = 0; i < cond.compAdj.size(); i++) {
            totalEdges += cond.compAdj.get(i).size();
        }
        System.out.println("Condensation edges (unique): " + totalEdges);

        Metrics topoMetrics = new Metrics();
        KahnTopoSort kahn = new KahnTopoSort(cond.compAdj, cond.compCount, topoMetrics);
        List<Integer> compTopo = kahn.topo();
        System.out.println("\nTopological order of components: " + compTopo);
        System.out.println("Topo metrics: " + topoMetrics);

        int[] compId = TarjanSCC.mapNodeToComp(comps, tg.n);
        List<Integer> nodeOrder = new ArrayList<>();
        for (int comp : compTopo) {
            for (int v : comps.get(comp)) nodeOrder.add(v);
        }
        System.out.println("\nDerived order of original tasks after SCC compression: " + nodeOrder);

        Metrics dagMetrics = new Metrics();
        DagShortestLongest dsp = new DagShortestLongest(cond.compAdj, dagMetrics);

        if (tg.source != null) {
            int srcComp = compId[tg.source];
            long[] dist = dsp.shortestFrom(srcComp, compTopo);
            System.out.println("\nShortest distances from source node's component (" + srcComp + "):");
            for (int i = 0; i < dist.length; i++) {
                System.out.println(" comp " + i + " : " + (dist[i] >= Long.MAX_VALUE/4 ? "INF" : dist[i]));
            }
        } else {
            System.out.println("\nNo source provided in JSON, skipping single-source shortest path.");
        }

        DagShortestLongest.Result longest = dsp.longestPath();
        System.out.println("\nLongest path in condensation (critical path): length=" + longest.length + " path=" + longest.path);
        System.out.println("DAG metrics: " + dagMetrics);
    }
}
