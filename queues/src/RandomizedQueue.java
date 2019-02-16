/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */


import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int size, start;
    public RandomizedQueue() {
        items = (Item[]) new Object[10];
        this.size = 0;
        this.start = 0;

    }                // construct an empty randomized queue
    public boolean isEmpty() {
        return size == 0;
    }                // is the randomized queue empty?
    public int size() {
        return size;
    }                       // return the number of items on the randomized queue
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (size == items.length){
            Item[] newItems = (Item[]) new Object[2 * size];
            for (int i = 0; i < size; i++) newItems[i] = items[(start + i) % size];
            newItems[size] = item;
            items = newItems;
            start = 0;
        } else {
            int index = (start + this.size ) % items.length;
            items[index] = item;
        }
        size ++;
    }           // add the item
    public Item dequeue() {
        if (size == 0) throw new NoSuchElementException();
        int lastIndex = (start + size - 1) % items.length;
        int ran = (start + StdRandom.uniform(size)) % items.length;
        Item item = items[ran];
        items[ran] = items[lastIndex];
        items[lastIndex] = null;
        size --;
        if (items.length >10 && items.length >= 4 * size) {
            Item[] newItems = (Item[]) new Object[items.length / 2];
            for (int i = 0; i < size; i++) {
                newItems[i] = items[(i+start) % items.length];
            }
            start = 0;
            items = newItems;
        }
        return item;
    }                    // remove and return a random item
    public Item sample() {
        if (size == 0) throw new NoSuchElementException();
        int index = StdRandom.uniform(size);
        return items[(index + start) % items.length];
    }                    // return a random item (but do not remove it)ååå

    private class RandomIterator implements Iterator<Item>{
        int current;
        int[] shuffle;
        public RandomIterator() {
            shuffle = new int[size];
            for (int i = 0; i < size; i++) shuffle[i] = i;
            shuffleArray();
            this.current = 0;
        }
        void shuffleArray(){
            for (int i = size - 1; i >=0; i--) {
                int x = StdRandom.uniform(i+1);
                int tmp = shuffle[i];
                shuffle[i] = shuffle[x];
                shuffle[x] = tmp;
            }
        }
        @Override
        public boolean hasNext() {
            return this.current < size;
        }

        @Override
        public Item next() {
            if(!hasNext()) throw new NoSuchElementException();
            int index = (shuffle[current] + start) % items.length;
            current++;
            return items[index];

        }
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public Iterator<Item> iterator() {
        return new RandomIterator();
    }        // return an independent iterator over items in random order
    public static void main(String[] args) {
        RandomizedQueue<Integer> r = new RandomizedQueue<>();
        for (int i = 0; i < 200; i++){
            r.enqueue(i);

        }
        int[] x = new int[200];
        for (int i = 0; i < 200; i++) {
            x[i] = r.dequeue();
            StdOut.println(x[i]);
        }
        Arrays.sort(x);
        for (int i : x) StdOut.print(i +", ");

    }  // unit testing (optional)
}
