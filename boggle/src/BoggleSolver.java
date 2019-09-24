import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver
{
    private Node root;

    private class Node {
        int score;
        Node[] next = new Node[26];
    }
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        root = new Node();
        for (String word : dictionary) {
            if (score(word) > 0) put(word, score(word));
        }
    }
    private void put(String key, int score) {
        root = put(root, key, score, 0);
    }
    private Node put(Node x, String key, int score, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.score = score;
            return x;
        }
        char c = key.charAt(d);
        x.next[c- 'A'] = put(x.next[c - 'A'], key, score, d+1);
        return x;
    }
//    private boolean contains(String key) {
//        return get(key) != 0;
//    }

    private int get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return 0;
        return x.score;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c - 'A'], key, d+1);
    }


    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> validWords = new HashSet<>();

        boolean[][] visited = new boolean[board.rows()][];
        for (int i = 0; i < board.rows(); i++) visited[i] = new boolean[board.cols()];
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                dfs(board, i, j, visited, validWords, root, new StringBuilder());
            }
        }
        return validWords;
    }


    // dfs search for all valid words
    private void dfs(BoggleBoard board, int i, int j, boolean[][] visited, Set<String> validWords, Node node, StringBuilder prefix) {
        if (i < 0 || i >= board.rows() || j < 0 || j >= board.cols()) return;
        if (visited[i][j]) return;
        char c = board.getLetter(i, j);
        if (node.next[c - 'A'] == null) return;
        prefix.append(c);
        Node store = null;
        if (c == 'Q') {
            store = node;
            node = node.next[c - 'A'];
            c = 'U';
            if (node.next[c - 'A'] == null) {
                prefix.deleteCharAt(prefix.length() - 1);

                visited[i][j] = false;
                return;
            }
            prefix.append('U');
        }
        visited[i][j] = true;

        if (node.next[c - 'A'].score != 0) validWords.add(prefix.toString());
        dfs(board, i - 1, j - 1, visited, validWords, node.next[c - 'A'], prefix);
        dfs(board, i - 1, j, visited, validWords, node.next[c - 'A'], prefix);
        dfs(board, i - 1, j + 1, visited, validWords, node.next[c - 'A'], prefix);
        dfs(board, i, j - 1, visited, validWords, node.next[c - 'A'], prefix);
        dfs(board, i, j + 1, visited, validWords, node.next[c - 'A'], prefix);
        dfs(board, i + 1, j - 1, visited, validWords, node.next[c - 'A'], prefix);
        dfs(board, i + 1, j, visited, validWords, node.next[c - 'A'], prefix);
        dfs(board, i + 1, j + 1, visited, validWords, node.next[c - 'A'], prefix);
        visited[i][j] = false;
        prefix.deleteCharAt(prefix.length() - 1);
        if (store != null) {
//            node = store;
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    private int score(String word) {
        int len = word.length();

        if (len < 3) return 0;
        if (len < 5) return 1;
        if (len < 6) return 2;
        if (len < 7) return 3;
        if (len < 8) return 5;
        return 11;
    }
    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)

    public int scoreOf(String word) {
        int len = word.length();
        if (len < 3) return 0;
        return get(word);
    }




    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        // System.out.println(solver.get("AID"));
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0, num = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            num++;
            score += solver.scoreOf(word);
        }
        StdOut.println("Total Number = " + num);
        StdOut.println("Score = " + score);
    }
}
