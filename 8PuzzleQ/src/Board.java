import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.List;

public class Board {
    private final int[][] board;
    private final int d;
    private int hamming, manhattan;
    private int zeroX, zeroY;
    public Board(int[][] blocks) {

        d = blocks.length;
        hamming = 0;
        manhattan = 0;
        board = new int[d] [d];
        for (int i = 0; i < d; i++) {
            board[i] = new int[d];
            for (int j = 0; j < d; j++) {
                int target = i * d + j + 1;
                int value = blocks[i][j];
                if (value == 0) {
                    zeroX = i;
                    zeroY = j;
                }
                board[i][j] = value;
                if (value != target) hamming += 1;
                int targetX = (value - 1) / d, targetY = (value - 1) % d;
                manhattan += Math.abs(i - targetX) + Math.abs(j - targetY);
            }
        }

    }          // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public int dimension() {
        return d;
    }                 // board dimension n
    public int hamming() {
        return hamming;
    }                  // number of blocks out of place
    public int manhattan() {
        return manhattan;
    }             // sum of Manhattan distances between blocks and goal
    public boolean isGoal() {
        return hamming == 0;

    }             // is this board the goal board?
    public Board twin() {
        int x1 = -1, x2 = -1, y1 = -1, y2 = -1;
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < d; j++) {
                if (board[i][j] != 0) {
                    if (x1 == -1) {
                        x1 = i;
                        y1 = j;
                    } else{
                        x2 = i;
                        y2 = j;
                        return getExchangedBoard(x1, y1, x2, y2);
                    }
                }
            }
        }
        return null;
    }                   // a board that is obtained by exchanging any pair of blocks
    public boolean equals(Object y) {
        if (y == null) return false;
        Board that = (Board) y;
        if (this.d != that.d) return false;
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < d; j++) {
                if (this.board[i][j] != that.board[i][j]) return false;
            }
        }
        return true;
    }       // does this board equal y?
    public Iterable<Board> neighbors() {
        List<Board> nbs = new LinkedList<>();
        Board up = getExchangedBoard(zeroX -1, zeroY, zeroX, zeroY);
        Board down = getExchangedBoard(zeroX +1, zeroY, zeroX, zeroY);
        Board left = getExchangedBoard(zeroX, zeroY -1, zeroX, zeroY);
        Board right = getExchangedBoard(zeroX, zeroY + 1, zeroX, zeroY);
        if (up != null) nbs.add(up);
        if (down != null) nbs.add(down);
        if (left != null) nbs.add(left);
        if (right != null) nbs.add(right);
        return nbs;
    }     // all neighboring boards
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(d);
        sb.append("\n");
        for (int i = 0; i < d; i++) {
            sb.append(" ");
            for (int j = 0; j < d; j++) {
                sb.append(board[i][j]);
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }              // string representation of this board (in the output format specified below)

    private Board getExchangedBoard(int x1, int y1, int x2, int y2) {
        if (x1 >= d || x1 < 0 || y1 >= d || y1 < 0) return null;
        int tmp = board[x2][y2];
        board[x2][y2] = board[x1][y1];
        board[x1][y1] = tmp;
        Board newBoard = new Board(board);
        board[x1][y1] = board[x2][y2];
        board[x2][y2] = tmp;
        return newBoard;
    }
    public static void main(String[] args) {

    }// unit tests (not graded)
}