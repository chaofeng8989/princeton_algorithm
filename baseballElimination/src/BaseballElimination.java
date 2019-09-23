import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.StdOut;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class BaseballElimination {
    private static boolean debug = false;
    private int n;
    private int[][] board;
    private List<String> teams;
    private int[] remain;
    private Map<String, Integer> map;


    private void init(String fileName) {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            Scanner line = new Scanner(scanner.nextLine());
            n = line.nextInt();
            map = new HashMap<>();
            board = new int[n][];
            remain = new int[(n*n - n) / 2];
            for (int i = 0; i < n; i++) board[i] = new int[3];
            int teamNumber = 0;
            while (scanner.hasNextLine()) {
                line = new Scanner(scanner.nextLine());
                String team = line.next();
                teams.add(team);
                map.put(team, teamNumber);
                board[teamNumber][0] = line.nextInt();
                board[teamNumber][1] = line.nextInt();
                board[teamNumber][2] = line.nextInt();
                for (int i = 0; i < n; i++) {
                    int opRemain = line.nextInt();
                    if (i <= teamNumber) continue;
                    remain[getMatchVertical(teamNumber, i) - n] = opRemain;

                }
                teamNumber++;
            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private FlowNetwork buildFlownetwork(String team){

        /*
                source 1   (# (n + n*n) / 2 )
                destination 1 (# (n + n*n) / 2 + 1)
                matches  n*(n-1)/2   {n  ->  (n + n*n) / 2}
                teams n            {0 - n - 1}
                total = n*n + 2;


             */

        FlowNetwork network = new FlowNetwork((n *n + n)/ 2+ 2);
        int teamNumber = map.get(team);



        for (int i = 0; i < n; i++) {
            if (i == teamNumber) continue;
            for (int j = i+1; j < n; j++) {
                if (j == teamNumber) continue;

                //  matches to teams
                int match = getMatchVertical(i, j);
                if(debug)System.out.println("matched to teams: " + match + "-> (" +i+","+ j + ")  :max");

                network.addEdge(new FlowEdge(match, i, Integer.MAX_VALUE));
                network.addEdge(new FlowEdge(match, j, Integer.MAX_VALUE));

                // source to matches
                int index = getMatchVertical(i, j);
                if(debug)System.out.println("source to matched: " + getSource() +"-> " + index + ": " + remain[index - n]);
                FlowEdge edge = new FlowEdge(getSource(), index, remain[index - n]);
                network.addEdge(edge);
            }
        }

        // team to destination
        int max = board[teamNumber][0] + board[teamNumber][2];
        for (int i = 0; i < n; i++) {
            if (i == teamNumber) continue;
            if(debug)System.out.println("team to destination: " + i + "-> " + getDestination() + " :" + (max - board[i][0]));
            FlowEdge edge = new FlowEdge(i, getDestination(), max - board[i][0]);
            network.addEdge(edge);
        }
        return network;
    }
    private int getMatchVertical(int i, int j){
        return (2 * n - i - 1)*i/2 + j - i - 1 + n;
    }
    private int getTeam(int team) {
        return team;
    }
    private int getSource() {
        return (n*n +n) / 2;
    }
    private int getDestination() {
        return (n*n +n) / 2 + 1;
    }
    public BaseballElimination(String filename) {
        teams = new LinkedList<>();
        init(filename);
    }                   // create a baseball division from given filename in format specified below
    public              int numberOfTeams() {
        return n;
    }                       // number of teams
    public Iterable<String> teams() {
        return teams;
    }                               // all teams
    public              int wins(String team) {
        validate(team);
        int teamNumber = map.get(team);
        return board[teamNumber][0];
    }                     // number of wins for given team
    public              int losses(String team) {
        validate(team);
        int teamNumber = map.get(team);
        return board[teamNumber][1];    }                   // number of losses for given team
    public              int remaining(String team) {
        validate(team);
        int teamNumber = map.get(team);
        return board[teamNumber][2];
    }                // number of remaining games for given team
    public              int against(String team1, String team2) {
        validate(team1);
        validate(team2);
        int t1 = map.get(team1), t2 = map.get(team2);
        if (t1 == t2) return 0;
        if (t1 > t2){
            int tmp = t1;
            t1 = t2;
            t2 = tmp;
        }
        return remain[getMatchVertical(t1, t2) - n];
    }   // number of remaining games between team1 and team2
    private FordFulkerson maxflow(String team) {
        FlowNetwork network = buildFlownetwork(team);


        FordFulkerson ffk = new FordFulkerson(network, getSource(), getDestination());
        if(debug)for (int i = 0; i < getDestination()+ 1; i++) System.out.print(ffk.inCut(i) +", ");
        if(debug)System.out.println("\n   " + ffk.value());
        return ffk;
    }


    public          boolean isEliminated(String team) {
        validate(team);
        int teamNumber = map.get(team);
        int max = board[teamNumber][0] + board[teamNumber][2];
        for (int i = 0; i < n; i++) {
            if (max - board[i][0] < 0) return true;
        }

        FordFulkerson ffk = maxflow(team);
        for (int i = n; i < (n * n + n) / 2; i++) {
            if (ffk.inCut(i)) return true;
        }
        return false;
    }             // is given team eliminated?
    public Iterable<String> certificateOfElimination(String team) {
        validate(team);
        Set<String> re = new HashSet<>();
        int teamNumber = map.get(team);
        int max = board[teamNumber][0] + board[teamNumber][2];
        for (int i = 0; i < n; i++) {
            if (max - board[i][0] < 0) {
                re.add(teams.get(i));
                return re;
            }
        }
        FordFulkerson ffk = maxflow(team);
        for (int i = n; i < (n * n + n) / 2; i++) {
            if (remain[i-n] != 0 && ffk.inCut(i)) add(i, re);
        }
        re.remove(team);
        if (re.isEmpty()) return null;
        return re;
    } // subset R of teams that eliminates given team; null if not eliminated

    private void add(int num, Set<String> set) {
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                if (getMatchVertical(i, j) == num ) {
                    set.add(teams.get(i));
                    set.add(teams.get(j));
                }
            }
        }
    }
    private void validate(String team) {
        if (!map.containsKey(team)) throw new IllegalArgumentException();
    }

    private void print() {
        for (int i = 0; i < n; i++) {
            System.out.print(teams.get(i)+"    ");

            System.out.print(board[i][0] + " " + board[i][1] + " " + board[i][2] + "   ");
            for (int j = i+1; j < n; j++)System.out.print(remain[getMatchVertical(i, j) - n] + " ");
            System.out.println();
        }
    }
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        //division.print();
        //debug = true;
        for (String team : division.teams()) {
            //if (!team.equals("Philadelphia") ) continue;
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
