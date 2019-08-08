


import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


import java.util.HashSet;
import java.util.Set;


public class SAP {
    private final Digraph G;
    // constructor takes a digraph (not necessarily a DAG)
    private final int[] store;
    private Set<Integer> prevv, prevw;
    public SAP(Digraph G) {
        this.G = deepCopyG(G);
        store = new int[2];
    }
    private Digraph deepCopyG(Digraph initialG) {
        Digraph cp = new Digraph(initialG.V());
        for (int i = 0; i < initialG.V(); i++) {
            for (int adj : initialG.adj(i)) cp.addEdge(i, adj);
        }
        return cp;
    }
    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        Set<Integer> setv = new HashSet<>(), setw = new HashSet<>();
        setv.add(v);
        setw.add(w);
        return lengthForSet(setv, setw);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        Set<Integer> setv = new HashSet<>(), setw = new HashSet<>();
        for (Integer i : v) setv.add(i);
        for (Integer i : w) setw.add(i);
        return lengthForSet(setv, setw);

    }

    private int lengthForSet(Set<Integer> setv, Set<Integer> setw) {
        for (Integer i : setv) {
            if (i == null || i < 0 || i >= G.V()) throw new IllegalArgumentException();
        }
        for (Integer i : setw) {
            if (i == null || i < 0 || i >= G.V()) throw new IllegalArgumentException();
        }
        //if (prevv != null && prevw != null &&isSame(prevv, setv) && isSame(prevw, setw)) return store[0];
        biBFS(setv, setw);
        prevv = setv;
        prevw = setw;
        return store[0];
    }
    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        Set<Integer> setv = new HashSet<>(), setw = new HashSet<>();
        setv.add(v);
        setw.add(w);
        return ancestorForSet(setv, setw);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        Set<Integer> setv = new HashSet<>(), setw = new HashSet<>();
        for (Integer i : v) setv.add(i);
        for (Integer i : w) setw.add(i);
        return ancestorForSet(setv, setw);
    }

    private int ancestorForSet(Set<Integer> setv, Set<Integer> setw) {
        for (Integer i : setv) {
            if (i == null || i < 0 || i >= G.V()) throw new IllegalArgumentException();
        }
        for (Integer i : setw) {
            if (i == null || i < 0 || i >= G.V()) throw new IllegalArgumentException();
        }
        //if (prevv != null && prevw != null &&isSame(prevv, setv) && isSame(prevw, setw)) return store[1];
        biBFS(setv, setw);
        prevv = setv;
        prevw = setw;
        return store[1];
    }

    private boolean isSame(Set<Integer> prev, Set<Integer> now) {
        if (prev.size() != now.size()) return false;
        for (int i : prev) {
            if (!now.contains(i)) return false;
        }
        return true;
    }

    private void biBFS(Set<Integer> starts, Set<Integer> ends) {
        int dis = 0;
        int mini = -1;
        int min = Integer.MAX_VALUE;
        int[] disToStart = new int[G.V()], disToEnd = new int[G.V()];
        for (int i = 0; i < G.V(); i++) {
            disToEnd[i] = -1;
            disToStart[i] = -1;
        }
        Set<Integer> tmp = new HashSet<>();
        while (!starts.isEmpty() || !ends.isEmpty()) {
            if (dis >= min) break;
            for (int i : starts) {
                disToStart[i] = dis;
                if (disToEnd[i] != -1) {
                    if (min > dis + disToEnd[i]) {
                        mini = i;
                        min = dis + disToEnd[i];
                    }
                }
                for (int adj : G.adj(i)) tmp.add(adj);
            }
            starts.clear();
            for (int i : tmp) {
                if (disToStart[i] == -1) starts.add(i);
            }
            tmp.clear();

            for (int i : ends) {
                disToEnd[i] = dis;
                if (disToStart[i] != -1) {
                    if (min > dis + disToStart[i]) {
                        mini = i;
                        min = dis + disToStart[i];
                    }
                }
                for (int adj : G.adj(i)) tmp.add(adj);
            }
            ends.clear();
            for (int i : tmp) {
                if (disToEnd[i] == -1) ends.add(i);
            }
            tmp.clear();
            dis++;
        }
        if (mini == -1) {store[0] = -1; store[1] = -1;}
        else {store[0] = min; store[1] = mini;}
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }


}
