import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

public class Permutation {
    public static void main(String[] args) {
        //int n = Integer.parseInt(args[0]);
        //String[] input = StdIn.readAllLines();
        //int n = 8;
//        try {
//            Scanner s = new Scanner(new File("duplicates.txt"));
//
//            RandomizedQueue<String> rq = new RandomizedQueue<>();
//            while (s.hasNext()) {
//                rq.enqueue(s.next());
//            }
//            Iterator<String> it = rq.iterator();
//            while (n-- > 0 && it.hasNext()) {
//                StdOut.println(it.next());
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        int n = Integer.parseInt(args[0]);
        if (n == 0) return;
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        int i = 1;
        while (!StdIn.isEmpty()) {
            String input = StdIn.readString();
            if (rq.size() == n) {
                if (StdRandom.uniform(1, i+1) <= n) {
                    rq.dequeue();
                    rq.enqueue(input);
                }

            } else rq.enqueue(input);
            i++;
        }

        Iterator<String> it = rq.iterator();
        while (n-- > 0 && it.hasNext()){
            StdOut.println(it.next());
        }

    }
}
