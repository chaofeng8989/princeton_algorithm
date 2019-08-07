


import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class SAP {
    private final Digraph G;
    // constructor takes a digraph (not necessarily a DAG)
    private int[] minPath;
    public SAP(Digraph G) {
        this.G = deepCopyG(G);
        minPath = new int[2];
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
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V()) throw new IllegalArgumentException();
        ancestor(v, w);
        return minPath[1];
    }


    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V()) throw new IllegalArgumentException();
        List<Integer> listv = new LinkedList<>(), listw = new LinkedList<>();
        listv.add(v);
        listw.add(w);
        find(listv, listw);
        //biBFS(listv, listw);
        return minPath[0];
    }
    private void find(Iterable<Integer> v, Iterable<Integer> w) {
        int[] disTov = new int[G.V()];
        int[] disTow = new int[G.V()];
        for (int i = 0; i < G.V(); i++) {
            disTov[i] = -1;
            disTow[i] = -1;
        }
        for (int i : v)disTov[i] = 0;
        for (int i : w)disTow[i] = 0;

        bfs(v, disTov);
        bfs(w, disTow);
        int min = Integer.MAX_VALUE;
        int mini = -1;

        // print(disTov);
        // print(disTow);
        for (int i = 0; i < G.V(); i++) {
            if ((disTov[i] != -1 && disTow[i] != -1)) {
                if (min > disTov[i] + disTow[i]) {
                    min = disTov[i] + disTow[i];
                    mini = i;
                }
            }
        }


        if (mini == -1) min = -1;
        minPath[0] = mini;
        minPath[1] = min;
    }
    private void print(int[] xx) {
        for (int x : xx) System.out.print(x + ", ");
        System.out.println();
    }


    private void biBFS(Iterable<Integer> starts, Iterable<Integer> ends) {
        int[] startDP = new int[G.V()], endDP = new int[G.V()];
        for (int i = 0; i < G.V(); i++) {
            startDP[i] = -1;
            endDP[i] = -1;
        }
        Set<Integer> startSet = new HashSet<>(), endSet = new HashSet<>();
        minPath = new int[2];
        for (int i : starts) {
            startSet.add(i);
            startDP[i] = 0;
        }
        for (int i : ends) {
            endSet.add(i);
            endDP[i] = 0;
        }

        int dis = 0;
        Set<Integer> tmp = new HashSet<>();

        while (!startSet.isEmpty() && !endSet.isEmpty()) {
            for (int start : startSet) {
                if (endDP[start] != -1){
                    minPath[0] = start;
                    minPath[1] = dis + endDP[start];
                    return;
                } else {
                    startDP[start] = dis;
                    for (int adj : G.adj(start)) {
                        tmp.add(adj);
                    }
                }
            }
            startSet.clear();
            for (int i : tmp) {
                if (startDP[i] == -1) startSet.add(i);
            }
            tmp.clear();

            for (int end : endSet) {
                if (startDP[end] != -1){
                    minPath[0] = end;
                    minPath[1] = dis + startDP[end];
                    return;
                } else {
                    endDP[end] = dis;
                    for (int adj : G.adj(end)) {
                        tmp.add(adj);
                    }
                }
            }
            endSet.clear();;
            for (int i : tmp) {
                if (endDP[i] == -1) endSet.add(i);
            }
            tmp.clear();
            dis++;
//            print(startDP);
//            print(endDP);
//            System.out.println();
        }


        minPath = new int[]{-1, -1};



    }


    private void bfs(Iterable<Integer> starts, int[] disToTarget) {
        Queue<Integer> q = new Queue<>();
        for (int p : starts) q.enqueue(p);
        int dis = 0;
        while (!q.isEmpty()) {
            int size = q.size();
            Set<Integer> set = new HashSet<>();
            for (int i = 0; i < size; i++) {
                int node = q.dequeue();
                disToTarget[node] = dis;
                for (int adj : G.adj(node)) set.add(adj);
            }
            for (int node : set) {
                if (disToTarget[node] == -1) q.enqueue(node);
            }

            dis++;
        }
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        for (Integer i : v) if (i == null || i < 0 || i >= G.V()) throw new IllegalArgumentException();
        for (Integer i : w) if (i == null || i < 0 || i >= G.V()) throw new IllegalArgumentException();

        ancestor(v, w);
        return minPath[1];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        for (Integer i : v) if (i == null || i < 0 || i >= G.V()) throw new IllegalArgumentException();
        for (Integer i : w) if (i == null || i < 0 || i >= G.V()) throw new IllegalArgumentException();
        find(v, w);
        //biBFS(v, w);
        return minPath[0];
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
