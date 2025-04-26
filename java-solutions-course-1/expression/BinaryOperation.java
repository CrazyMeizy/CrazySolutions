package expression;

import java.util.List;
import java.util.Objects;

public abstract class BinaryOperation implements AnyExpression {
    protected final AnyExpression expr1;
    protected final AnyExpression expr2;
    protected final String sign;

    protected BinaryOperation(AnyExpression expr1, AnyExpression expr2, String sign) {
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.sign = sign;
    }
    abstract int calculate(int left, int right);
    @Override
    public int evaluate(int x) {
        return calculate(expr1.evaluate(x),expr2.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z){
        return calculate(expr1.evaluate(x,y,z),expr2.evaluate(x,y,z));
    }

    @Override
    public int evaluate(List <Integer> variables){
        return calculate(expr1.evaluate(variables),expr2.evaluate(variables));
    }

    @Override
    public String toString() {
        return "(" + expr1.toString() + " " + sign + " " + expr2.toString() + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(expr1, expr2, sign);
    }

    @Override
    public boolean equals(Object otherExpr) {
        return this == otherExpr || (otherExpr != null &&
                (otherExpr.getClass() == this.getClass()) &&
                Objects.equals(((BinaryOperation) otherExpr).expr1, this.expr1) &&
                Objects.equals(((BinaryOperation) otherExpr).expr2, this.expr2) &&
                Objects.equals(((BinaryOperation) otherExpr).sign, this.sign)
        );
    }
}
