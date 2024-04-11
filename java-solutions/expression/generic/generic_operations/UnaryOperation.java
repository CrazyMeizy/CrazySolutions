package expression.generic.generic_operations;

import java.util.List;
import java.util.Objects;

public abstract class UnaryOperation<T> implements AnyExpression<T> {
    protected final AnyExpression<T> expr;
    protected final String sign;

    protected UnaryOperation(AnyExpression<T> op, String sign) {
        this.expr = op;
        this.sign = sign;
    }

    protected abstract T calculate(T val,GenericCalculator<T> calculator);


    @Override
    public T evaluate(T x, T y, T z,GenericCalculator<T> calculator) {
        return calculate(expr.evaluate(x, y, z,calculator),calculator);
    }


    @Override
    public String toString() {
        return sign + "(" + expr.toString() + ")";
    }
}
