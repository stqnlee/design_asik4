package graph.scc;

import graph.model.Edge;
import graph.model.TaskGraph;

import java.util.*;

public class Condensation {
    public final int compCount;
    public final List<List<Edge>> compAdj;
    public final List<Integer> compSize;

    public Condensation(TaskGraph g, List<List<Integer>> comps) {
        this.compCount = comps.size();
        this.compAdj = new ArrayList<>();
        this.compSize = new ArrayList<>();
        for (int i = 0; i < compCount; i++) compAdj.add(new ArrayList<>());
        int[] compId = TarjanSCC.mapNodeToComp(comps, g.n);

        Set<Long> seen = new HashSet<>();
        for (Edge e : g.edges) {
            int a = compId[e.u], b = compId[e.v];
            if (a != b) {
                long key = ((long)a<<32) | (b & 0xffffffffL);
                if (!seen.contains(key)) {
                    compAdj.get(a).add(new Edge(a, b, e.w));
                    seen.add(key);
                }
            }
        }
        for (List<Integer> comp : comps) compSize.add(comp.size());
    }
}
