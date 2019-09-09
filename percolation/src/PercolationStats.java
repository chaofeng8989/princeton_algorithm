/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int trials;
    private final static double cONF095 = 1.96;
    private final double mean, stddev;
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        this.trials = trials;
        double[] stat = new double[trials];
        Percolation p;
        while (trials > 0) {
            p = new Percolation(n);
            while (!p.percolates()) {
                int row = StdRandom.uniform(n) +1, col = StdRandom.uniform(n) + 1;
                while (p.isOpen(row, col)) {
                    row = StdRandom.uniform(n) +1;
                    col = StdRandom.uniform(n) + 1;
                }
                p.open(row, col);
            }
            trials -= 1;
            stat[trials] = 1.0 * p.numberOfOpenSites()/ n / n;
        }
        mean = StdStats.mean(stat);
        stddev = StdStats.stddev(stat);
    }    // perform trials independent experiments on an n-by-n grid
    public double mean() {
        return mean;
    }                          // sample mean of percolation threshold
    public double stddev() {
        return stddev;
    }                        // sample standard deviationa of percolation threshold
    public double confidenceLo() {
        return mean - cONF095 * stddev / Math.sqrt(trials);
    }                 // low  endpoint of 95% confidence interval
    public double confidenceHi() {

        return mean + cONF095 * stddev / Math.sqrt(trials);
    }                  // high endpoint of 95% confidence interval

    public static void main(String[] args) {
        PercolationStats ps = new PercolationStats(4, 100);
        StdOut.println("mean: " + ps.mean());
        StdOut.println("stddev: " + ps.stddev());
        StdOut.println("95% confidence interval: [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");

    }        // test client (described below)
}
