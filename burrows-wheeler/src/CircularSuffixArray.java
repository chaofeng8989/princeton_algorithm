import edu.princeton.cs.algs4.MSD;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CircularSuffixArray {
    private final String s;
    private int[] index;
    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        this.s = s;
        Map<String, List<Integer>> map = new HashMap<>();
        String[] suffixes = new String[s.length()];
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < s.length(); i++) {
            suffixes[i] = sb.toString();
            if(!map.containsKey(suffixes[i]))map.put(suffixes[i], new LinkedList<>());
            map.get(suffixes[i]).add(i);
            char first = sb.charAt(0);
            sb.deleteCharAt(0);
            sb.append(first);
        }
        MSD.sort(suffixes);
        this.index = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            index[i] = map.get(suffixes[i]).get(0);
            map.get(suffixes[i]).remove(0);
        }
    }

    // length of s
    public int length() {
        return s.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= s.length()) throw new IllegalArgumentException();
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray c = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < 12; i++) System.out.println(c.index(i));
    }

}