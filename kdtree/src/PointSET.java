import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Set;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> set;
    public         PointSET() {
        this.set = new TreeSet<>();
    }                              // construct an empty set of points
    public           boolean isEmpty() {
        return set.isEmpty();
    }                     // is the set empty?
    public               int size() {
        return set.size();
    }                        // number of points in the set
    public              void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        set.add(p);
    }             // add the point to the set (if it is not already in the set)
    public           boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return set.contains(p);
    }           // does the set contain point p?
    public              void draw() {
        for (Point2D p : set) {
            StdDraw.point(p.x(), p.y());
        }
    }                        // draw all points to standard draw
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Set<Point2D> in = new TreeSet<>();
        for (Point2D p : set) {
            if (rect.contains(p)) in.add(p);
        }
        return in;
    }            // all points that are inside the rectangle (or on the boundary)
    public           Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        double nearest = Double.POSITIVE_INFINITY;
        Point2D pn = null;
        for (Point2D e : set) {
            if (e.distanceSquaredTo(p) < nearest) {
                pn = e;
                nearest = e.distanceSquaredTo(p);
            }
        }
        return pn;
    }            // a nearest neighbor in the set to point p; null if the set is empty

             // unit testing of the methods (optional)
}