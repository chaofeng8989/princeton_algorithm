/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */


import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        Node next, prevous;
        Item item;

        public Node(Item item) {
            this.item = item;
        }
    }

    private Node first, last;
    private int size;

    public Deque() {
        this.size = 0;
        first = new Node(null);
        last = new Node(null);
        first.next = last;
        last.prevous = first;
    }                          // construct an empty deque

    public boolean isEmpty() {
        return size == 0;
    }                // is the deque empty?

    public int size() {
        return size;
    }                      // return the number of items on the deque

    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node i = new Node(item);
        i.next = first.next;
        first.next.prevous = i;
        i.prevous = first;
        first.next = i;
        size++;
    }         // add the item to the front

    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node i = new Node(item);
        i.prevous = last.prevous;
        last.prevous.next = i;
        i.next = last;
        last.prevous = i;
        size++;
    }          // add the item to the end

    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Node i = first.next;
        first.next = i.next;
        i.next.prevous = first;
        size--;
        return i.item;
    }               // remove and return the item from the front

    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        Node i = last.prevous;
        last.prevous = i.prevous;
        i.prevous.next = last;
        size--;
        return i.item;
    }                // remove and return the item from the end

    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            Node current = first.next;

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean hasNext() {
                return current != last;
            }

            @Override
            public Item next() {
                if (!hasNext()) throw new NoSuchElementException();
                Item i = current.item;
                current = current.next;
                return i;
            }
        };
    }        // return an iterator over items in order from front to end





    public static void main(String[] args){
        Deque<Integer> d = new Deque<>();
        d.addFirst(2);
        StdOut.println(d.isEmpty());
        StdOut.println(d.removeLast());
    }  // unit testing (optional)

}

