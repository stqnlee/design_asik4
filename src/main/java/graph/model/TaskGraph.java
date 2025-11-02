package graph.model;

import java.util.*;

public final class TaskGraph {
    public final boolean directed;
    public final int n;
    public final List<Edge> edges;
    public final Integer source;
    public final String weightModel;

    public TaskGraph(boolean directed, int n, List<Edge> edges, Integer source, String weightModel){
        this.directed = directed; this.n = n; this.edges = edges; this.source = source; this.weightModel = weightModel;
    }

    public List<List<Edge>> adjacencyList(){
        List<List<Edge>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) g.add(new ArrayList<>());
        for (Edge e : edges) g.get(e.u).add(e);
        return g;
    }
}
