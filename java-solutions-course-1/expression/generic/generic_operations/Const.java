package expression.generic.generic_operations;

public class Const<T> implements AnyExpression<T> {
    private final int c;

    public Const(int c) {
        this.c = c;
    }

    @Override
    public T evaluate(T x, T y, T z,GenericCalculator<T> calculator) {
        return calculator.intToType(c);
    }

    @Override
    public String toString() {
        return Integer.toString(c);
    }
}
