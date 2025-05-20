package expression.generic.generic_operations;

public class Max<T> extends BinaryOperation<T> {
    public Max(AnyExpression<T> expr1, AnyExpression<T> expr2) {
        super(expr1, expr2,"max");
    }
    @Override
    protected T calculate(T left, T right,GenericCalculator<T> calculator) {
        return calculator.max(left ,right);
    }
}
