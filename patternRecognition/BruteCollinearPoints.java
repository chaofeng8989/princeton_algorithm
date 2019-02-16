import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class BruteCollinearPoints {


    private final List<LineSegment> lines;

    /**
     * finds all line segments containing 4 points
     *
     * @param points all points
     */
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        points = points.clone();
        validate(points);
        Arrays.sort(points);
        validateDuplicate(points);
        List<List<Double>> slopes = new ArrayList<>(points.length);
        for (int i = 0; i < points.length; i++) slopes.add(new LinkedList<>());
        lines = new LinkedList<>();
        for (int i = 0; i < points.length; i++) {
           for (int j = i+1; j < points.length; j++) {
               for (int m = j + 1; m < points.length; m++) {
                   for (int n = points.length - 1; n > m; n--) {
                       if (isCollinear(points[i], points[j], points[m], points[n])) {
                           boolean exist = false;
                           for (double slope : slopes.get(i)) {
                               if (Double.compare(slope, points[i].slopeTo(points[n])) == 0) exist = true;
                           }
                           if (!exist) {
                               lines.add(new LineSegment(points[i], points[n]));
                               double s = points[i].slopeTo(points[n]);
                               slopes.get(i).add(s);
                               slopes.get(j).add(s);
                               slopes.get(m).add(s);
                               slopes.get(n).add(s);
                           }
                       }
                   }
               }
           }
        }
    }
    private void validateDuplicate(Point[] points) {
        for (int i = 1; i < points.length; i++) {
            if (points[i - 1].compareTo(points[i]) == 0) throw new IllegalArgumentException();
        }
    }
    private void validate(Point[] points) {
        for (Point p : points)
            if (p == null) throw new IllegalArgumentException();
    }

    private boolean isCollinear(Point p1, Point p2, Point p3, Point p4) {
//        StdOut.println(p1+ "to" + p2 + ": "+ p1.slopeTo(p2)+","+ p1+" to"+ p3+":" + p1.slopeTo(p3) +","+ p1+" to "+p4+":" + p1.slopeTo(p4));
        return (Double.compare(p1.slopeTo(p2), p1.slopeTo(p3)) == 0 && Double.compare(p1.slopeTo(p2), p1.slopeTo(p4)) == 0);
    }

    public           int numberOfSegments() {
        return lines.size();
    }       // the number of line segments

    public LineSegment[] segments() {
        LineSegment[] re = new LineSegment[lines.size()];
        lines.toArray(re);
        return re;
    }               // the line segments

//    private void trim () {
//        if (lines.size() == 0) return;
//        Collections.sort(lines);
//        Line start = lines.get(0);
//        List<Line> list = new LinkedList<>();
//        Point end = null;
//        for (Line l : lines) {
//            if (l.slope == start.slope) {
//               if (end == null || end.compareTo(l.q) < 0) end = l.q;
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
        In in = new In("input6.txt");
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}