package game;


public interface Position {

    Cell[][] getCells();

    boolean isValid(Move move);

    Cell getCell(int r, int c);
}
