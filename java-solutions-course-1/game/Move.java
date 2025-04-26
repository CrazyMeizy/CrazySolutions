package game;


public final class Move{
    private final int row;
    private final int column;
    private final Cell value;

    public Move(final Object row, final Object column, final Cell value) throws Exception{
        this.value = value;
        this.row = (Integer) row;
        this.column = (Integer) column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Cell getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "row=" + (row+1) + ", column=" + (column+1) + ", value=" + value;
    }
}
