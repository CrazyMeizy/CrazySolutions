package expression;

import java.util.List;
import java.util.Objects;

public class Const implements AnyExpression {
    private final int c;

    public Const(int c) {
        this.c = c;
    }

    @Override
    public int evaluate(int x) {
        return c;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return c;
    }

    @Override
    public int evaluate(List<Integer> variables) {
        return c;
    }

    @Override
    public String toString() {
        return Integer.toString(c);
    }

    @Override
    public boolean equals(Object otherExpr) {
        return this == otherExpr || ((otherExpr instanceof Const) && ((Const) otherExpr).c == this.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(c);
    }
}
