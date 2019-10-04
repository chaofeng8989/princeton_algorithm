import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.HexDump;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MoveToFront {
    private static final int R = 256;
    private static final int lgR = 8;
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        List<Character> list = new LinkedList<>();
        for (char c = 0; c < 256; c++) list.add(c);
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar(lgR);
            char index = (char)list.indexOf(c);
            BinaryStdOut.write(index);

            list.remove((Character)c);
            list.add(0, c);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] list = new char[256];
        for (char c = 0; c < 256; c++) list[c] = c;
        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readInt(8);
            char c = list[index];
            BinaryStdOut.write(list[index]);
            System.arraycopy(list, 0, list, 1, index);
            list[0] = c;
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0] .equals("-")) {
            encode();
        }
        else decode();
    }

}