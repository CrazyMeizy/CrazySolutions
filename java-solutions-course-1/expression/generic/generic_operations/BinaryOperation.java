package expression.generic.generic_operations;

import java.util.Objects;

public abstract class BinaryOperation<T> implements AnyExpression<T> {
    protected final AnyExpression<T> expr1;
    protected final AnyExpression<T> expr2;
    protected final String sign;

    protected BinaryOperation(AnyExpression<T> expr1, AnyExpression<T> expr2, String sign) {
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.sign = sign;
    }
    abstract T calculate(T left, T right,GenericCalculator<T> calculator);

    public T evaluate(T x, T y, T z,GenericCalculator<T> calculator){
        return calculate(expr1.evaluate(x,y,z,calculator),expr2.evaluate(x,y,z,calculator),calculator);
    }

    @Override
    public String toString() {
        return "(" + expr1.toString() + " " + sign + " " + expr2.toString() + ")";
    }
}
