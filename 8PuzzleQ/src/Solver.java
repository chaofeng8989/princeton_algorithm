import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

public class Solver {
    private class Node implements Comparable<Node> {
        Board board;
        Node pre;
        int move;

        public Node(Board board, Node pre, int move) {
            this.board = board;
            this.pre = pre;
            this.move = move;
        }

        public Iterable<Node> getNeightbors() {
            List<Node> re = new LinkedList<>();
            Iterator<Board> nbs = this.board.neighbors().iterator();
            while (nbs.hasNext()) {
                Board bb = nbs.next();
                if (pre == null || !bb.equals(pre.board)) re.add(new Node(bb, this, move + 1));
            }
            return re;
        }

        @Override
        public int compareTo(Node o) {
            return board.hamming() + move - o.board.hamming() - move;
        }

        public boolean equals(Node o) {return board.equals(o.board);}


        @Override
        public String toString() {
            return "Node{" +
                    "board=" + board +
                    ", pre=" + pre +
                    ", move=" + move +
                    '}';
        }
    }



    private Node end;
    private boolean solvable = false;
    public Solver(Board initial) {

        if (initial == null) throw new IllegalArgumentException();
        MinPQ<Node> pq1 = new MinPQ<>(), pq2 = new MinPQ<>();

        Node initialNode = new Node(initial, null, 0);
        Board twin = initial.twin();
        Node twinNode = new Node(twin, null, 0);

        pq1.insert(initialNode);
        pq2.insert(twinNode);

        while (!pq1.isEmpty() && !pq2.isEmpty()) {
            Node tmp1 = pq1.delMin(), tmp2 = pq2.delMin();
            if (tmp1.board.isGoal()) {
                solvable = true;
                end = tmp1;
                break;
            }
            if (tmp2.board.isGoal()) {
                break;
            }

            Iterator<Node> nb1 = tmp1.getNeightbors().iterator();
            Iterator<Node> nb2 = tmp2.getNeightbors().iterator();

            while (nb1.hasNext()) pq1.insert(nb1.next());
            while (nb2.hasNext()) pq2.insert(nb2.next());


        }


    }          // find a solution to the initial board (using the A* algorithm)

    public boolean isSolvable() {
        return solvable;
    }           // is the initial board solvable?
    public int moves() {
        if (!solvable) return -1;
        return end.move;                       // min number of moves to solve initial board; -1 if unsolvable
    }
    public Iterable<Board> solution() {
        List<Board> re = new LinkedList<>();
        Node tmp = end;
        while (tmp.pre != null) {
            re.add(0, tmp.board);
            tmp = tmp.pre;
        }
        re.add(0, tmp.board);
        return re;



    }// sequence of boards in a shortest solution; null if unsolvable


    public static void main(String[] args) {
        // create initial board from file
        In in = new In("puzzle04.txt");
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }// solve a slider puzzle (given below)
}