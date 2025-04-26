package game;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class OlympicGame {
    private final boolean log;
    private final Player[] players;
    private final ArrayList<Integer> indexPlayers;
    private final int m;
    private final int n;
    private final int k;
    private int numOfPlayers;


    public OlympicGame(boolean log) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Enter the number of players who qualified for the playoffs: ");
            try {
                numOfPlayers = sc.nextInt();
                if (numOfPlayers <= 0) {
                    System.out.println("Cannot be zero players");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("Invalid input, expected integer");
            }
        }
        int first, second, third;
        while (true) {
            PrintStream out = new PrintStream(System.out);
            Scanner in = new Scanner(System.in);
            try {
                System.out.println("Write 3 integers m, n and k:");
                first = in.nextInt();
                second = in.nextInt();
                third = in.nextInt();
                if (first <=0 || second <= 0 || third>Math.max(first,second)){
                    out.println("Board cannot be empty! m, n > 0 (and k <= max(m,n))");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                in.nextLine();
                out.println("Incorrect data input, you have to enter 3 integers");
            }
        }
        this.m = first;
        this.n = second;
        this.k = third;
        this.log = log;
        this.players = new Player[numOfPlayers];
        this.indexPlayers = new ArrayList<>(numOfPlayers);
        for (int i = 0; i < numOfPlayers; i++) {
            this.players[i] = new RandomPlayer();
            this.indexPlayers.add(i);
        }
    }

    public int playTournament() {
        System.out.println(numOfPlayers + " " + ifLog2(numOfPlayers));
        for (int stage = 0; stage < ifLog2(numOfPlayers) + 1; stage++) {
            for (int i = numOfPlayers - 1; i >= 1; i = i - 2) {
                Player player1 = players[i - 1];
                Player player2 = players[i];
                final Game game = new Game(log, player1, player2);
                int result;
                int winnerIndex = -1;
                int loserIndex = -1;
                do {
                    result = game.play(new MNKBoard(m,n,k));
                    if(result == 0){
                        System.out.println("Draw");
                        continue;
                    }
                    winnerIndex = result == 1 ? indexPlayers.get(i - 1) : indexPlayers.get(i);
                    loserIndex = result == 2 ? indexPlayers.get(i - 1) : indexPlayers.get(i);
                    indexPlayers.remove((Integer) loserIndex);
                    System.out.println("Game result between players " + (winnerIndex+1) + " and " + (loserIndex+1) + ": " + (winnerIndex+1));
                } while (result == 0);
            }
            numOfPlayers = numOfPlayers/2 + (numOfPlayers%2);
        }
        return indexPlayers.get(0)+1;
    }

    public int ifLog2(int num) {
        int a = num;
        int k = 0;
        if (a >= 1) {
            while (a > 1) {
                int b = a % 2;
                a /= 2;
                k += 1;
            }
            return k + (a % 2);
        } else {
            return -1;
        }
    }

    public int pow2(int k) {
        int s = 1;
        for (int i = 0; i < k; i++) {
            s *= 2;
        }
        return s;
    }

}
