import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;

    private double[][] energy;
    private double[][] dis;
    private int[][] lastEdge;
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (energy == null) energy = buildEnergyPicture(picture);
        return energy[x][y];
    }

    private int findMin(double i, double j, double k) {
        if (i <= j && i <= k) return -1;
        if (j <= i && j <= k) return 0;
        else return 1;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        init(picture);
        for (int i = 0; i < picture.width(); i++) {
            for (int j = 1; j < picture.height() - 1; j++) {
                if (i == 0) {
                    dis[i][j] = energy[i][j];
                    continue;
                }
                int minIndex = findMin(dis[i-1][j-1], dis[i-1][j], dis[i-1][j+1]);
                dis[i][j] = energy[i][j] + dis[i-1][j+minIndex];
                lastEdge[i][j] = j + minIndex;
            }
        }

        int[] seam = new int[picture.width()];
        double min = Double.MAX_VALUE;
        for (int i = 0; i < picture.height(); i++) {
            if (min > dis[picture.width() - 1][i]) {
                min = dis[picture.width() -1][i];
                seam[picture.width() - 1] = i;
            }
        }
        for (int i = picture.width() - 1; i > 0; i--) {
            seam[i - 1] = lastEdge[i][seam[i]];
        }

        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        init(picture);
        for (int j = 0; j < picture.height(); j++) {
            for (int i = 1; i < picture.width() - 1; i++) {
                if (j == 0) {
                    dis[i][j] = energy[i][j];
                    continue;
                }
                int minIndex = findMin(dis[i-1][j-1], dis[i][j-1], dis[i+1][j-1]);
                dis[i][j] = energy[i][j] + dis[i+minIndex][j-1];
                lastEdge[i][j] = i + minIndex;
            }
        }

        int[] seam = new int[picture.height()];
        double min = Double.MAX_VALUE;
        for (int i = 0; i < picture.width(); i++) {
            if (min > dis[i][picture.height() - 1]) {
                min = dis[i][picture.height() -1];
                seam[picture.height() - 1] = i;
            }
        }
        for (int i = picture.height() - 1; i > 0; i--) {
            seam[i - 1] = lastEdge[seam[i]][i];
        }

        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        Picture newP = new Picture(picture.width(), picture.height() - 1);
        for (int i = 0; i < newP.width(); i++) {
            for (int jn = 0, jo = 0; jn < newP.height(); jn++, jo++) {
                if (seam[i] == jo) jn--;
                else newP.set(i, jn, picture.get(i, jo));
            }
        }
        this.picture = newP;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        Picture newP = new Picture(picture.width() - 1, picture.height());
        for (int j = 0; j < newP.height(); j++) {
            for (int io = 0, in = 0; io < newP.width(); in++, io++) {
                if (seam[j] == io) in--;
                else newP.set(in, j, picture.get(io, j));
            }
        }
        this.picture = newP;
    }


    private void init(Picture picture) {
        dis = new double[picture.width()][];
        lastEdge = new int[picture.width()][];
        for (int i = 0; i < picture.width(); i++) {
            dis[i] = new double[picture.height()];
            lastEdge[i] = new int[picture.height()];
            for (int j = 0; j < picture.height(); j++) {
                dis[i][j] = Double.MAX_VALUE;
                lastEdge[i][j] = -1;
            }
        }
        this.energy = buildEnergyPicture(picture);

    }


    private double[][] buildEnergyPicture(Picture p) {
        //
        double[][] energy = new double[p.width()][];
        for (int i = 0; i < p.width(); i++) {
            energy[i] = new double[p.height()];
            if (i == 0 || i == p.width() - 1) {
                for (int j = 0; j < p.height(); j++) {
                    energy[i][j] = 1000;
                }
            }
            energy[i][0] = 1000;
            energy[i][p.height() - 1] = 1000;
        }
        int rx = 0, gx = 0, bx = 0, ry = 0, gy = 0, by = 0;
        for (int i = 1; i < p.width() - 1; i++) {
            for (int j = 1; j < p.height() - 1; j++) {
                rx = p.get(i - 1, j).getRed() - p.get(i + 1, j).getRed();
                gx = p.get(i - 1, j).getGreen() - p.get(i + 1, j).getGreen();
                bx = p.get(i - 1, j).getBlue() - p.get(i + 1, j).getBlue();
                ry = p.get(i, j - 1).getRed() - p.get(i, j + 1).getRed();
                gy = p.get(i, j - 1).getGreen() - p.get(i, j + 1).getGreen();
                by = p.get(i, j - 1).getBlue() - p.get(i, j + 1).getBlue();
                double value = Math.sqrt(rx*rx + gx*gx +bx*bx + ry*ry +gy*gy + by*by);
                energy[i][j] = value;
            }
        }
        return energy;
    }


    //  unit testing (optional)
    public static void main(String[] args) {

    }

}