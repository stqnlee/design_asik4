package graph.topo;
import graph.model.Edge;
import graph.util.Metrics;
import java.util.*;
public class KahnTopoSort {
    private final List<List<Edge>> adj;
    private final int n;
    private final Metrics metrics;
    public KahnTopoSort(List<List<Edge>> adj, int n, Metrics metrics){
        this.adj = adj; this.n = n; this.metrics = metrics;
    }
    public List<Integer> topo() {
        metrics.startTimer();
        int[] indeg = new int[n];
        for (int u = 0; u < n; u++) {
            for (Edge e : adj.get(u)) indeg[e.v]++;
        }
        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) { if (indeg[i] == 0) { q.add(i); metrics.kahnPushes++; } }
        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll(); metrics.kahnPops++;
            order.add(u);
            for (Edge e : adj.get(u)) {
                indeg[e.v]--;
                if (indeg[e.v] == 0) { q.add(e.v); metrics.kahnPushes++; }
            }
        }
        metrics.stopTimer();
        return order;
    }
}
