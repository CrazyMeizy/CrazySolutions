package expression.generic.generic_operations;

public interface GenericCalculator<T> {
    T add(T x, T y);
    T subtract(T x, T y);
    T multiply(T x, T y);
    T divide(T x, T y);
    T negate(T x);
    T min(T x, T y);
    T max(T x, T y);
    T count(T x);
    T intToType(int x);
}
