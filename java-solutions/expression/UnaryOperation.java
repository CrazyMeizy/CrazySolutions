package expression;

import java.util.List;
import java.util.Objects;

public abstract class UnaryOperation implements AnyExpression {
    protected final AnyExpression expr;
    protected final String sign;

    protected UnaryOperation(AnyExpression op, String sign) {
        this.expr = op;
        this.sign = sign;
    }

    protected abstract int calculate(int val);

    @Override
    public int evaluate(int x) {
        return calculate(expr.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return calculate(expr.evaluate(x, y, z));
    }

    @Override
    public int evaluate(List<Integer> variables) {
        return calculate(expr.evaluate(variables));
    }

    @Override
    public String toString() {
        return sign + "(" + expr.toString() + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(expr, sign);
    }

    @Override
    public boolean equals(Object otherExpr) {
        return this == otherExpr || (otherExpr != null &&
                (otherExpr.getClass() == this.getClass()) &&
                Objects.equals(((UnaryOperation) otherExpr).expr, this.expr) &&
                Objects.equals(((UnaryOperation) otherExpr).sign, this.sign)
        );
    }
}
