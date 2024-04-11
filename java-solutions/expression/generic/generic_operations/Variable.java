package expression.generic.generic_operations;

public class Variable<T> implements AnyExpression<T> {
    private final String var;

    public Variable(String var) {
        this.var = var;
    }

    @Override
    public T evaluate(T x, T y, T z,GenericCalculator<T> calculator) {
        switch (var) {
            case "x" -> {
                return x;
            }
            case "y" -> {
                return y;
            }
            case "z" -> {
                return z;
            }
            default -> throw new IllegalArgumentException("Bad argument " + var);
        }
    }

    @Override
    public String toString() {
        return var;
    }

}
