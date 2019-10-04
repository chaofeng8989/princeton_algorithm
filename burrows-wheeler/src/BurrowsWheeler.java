import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.MSD;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        BinaryStdIn.close();
        CircularSuffixArray c = new CircularSuffixArray(s);
        char[] trans = new char[s.length()];
        int original = -1;
        for (int i = 0; i < s.length(); i++) {
            if (c.index(i) == 0) original = i;
            trans[i] = s.charAt((c.index(i) - 1 + s.length()) % s.length() );
        }
        BinaryStdOut.write((Integer)original);
        for (char ch : trans) BinaryStdOut.write((Character)ch);
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first= BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();

        char[] tArray = t.toCharArray();
        char[] sortedArray = tArray.clone();
        Arrays.sort(sortedArray);

        int[] next = buildNext(sortedArray, tArray);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < t.length(); i++) {
            sb.append(sortedArray[first]);
            first = next[first];
        }
        BinaryStdOut.write(sb.toString());
        BinaryStdOut.close();
    }

    private static int[] buildNext(char[] sortedArray, char[] tArray) {
        int[] next = new int[tArray.length];
        Map<Character, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < tArray.length; i++) {
            if (!map.containsKey(tArray[i])) map.put(tArray[i], new LinkedList<>());
            map.get(tArray[i]).add(i);
        }
        for (int i = 0; i < tArray.length; i++) {
            next[i] = map.get(sortedArray[i]).get(0);
            map.get(sortedArray[i]).remove(0);
        }
//        for (int i : next)StdOut.print(i + ", ");
        return next;
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0] .equals("-")) {
            transform();
        }
        else inverseTransform();
    }

}