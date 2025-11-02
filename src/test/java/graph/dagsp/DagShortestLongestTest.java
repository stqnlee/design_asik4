package graph.dagsp;
import graph.model.Edge;
import graph.util.Metrics;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class DagShortestLongestTest {
    @Test
    public void testShortestAndLongest() {
        int n = 4;
        List<List<Edge>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        adj.get(0).add(new Edge(0,1,3));
        adj.get(0).add(new Edge(0,2,5));
        adj.get(1).add(new Edge(1,3,4));
        adj.get(2).add(new Edge(2,3,1));
        Metrics m = new Metrics();
        DagShortestLongest ds = new DagShortestLongest(adj, m);
        List<Integer> topo = Arrays.asList(0,1,2,3);
        long[] dist = ds.shortestFrom(0, topo);
        assertEquals(0, dist[0]);
        assertEquals(3, dist[1]);
        assertEquals(5, dist[2]);
        assertEquals(6, dist[3]);

        DagShortestLongest.Result res = ds.longestPath();
        assertNotNull(res.path);
    }
}
