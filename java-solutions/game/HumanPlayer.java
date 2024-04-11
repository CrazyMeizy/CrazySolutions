package game;

import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;


public class HumanPlayer implements Player {
    private final PrintStream out;
    private final Scanner in;

    public HumanPlayer(final PrintStream out, final Scanner in) {
        this.out = out;
        this.in = in;
    }

    public HumanPlayer() {
        this(System.out, new Scanner(System.in));
    }

    @Override
    public Move move(final Position position, final Cell cell) throws Exception{
        int firstIn, secondIn;
        while (true) {
            try {
                out.println("Position");
                out.println(position);
                out.println(cell + "'s move");
                out.println("Enter row and column");
                firstIn = in.nextInt() - 1;
                secondIn = in.nextInt() - 1;
                final Move move = new Move(firstIn, secondIn, cell);
                if (position.isValid(move)) {
                    return move;
                }
                final int row = move.getRow();
                final int column = move.getColumn();
                out.println("Move " + move + " is invalid");
            }catch(InputMismatchException e){
                in.nextLine();
                out.println("Incorrect data input, you have to enter 2 integers");
            }
        }
    }
}
