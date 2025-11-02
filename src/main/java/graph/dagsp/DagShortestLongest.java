package graph.dagsp;

import graph.model.Edge;
import graph.util.Metrics;

import java.util.*;

public class DagShortestLongest {
    private final List<List<Edge>> adj;
    private final Metrics metrics;

    public DagShortestLongest(List<List<Edge>> adj, Metrics metrics) {
        this.adj = adj;
        this.metrics = metrics;
    }

    public long[] shortestFrom(int source, List<Integer> topo) {
        int n = adj.size();
        final long INF = Long.MAX_VALUE / 4;
        long[] dist = new long[n];
        Arrays.fill(dist, INF);
        dist[source] = 0;
        Set<Integer> inTopo = new HashSet<>(topo);
        boolean started = false;
        for (int u : topo) {
            if (!started && u == source) started = true;
            if (!started && dist[u] == INF) continue;
            if (dist[u] == INF) continue;
            for (Edge e : adj.get(u)) {
                metrics.relaxations++;
                if (dist[e.v] > dist[u] + e.w) dist[e.v] = dist[u] + e.w;
            }
        }
        return dist;
    }

    public Result longestPath() {
        int n = adj.size();
        List<Integer> topo = computeTopo();
        final long NEG_INF = Long.MIN_VALUE / 4;
        long[] dp = new long[n];
        int[] parent = new int[n];
        Arrays.fill(dp, NEG_INF);
        Arrays.fill(parent, -1);
        for (int u : topo) {
            if (dp[u] == NEG_INF) dp[u] = 0;
            for (Edge e : adj.get(u)) {
                metrics.relaxations++;
                if (dp[e.v] < dp[u] + e.w) {
                    dp[e.v] = dp[u] + e.w;
                    parent[e.v] = u;
                }
            }
        }
        long best = NEG_INF; int bestV = -1;
        for (int i = 0; i < n; i++) if (dp[i] > best) { best = dp[i]; bestV = i; }
        List<Integer> path = new ArrayList<>();
        if (bestV != -1) {
            int cur = bestV;
            while (cur != -1) { path.add(cur); cur = parent[cur]; }
            Collections.reverse(path);
        }
        return new Result(best, path);
    }

    private List<Integer> computeTopo() {
        int n = adj.size();
        int[] indeg = new int[n];
        for (int u = 0; u < n; u++) for (Edge e : adj.get(u)) indeg[e.v]++;
        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) q.add(i);
        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            for (Edge e : adj.get(u)) {
                indeg[e.v]--;
                if (indeg[e.v] == 0) q.add(e.v);
            }
        }
        return order;
    }

    public static class Result {
        public final long length;
        public final List<Integer> path;
        public Result(long length, List<Integer> path){ this.length = length; this.path = path; }
    }
}
