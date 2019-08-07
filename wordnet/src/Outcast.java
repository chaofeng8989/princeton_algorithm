import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnet;
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }        // constructor takes a WordNet object
    public String outcast(String[] nouns) {

        int[] sum = new int[nouns.length];

        for (int i = 0; i < sum.length; i++) {
            for (int j = i; j < sum.length; j++) {
                int dis = wordnet.distance(nouns[i], nouns[j]);
                sum[i] += dis;
                sum[j] += dis;
            }
        }
        int max = -1;
        int maxi = -1;
        for (int i = 0; i < sum.length; i++) {
            if (sum[i] > max) {
                max = sum[i];
                maxi = i;
            }
        }

//        for (int s : sum) System.out.print(s + ",  ");
        return nouns[maxi];
    }  // given an array of WordNet nouns, return an outcast
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}