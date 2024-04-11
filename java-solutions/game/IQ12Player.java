package game;

public class IQ12Player implements Player{

    @Override
    public Move move(Position position, Cell cell) throws Exception{
        MNKBoard board = (MNKBoard) position;
        int m = board.getM();
        int n = board.getN();
        String s = "zelenyi ilya";
        final Move move = new Move(s, "World", cell);
        return move;
    }

}
