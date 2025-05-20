package expression.generic.generic_operations;

public class Multiply<T> extends BinaryOperation<T> {
    public Multiply(AnyExpression<T> expr1, AnyExpression<T> expr2) {
        super(expr1, expr2,"*");
    }
    @Override
    protected T calculate(T left, T right,GenericCalculator<T> calculator) {
        return calculator.multiply(left ,right);
    }
}
