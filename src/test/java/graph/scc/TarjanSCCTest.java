package graph.scc;

import graph.model.Edge;
import graph.model.TaskGraph;
import graph.util.Metrics;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class TarjanSCCTest {
    @Test
    public void testSimpleCycle() {
        List<Edge> edges = Arrays.asList(new Edge(0,1,1), new Edge(1,2,1), new Edge(2,0,1));
        TaskGraph tg = new TaskGraph(true, 3, edges, null, "edge");
        Metrics m = new Metrics();
        TarjanSCC scc = new TarjanSCC(tg, m);
        List<List<Integer>> comps = scc.run();
        assertEquals(1, comps.size());
        assertEquals(3, comps.get(0).size());
    }

    @Test
    public void testDAG() {
        List<Edge> edges = Arrays.asList(new Edge(0,1,1), new Edge(1,2,1));
        TaskGraph tg = new TaskGraph(true, 3, edges, null, "edge");
        Metrics m = new Metrics();
        TarjanSCC scc = new TarjanSCC(tg, m);
        List<List<Integer>> comps = scc.run();
        assertEquals(3, comps.size());
    }
}
