package expression.generic.generic_operations;
public class Count<T> extends UnaryOperation<T>{
    public Count(AnyExpression<T> expr) {
        super(expr,"count");
    }

    @Override
    protected T calculate(T val,GenericCalculator<T> calculator) {
        return calculator.count(val);
    }
}
