package expression.generic.generic_operations;


public class Divide<T> extends BinaryOperation<T> {
    public Divide(AnyExpression<T> expr1, AnyExpression<T> expr2) {
        super(expr1, expr2, "/");
    }


    @Override
    protected T calculate(T left, T right, GenericCalculator<T> calculator) {
        return calculator.divide(left, right);
    }
}
