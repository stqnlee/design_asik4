package graph.topo;
import graph.model.Edge;
import graph.util.Metrics;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class KahnTopoSortTest {
    @Test
    public void testTopo() {
        int n = 3;
        List<List<Edge>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        adj.get(0).add(new Edge(0,1,1));
        adj.get(1).add(new Edge(1,2,1));
        Metrics m = new Metrics();
        KahnTopoSort k = new KahnTopoSort(adj, n, m);
        List<Integer> topo = k.topo();
        assertEquals(3, topo.size());
        assertTrue(topo.indexOf(0) < topo.indexOf(1));
        assertTrue(topo.indexOf(1) < topo.indexOf(2));
    }
}
