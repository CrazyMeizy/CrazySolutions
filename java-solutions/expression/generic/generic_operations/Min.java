package expression.generic.generic_operations;

public class Min<T> extends BinaryOperation<T> {
    public Min(AnyExpression<T> expr1, AnyExpression<T> expr2) {
        super(expr1, expr2,"min");
    }
    @Override
    protected T calculate(T left, T right,GenericCalculator<T> calculator) {
        return calculator.min(left ,right);
    }
}
