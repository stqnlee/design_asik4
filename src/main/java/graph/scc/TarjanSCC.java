package graph.scc;

import graph.model.Edge;
import graph.model.TaskGraph;
import graph.util.Metrics;
import java.util.*;

public class TarjanSCC {
    private final TaskGraph g;
    private final List<List<Edge>> adj;
    private final int n;
    private final Metrics metrics;

    public TarjanSCC(TaskGraph g, Metrics metrics){
        this.g = g;
        this.n = g.n;
        this.adj = g.adjacencyList();
        this.metrics = metrics;
    }

    public List<List<Integer>> run(){
        metrics.startTimer();
        int[] disc = new int[n];
        int[] low = new int[n];
        boolean[] onStack = new boolean[n];
        Arrays.fill(disc, -1);
        Stack<Integer> stack = new Stack<>();
        List<List<Integer>> comps = new ArrayList<>();
        int[] time = {0};

        for (int i = 0; i < n; i++) {
            if (disc[i] == -1) dfs(i, disc, low, onStack, stack, comps, time);
        }

        metrics.stopTimer();
        return comps;
    }

    private void dfs(int u, int[] disc, int[] low, boolean[] onStack, Stack<Integer> stack, List<List<Integer>> comps, int[] time){
        disc[u] = low[u] = time[0]++;
        metrics.dfsVisits++;
        stack.push(u);
        onStack[u] = true;

        for (Edge e : adj.get(u)) {
            metrics.dfsEdges++;
            int v = e.v;
            if (disc[v] == -1) {
                dfs(v, disc, low, onStack, stack, comps, time);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        if (low[u] == disc[u]) {
            List<Integer> comp = new ArrayList<>();
            while (true) {
                int w = stack.pop();
                onStack[w] = false;
                comp.add(w);
                if (w == u) break;
            }
            comps.add(comp);
        }
    }

    public static int[] mapNodeToComp(List<List<Integer>> comps, int n) {
        int[] compId = new int[n];
        for (int i = 0; i < comps.size(); i++) {
            for (int v : comps.get(i)) compId[v] = i;
        }
        return compId;
    }
}
