package expression.exceptions;

import expression.AnyExpression;
import expression.UnaryMinus;

public class CheckedNegate extends UnaryMinus {
    public CheckedNegate(AnyExpression expr) {
        super(expr);
    }

    @Override
    protected int calculate(int val) {
        if (val == Integer.MIN_VALUE) {
            throw new CalculationException("Integer overflow during negation of Integer.MIN_VALUE");
        }
        return val*(-1);
    }
}
