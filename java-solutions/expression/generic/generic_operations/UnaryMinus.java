package expression.generic.generic_operations;


public class UnaryMinus<T> extends UnaryOperation<T> {

    public UnaryMinus(AnyExpression<T> expr) {
        super(expr,"-");
    }

    @Override
    protected T calculate(T val,GenericCalculator<T> calculator) {
        return calculator.negate(val);
    }
}
