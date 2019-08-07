import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WordNet {

    private final Map<String, List<Integer>> wordToID;
    private final List<List<String>> wordList;
    private final SAP sap;
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();

        this.wordToID = new HashMap<>();
        this.wordList = new LinkedList<>();
        In in = new In(synsets);
        while (in.hasNextLine()) {
            String s = in.readLine();
            String[] ins = s.split(",");
            int id = Integer.parseInt(ins[0]);
            wordList.add(new LinkedList<>());
            // System.out.println("synsets id: " + ins[0] +", size: " + words.size());
            String[] syns = ins[1].split("\\s+");

            for (int i = 0; i < syns.length; i++) {
                wordList.get(id).add(syns[i]);
                if (!wordToID.containsKey(syns[i])) wordToID.put(syns[i], new LinkedList<>());
                wordToID.get(syns[i]).add(id);

            }

        }

        in = new In(hypernyms);
        Digraph G = new Digraph(wordList.size());
        while (in.hasNextLine()) {
            String s = in.readLine();
            String[] ins = s.split(",");
            // System.out.println("hypernyms id: " + ins[0] + ", size: " + tree.size());
            int num = Integer.parseInt(ins[0]);
            for (int i = 1; i < ins.length; i++) {
                G.addEdge(num, Integer.parseInt(ins[i]));
            }
        }


        int root = 0;
        for (int i = 0; i < G.V(); i++) {
            if (G.outdegree(i) == 0) {
                //System.out.println("root: " + i);
                root++;
            }
        }
        DirectedCycle dc = new DirectedCycle(G);
        if (dc.hasCycle()) throw new IllegalArgumentException();
        //System.out.println("root number: " + root);
        if (root != 1) throw new IllegalArgumentException();
        sap = new SAP(G);


    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        List<String> re = new LinkedList<>();
        for (Map.Entry<String, List<Integer>> e : wordToID.entrySet()) re.add(e.getKey());
        return re;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return wordToID.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        List<Integer> v = wordToID.get(nounA);
        List<Integer> w = wordToID.get(nounB);
        return sap.length(v, w);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        List<Integer> v = wordToID.get(nounA);
        List<Integer> w = wordToID.get(nounB);
        int r = sap.ancestor(v, w);
        String re = "";
        for (String s : wordList.get(r)) {
            re += s;
            re += " ";
        }
        return re.substring(0, re.length() - 1);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");
        Iterable<String> all = wn.nouns();
        System.out.println(wn.isNoun("globulin"));
        System.out.println(wn.distance("collagen", "globulin"));
    }
}