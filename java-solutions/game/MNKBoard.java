package game;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class MNKBoard implements Board, Position {
    private static final Map<Cell, Character> SYMBOLS = Map.of(
            Cell.X, 'X',
            Cell.O, 'O',
            Cell.E, '.'
    );

    private final Cell[][] cells;
    private final PrintStream out = new PrintStream(System.out);
    private final Scanner in = new Scanner(System.in);
    private final int m;
    private final int n;
    private final int k;
    private Cell turn;
    private int moveNum;
    public MNKBoard(int m, int n, int k) {
        this.m = m;
        this.n = n;
        this.k = k;
        this.cells = new Cell[m][n];
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }
        turn = Cell.X;
    }
    public int getM(){
        return this.m;
    }
    public int getN(){
        return this.n;
    }
    public int getK(){
        return this.k;
    }
    MNKBoard copy = this;
    @Override
    public Position getPosition() {
        return copy;
    }

    @Override
    public Cell getCell() {
        return turn;
    }
    @Override
    public Cell[][] getCells() {
        return cells;
    }

    @Override
    public Result makeMove(final Move move) {
        if (!isValid(move)) {
            return Result.LOSE;
        }
        int nowM = move.getRow();
        int nowN = move.getColumn();
        cells[move.getRow()][move.getColumn()] = move.getValue();

        int inDiag1 = 1;
        int inDiag2 = 1;
        int inRow = 1;
        int inColumn = 1;
        for (int i = nowM+1; i <= Math.min(nowM+k,m-1) ; i++){
            if (cells[i][nowN] != turn){
                break;
            }
            inRow++;
        }
        for (int i = nowM-1; i >= Math.max(nowM-k,0) ; i--){
            if (cells[i][nowN] != turn){
                break;
            }
            inRow++;
        }
        for(int i = nowN + 1; i <= Math.min(nowN + k,n-1); i++){
            if(cells[nowM][i] != turn){
                break;
            }
            inColumn++;
        }
        for(int i = nowN - 1; i >= Math.max(nowN - k,0); i--){
            if(cells[nowM][i] != turn){
                break;
            }
            inColumn++;
        }
        for(int i = 1; i < Math.min(Math.min(n - nowN, m-nowM),k);i++){
            if(cells[nowM+i][nowN+i] != turn){
                break;
            }
            inDiag1++;
        }
        for(int i = 1; i < Math.min(Math.min(nowN+1,nowM+1),k);i++){
            if(cells[nowM-i][nowN-i] != turn){
                break;
            }
            inDiag1++;
        }
        for(int i = 1; i < Math.min(Math.min(n - nowN, nowM+1),k);i++){
            if(cells[nowM-i][nowN+i] != turn){
                break;
            }
            inDiag2++;
        }
        for(int i = 1; i < Math.min(Math.min(nowN+1,m-nowM),k);i++){
            if(cells[nowM+i][nowN-i] != turn){
                break;
            }
            inDiag2++;
        }
        if (inColumn >= k || inRow >= k || inDiag1>=k || inDiag2>=k){
            return Result.WIN;
        }
        if (inColumn >= 4 || inRow >= 4 || inDiag1>=4 || inDiag2>=4){
            return Result.ONEMORE;
        }

        moveNum++;
        if (moveNum == m*n){
            return Result.DRAW;
        }



        turn = turn == Cell.X ? Cell.O : Cell.X;
        return Result.UNKNOWN;
    }


    @Override
    public boolean isValid(final Move move) {
        return 0 <= move.getRow() && move.getRow() < m
                && 0 <= move.getColumn() && move.getColumn() < n
                && cells[move.getRow()][move.getColumn()] == Cell.E
                && turn == getCell();
    }

    @Override
    public Cell getCell(final int r, final int c) {
        return cells[r][c];
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(" ");
        for (int r = 1; r < n+1; r++) {
            sb.append(r);
        }
        for (int r = 1; r < m+1; r++) {
            sb.append("\n");
            sb.append(r);
            for (int c = 1; c < n+1; c++) {
                sb.append(SYMBOLS.get(cells[r-1][c-1]));
            }
        }
        sb.append("\n");
        return sb.toString();
    }
}
