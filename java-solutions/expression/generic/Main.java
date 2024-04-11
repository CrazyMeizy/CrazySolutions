package expression.generic;

import expression.generic.IncorrectMethodException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Not enough arguments");
        }
        Tabulator tabulator = new GenericTabulator();
        try {
            Object[][][] result = tabulator.tabulate(args[0].substring(1), args[1], -2, 2, -2, 2, -2, 2);
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j < result[i].length; j++) {
                    for (int k = 0; k < result[i][j].length; k++) {
                        System.out.printf("x = %d\ty =%d\tz = %d\t%s = %s\n", i - 2, j - 2, k - 2, args[1], result[i][j][k]);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Incorrect arguments");
        }
    }
}
