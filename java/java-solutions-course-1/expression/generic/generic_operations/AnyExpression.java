package expression.generic.generic_operations;

public interface AnyExpression<T> {

    T evaluate(T x, T y, T z,GenericCalculator<T> calculator);
}
