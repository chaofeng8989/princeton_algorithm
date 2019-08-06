import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.List;


public class KdTree {
    private class Node {
        boolean vertical;
        Point2D p;
        Node left, right;
        public Node(Point2D p, boolean vertical) {
            this.p = p;
            this.vertical = vertical;
        }
    }
    private Node root;
    private double minDistance;
    private Point2D nearest;
    // private final Set<Point2D> set;
    private int size;
    public         KdTree() {
        root = null;
        this.size = 0;
    }                              // construct an empty set of points
    public           boolean isEmpty() {
        return this.size == 0;
    }                     // is the set empty?
    public               int size() {
        return this.size;
    }                        // number of points in the set
    public              void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (contains(p)) return;
        Node n = new Node(p, true);
        if (root == null) {
            root = n;
            this.size++;
            return;
        }
        Node tmp = root, prev = null;
        while (tmp != null) {
            prev = tmp;
            if (tmp.vertical) {
                if (tmp.p.x() < n.p.x()) {
                    tmp = tmp.right;
                } else {
                    tmp = tmp.left;
                }
            } else {
                if (tmp.p.y() < n.p.y()) {
                    tmp = tmp.right;
                } else {
                    tmp = tmp.left;
                }
            }
        }
        tmp = prev;
        n.vertical = !tmp.vertical;
        if (tmp.vertical) {
            if (tmp.p.x() < n.p.x()) {
                tmp.right = n;
            } else {
                tmp.left = n;
            }
        } else {
            if (tmp.p.y() < n.p.y()) {
                tmp.right = n;
            } else {
                tmp.left = n;
            }
        }
        this.size++;

    }             // add the point to the set (if it is not already in the set)
    public           boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node node = root;
        while (node != null) {
            if (node.p.equals(p)) return true;
            if (node.vertical) {
                if (node.p.x() < p.x()) node = node.right;
                else node = node.left;

            } else {
                if (node.p.y() < p.y()) node = node.right;
                else node = node.left;
            }
        }
        return false;

    }           // does the set contain point p?
    public              void draw() {
        draw(root, 0, 1, 0, 1);
    }                        // draw all points to standard draw

    private void draw(Node node, double xstart, double xend, double ystart, double yend) {
        if (node == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(node.p.x(), node.p.y());
        StdDraw.setPenRadius();

        if (node.vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.p.x(), ystart, node.p.x(), yend);

            draw(node.left, xstart, node.p.x(), ystart, yend);
            draw(node.right, node.p.x(), xend, ystart, yend);
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(xstart, node.p.y(), xend, node.p.y());
            draw(node.left, xstart, xend, ystart, node.p.y());

            draw(node.right, xstart, xend, node.p.y(), yend);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        List<Point2D> in = new LinkedList<>();
        range(rect, in, root, 0, 1, 0, 1);
        return in;
    }            // all points that are inside the rectangle (or on the boundary)
    private void range(RectHV rect, List<Point2D> in, Node node, double xstart, double xend, double ystart, double yend) {
        if (node == null) return;
        if (rect.contains(node.p)) {
            in.add(node.p);
        }
        if (node.vertical) {

            if (rect.xmax() < node.p.x()) range(rect, in, node.left, xstart, node.p.x(), ystart, yend);
            else if (rect.xmin() > node.p.x()) range(rect, in, node.right, node.p.x(), xend, ystart, yend);
            else {
                range(rect, in, node.left, xstart, node.p.x(), ystart, yend);
                range(rect, in, node.right, node.p.x(), xend, ystart, yend);
            }
        } else {
            if (rect.ymax() < node.p.y()) {
                range(rect, in, node.left, xstart, xend, ystart, node.p.y());
            }
            else if (rect.ymin() > node.p.y()) {
                range(rect, in, node.right, xstart, xend, node.p.y(), yend);

            } else {
                range(rect, in, node.left, xstart, xend, ystart, node.p.y());

                range(rect, in, node.right, xstart, xend, node.p.y(), yend);

            }
        }

    }

    public           Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        nearest = null;
        nearest(p, root, 0, 1, 0, 1);
        return nearest;
    }            // a nearest neighbor in the set to point p; null if the set is empty
    private void nearest(Point2D p, Node node, double xstart, double xend, double ystart, double yend) {
        if (node == null) return;
        double dis = p.distanceSquaredTo(node.p);
        if (nearest == null) {
            nearest = node.p;
            minDistance = dis;
        }
        if (dis < minDistance) {
            minDistance = dis;
            nearest = node.p;
        }
        if (node.vertical) {
            Point2D p1 = new Point2D(node.p.x(), p.y());
            double toline = p1.distanceSquaredTo(p);
            if (p.x() < node.p.x()) {
                nearest(p, node.left, xstart, node.p.x(), ystart, yend);
                if (toline < minDistance) nearest(p, node.right, node.p.x(), xend, ystart, yend);
            } else {
                nearest(p, node.right, node.p.x(), xend, ystart, yend);
                if (toline < minDistance) nearest(p, node.left, xstart, node.p.x(), ystart, yend);
            }

        } else {
            Point2D p1 = new Point2D(p.x(), node.p.y());
            double toline = p1.distanceSquaredTo(p);
            if (p.y() < node.p.y()) {
                nearest(p, node.left, xstart, xend, ystart, node.p.y());
                if (toline < minDistance) nearest(p, node.right, xstart, xend, node.p.y(), yend);
            } else {
                nearest(p, node.right, xstart, xend, node.p.y(), yend);
                if (toline < minDistance) nearest(p, node.left, xstart, xend, ystart, node.p.y());
            }


        }
    }
    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(1.0, 1.0));
        kdTree.insert(new Point2D(0.0, 0.0));
        kdTree.insert(new Point2D(0.0, 0.0));

    }

}
