import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class FastCollinearPoints {

    private final List<LineSegment> lines;
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        validate(points);
        points = points.clone();
        Arrays.sort(points);
        validateDuplicate(points);
        lines = new LinkedList<>();
        for (int m = 0; m < points.length; m++) {
            Point p = points[m];
            Point[] tmp = points.clone();
            Arrays.sort(tmp, p.slopeOrder());
            int count = 0;
            double existSlope = Double.NEGATIVE_INFINITY;
            for (int i = 1; i < tmp.length; i++) {
                if (Double.compare(p.slopeTo(tmp[i-1]), p.slopeTo(tmp[i])) == 0 && Double.compare(p.slopeTo(tmp[i-1]), existSlope) != 0) {
                    if (p.compareTo(tmp[i-1]) > 0) {
                        count = 0;
                        existSlope = p.slopeTo(tmp[i-1]);
                    }
                    else count++;
                } else {
                    if (count >= 3) {
//                        StdOut.println(count);
                        lines.add(new LineSegment(p, tmp[i-1]));
                    }
                    count = 1;
                }
            }
            if (count >= 3) {
//                StdOut.println(count);
                lines.add(new LineSegment(p, tmp[tmp.length - 1]));
            }
        }
//        StdOut.println("done");

    }     // finds all line segments containing 4 or more points

    private void validate(Point[] points) {
        for (Point p : points)
            if (p == null) throw new IllegalArgumentException();
    }
    private void validateDuplicate(Point[] points) {
        for (int i = 1; i < points.length; i++) {
            if (points[i - 1].compareTo(points[i]) == 0) throw new IllegalArgumentException();
        }
    }
    public           int numberOfSegments() {
        return lines.size();
    }        // the number of line segments
    public LineSegment[] segments() {
        LineSegment[] re = new LineSegment[lines.size()];
        lines.toArray(re);
        return re;
    }                // the line segments


//  private void trim () {
//        if (lines.size() == 0) return;
//        Collections.sort(lines);
//        Line start = lines.get(0);
//        List<Line> list = new LinkedList<>();
//        Point end = null;
//        for (Line l : lines) {
//            if (l.slope == start.slope) {
//                if (end == null || end.compareTo(l.q) < 0) end = l.q;
//            } else {
//                list.add(new Line(start.p, end, start.slope));
//                start = l;
//                end = l.q;
//            }
//        }
//        list.add(new Line(start.p, end, start.slope));
//        lines = list;
//
//
//    }
    
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In("input8.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        StdOut.println("size: " + collinear.numberOfSegments());
    }
}