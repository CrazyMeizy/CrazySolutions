package expression.generic.generic_operations;


public class Subtract<T> extends BinaryOperation<T> {
    public Subtract(AnyExpression<T> expr1, AnyExpression<T> expr2) {
        super(expr1, expr2, "-");
    }

    @Override
    protected T calculate(T left, T right,GenericCalculator<T> calculator) {
        return calculator.subtract(left,right);
    }
}
