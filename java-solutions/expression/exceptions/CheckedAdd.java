package expression.exceptions;

import expression.*;

public class CheckedAdd extends Add {
    public CheckedAdd(AnyExpression expr1, AnyExpression expr2) {
        super(expr1, expr2);
    }

    @Override
    protected int calculate(int right, int left) {
        if (right > 0 && left > 0 && right > Integer.MAX_VALUE - left) {
            throw new CalculationException("Integer overflow during addition");
        } else if (right < 0 && left < 0 && right < Integer.MIN_VALUE - left) {
            throw new CalculationException("Integer underflow during addition");
        }
        return right + left;
    }
}
