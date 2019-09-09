import edu.princeton.cs.algs4.Picture;



public class SeamCarver {
    // private Picture picture;
    private int[][] pic;            // 4byte n*n
    private int width, height;      //  ~0
    private double[][] energy;     //   8byte n*n
    // private int[][] lastEdge;      //  4byte n*n
                                   // total 16byte n*n,    should be 12byte n*n to collect all points.
                                   //  solution: use first 8bit of pic(int[][])  to store lastEdge
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.height = picture.height();
        this.width = picture.width();
        pic = new int[width][];
        for (int i = 0; i < width; i++) {
            pic[i] = new int[height];
            for (int j = 0; j < height; j++) {
                pic[i][j] = picture.getRGB(i, j);
            }
        }
    }

    // current picture
    public Picture picture() {
        Picture picture = new Picture(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                picture.setRGB(i, j, pic[i][j]);
            }
        }
        return picture;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x >= width || x < 0 || y >= height || y < 0) throw new IllegalArgumentException();
        if (energy == null) energy = buildEnergyPicture();
        return energy[x][y];
    }

    private int findMin(double i, double j, double k) {
        if (i <= j && i <= k) return -1;
        if (j <= i && j <= k) return 0;
        else return 1;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        this.energy = buildEnergyPicture();
        double first, second, third;
        double[] lastDis = new double[height];
        for (int i = 0; i < width; i++) {
            double[] newDis = new double[height];
            newDis[0] = Double.POSITIVE_INFINITY;
            newDis[height - 1] = Double.POSITIVE_INFINITY;
            for (int j = 0; j < height; j++) {
                if (j-1 >= 0) first = lastDis[j-1];
                else first = Double.POSITIVE_INFINITY;
                second = lastDis[j];
                if (j+1 < height) third = lastDis[j+1];
                else third = Double.POSITIVE_INFINITY;
                int minIndex = findMin(first, second, third);
                newDis[j] = energy[i][j] + lastDis[j+minIndex];
                // lastEdge[i][j] = j + minIndex;
                pic[i][j]  = pic[i][j] & 0x00ffffff;
                if (minIndex == -1) {
                    pic[i][j]  = pic[i][j] | 0x01000000;
                } else if (minIndex == 0) {
                    pic[i][j]  = pic[i][j] | 0x00000000;  // no change
                } else {
                    pic[i][j]  = pic[i][j] | 0x10000000;
                }
            }
            lastDis = newDis;
        }

        int[] seam = new int[width];
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < height; i++) {
            if (min > lastDis[i]) {
                min = lastDis[i];
                seam[width - 1] = i;
            }
        }
        for (int i = width - 1; i > 0; i--) {
            // seam[i - 1] = lastEdge[i][seam[i]];
            int j = seam[i];
            if ((pic[i][j] >> 24) == 0x01) {
                seam[i-1] = j-1;
            } else if ((pic[i][j] >> 24) == 0x00) {
                seam[i-1] = j;
            } else {
                seam[i-1] = j+1;
            }
        }

        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        this.energy = buildEnergyPicture();
        double[] lastDis = new double[width];
        double first, second, third;
        for (int j = 0; j < height; j++) {
            double[] newDis = new double[width];
            newDis[0] = Double.POSITIVE_INFINITY;
            newDis[width - 1] = Double.POSITIVE_INFINITY;
            for (int i = 0; i < width; i++) {
                if (i-1 >= 0) first = lastDis[i-1];
                else first = Double.POSITIVE_INFINITY;
                second = lastDis[i];
                if (i+1 < width) third = lastDis[i+1];
                else third = Double.POSITIVE_INFINITY;
                int minIndex = findMin(first, second, third);
                newDis[i] = energy[i][j] + lastDis[i+minIndex];
                // lastEdge[i][j] = i + minIndex;
                pic[i][j]  = pic[i][j] & 0x00ffffff;
                if (minIndex == -1) {
                    pic[i][j]  = pic[i][j] | 0x01000000;
                } else if (minIndex == 0) {
                    pic[i][j]  = pic[i][j] | 0x00000000;  // no change
                } else {
                    pic[i][j]  = pic[i][j] | 0x10000000;
                }
            }
            lastDis = newDis;
        }

        int[] seam = new int[height];
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < width; i++) {
            if (min > lastDis[i]) {
                min = lastDis[i];
                seam[height - 1] = i;
            }
        }
        for (int j = height - 1; j > 0; j--) {
            // seam[i - 1] = lastEdge[seam[i]][i];



            int i = seam[j];
            if ((pic[i][j] >> 24) == 0x01) {
                seam[j-1] = i-1;
            } else if ((pic[i][j] >> 24) == 0x00) {
                seam[j-1] = i;
            } else {
                seam[j-1] = i+1;
            }



        }

        return seam;
    }

    private void verifySeam(int[] seam, int range) {
        if (seam[0] >= range || seam[0] < 0) throw new IllegalArgumentException();
        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i] - seam[i-1]) > 1) throw new IllegalArgumentException();
            if (seam[i] >= range || seam[i] < 0) throw new IllegalArgumentException();
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != width) throw new IllegalArgumentException();
        verifySeam(seam, height);
        if (height <= 1) throw new IllegalArgumentException();
        int[][] newP = new int[width][];
        for (int i = 0; i < width; i++) {
            newP[i] = new int[height - 1];
        }
        for (int i = 0; i < width; i++) {
            for (int jn = 0, jo = 0; jn < height-1; jn++, jo++) {
                if (seam[i] == jo) jn--;
                else newP[i][jn] = pic[i][jo];
            }
        }
        this.pic = newP;
        height--;
        energy = null;
    }



    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length != height) throw new IllegalArgumentException();
        verifySeam(seam, width);
        if (width <= 1) throw new IllegalArgumentException();
        int[][] newP = new int[width - 1][];
        for (int i = 0; i < width-1; i++) {
            newP[i] = new int[height];
        }

        for (int j = 0; j < height; j++) {
            for (int io = 0, in = 0; in < width - 1; in++, io++) {
                if (seam[j] == io) in--;
                else newP[in][j] = pic[io][j];
            }
        }
        this.pic = newP;
        width--;
        energy = null;
    }



    private double[][] buildEnergyPicture() {
        //
        double[][] localEnergy = new double[width][];
        for (int i = 0; i < width; i++) {
            localEnergy[i] = new double[height];
            for (int j = 0; j < height; j++) {
                localEnergy[i][j] = getEnergy(i, j);
            }
        }

        return localEnergy;
    }
    private double getEnergy(int i, int j) {

        if (i == 0 || i == width - 1 || j == 0 || j == height - 1) return 1000;
        int rx = 0, gx = 0, bx = 0, ry = 0, gy = 0, by = 0;
        rx = ((pic[i - 1][j] >> 16) & 0xFF) - ((pic[i + 1][j] >> 16) & 0xFF);


        gx = ((pic[i - 1][j] >> 8) & 0xFF) - ((pic[i + 1][j] >> 8) & 0xFF);
        bx = ((pic[i - 1][j] >> 0) & 0xFF) - ((pic[i + 1][j] >> 0) & 0xFF);


        ry = ((pic[i][j - 1] >> 16) & 0xFF) - ((pic[i][j + 1] >> 16) & 0xFF);
        gy = ((pic[i][j - 1] >> 8) & 0xFF) - ((pic[i][j + 1] >> 8) & 0xFF);
        by = ((pic[i][j - 1] >> 0) & 0xFF) - ((pic[i][j + 1] >> 0) & 0xFF);
        double value = Math.sqrt(rx*rx + gx*gx +bx*bx + ry*ry +gy*gy + by*by);
        return value;
    }


    public static void main(String[] args) {
        Picture inputImg = new Picture("Seam/resource/7x10.png");
        SeamCarver carver = new SeamCarver(inputImg);
        int[]  seam = carver.findHorizontalSeam();
        carver.removeHorizontalSeam(seam);


    }
}