package game;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final OlympicGame game = new OlympicGame(false);
        int result = game.playTournament();
        System.out.println("OlympicGame result: " + result);
    }
}
