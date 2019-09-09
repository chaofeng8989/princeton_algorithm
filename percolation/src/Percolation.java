/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF uf2;
    private final boolean[] top, bottom, open;
    private boolean per;
    private final int n;
    private int numberOfOpen;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        this.n = n;
        per = false;
        this.numberOfOpen = 0;
        top = new boolean[n*n];
        bottom = new boolean[n*n];
        open = new boolean[n*n];
        uf2 = new WeightedQuickUnionUF(n*n);     // m[n*n+1]: bottomNode

    }
        // create n-by-n grid, with all sites blocked
    public    void open(int row, int col) {
        // open site (row, col) if it is not open already
        validate(row, col);
        if (isOpen(row, col)) return;
        numberOfOpen++;
        int index = getIndex(row, col);
        open[index] = true;
        int[] neibor = getNeibor(row, col);
        boolean thisTop = false, thisBottom = false;
        if (index < n) thisTop = true;
        if (index >= n*n - n) thisBottom = true;
        for (int i : neibor) {
            if (i == -1 || !open[i]) continue;
            int neiborRoot = uf2.find(i);
            thisTop |= top[neiborRoot];
            thisBottom |= bottom[neiborRoot];
            uf2.union(index, i);
        }
        int thisRoot = uf2.find(index);
        top[thisRoot] = thisTop;
        bottom[thisRoot] = thisBottom;
        if(thisBottom && thisTop) per = true;
    }
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return open[getIndex(row, col)];
        // is site (row, col) open?
    }
    public boolean isFull(int row, int col) {
        // is site (row, col) full?
        validate(row, col);
        return top[uf2.find(getIndex(row, col))];

    }
    public     int numberOfOpenSites() {
        // number of open sites
        return numberOfOpen;

    }
    public boolean percolates() {
        // does the system percolate?
        return per;
    }
    private void validate(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) throw new IllegalArgumentException();
    }
    private int[] getNeibor(int row, int col) {
        int[] re = new int[]{getIndex(row -1, col), getIndex(row +1, col), getIndex(row, col - 1), getIndex(row, col + 1)};
        return re;
    }
    private int getIndex(int row, int col) {
        if (row < 1) return -1; // topNode
        if (row == n+1) return -1; // bottomNode
        if (col == n+1 || col == 0) return -1; // out of boundry
        return (row - 1) * n + col - 1;
    }

    public static void main(String[] args) {
        // test client (optional)
        // String name ="input20.txt";
        // try (Scanner s = new Scanner(new java.io.File(name))) {
        //     int n = s.nextInt();
        //     StdDraw.enableDoubleBuffering();
        //     Percolation perc = new Percolation(n);
        //     PercolationVisualizer.draw(perc, n);
        //     StdDraw.show();
        //     int t = 23;
        //     while (true) {
        //         if(StdDraw.isMousePressed()){
        //             int row = s.nextInt();
        //             int col = s.nextInt();
        //             perc.open(row, col);
        //             PercolationVisualizer.draw(perc, n);
        //             StdDraw.show();
        //             StdOut.println("open :" +row +"," + col +",  percolation" + perc.percolates());
        //         }
        //         StdDraw.pause(200);
        //     }
        // }
        // catch (Exception e) {
        //     e.printStackTrace();
        // }
    }
}


