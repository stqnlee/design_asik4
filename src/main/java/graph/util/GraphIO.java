package graph.util;

import com.fasterxml.jackson.databind.*;
import graph.model.Edge;
import graph.model.TaskGraph;

import java.io.*;
import java.util.*;

public class GraphIO {
    private static final ObjectMapper M = new ObjectMapper();

    public static TaskGraph readFromFile(File f) throws IOException {
        JsonNode root = M.readTree(f);
        boolean directed = root.path("directed").asBoolean(true);
        int n = root.path("n").asInt();
        List<Edge> edges = new ArrayList<>();
        for (JsonNode e : root.withArray("edges")) {
            int u = e.path("u").asInt();
            int v = e.path("v").asInt();
            long w = e.path("w").asLong(1);
            edges.add(new Edge(u, v, w));
        }
        Integer source = root.has("source") && !root.get("source").isNull() ? root.get("source").asInt() : null;
        String weightModel = root.has("weight_model") ? root.get("weight_model").asText("edge") : "edge";
        return new TaskGraph(directed, n, edges, source, weightModel);
    }
}
