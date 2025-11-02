package graph.util;
public class Metrics {
    private long startNs;
    public long durationNs;
    public long dfsVisits;
    public long dfsEdges;
    public long kahnPushes;
    public long kahnPops;
    public long relaxations;
    public void startTimer(){ startNs = System.nanoTime(); }
    public void stopTimer(){ durationNs = System.nanoTime() - startNs; }
    public String toString() {
        return String.format("time(ns)=%d, dfsVisits=%d, dfsEdges=%d, kahnPushes=%d, kahnPops=%d, relaxations=%d",
                durationNs, dfsVisits, dfsEdges, kahnPushes, kahnPops, relaxations);
    }
}
