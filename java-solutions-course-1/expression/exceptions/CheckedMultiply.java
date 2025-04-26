package expression.exceptions;

import expression.AnyExpression;
import expression.Multiply;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(AnyExpression expr1, AnyExpression expr2) {
        super(expr1, expr2);
    }
    @Override
    protected int calculate(int left, int right) {
        if (left<0 && right>0 && (left<Integer.MIN_VALUE / right)) {
            throw new CalculationException("Integer underflow during multiplication");
        } else if (left>0 && right<0 && (right<Integer.MIN_VALUE/left)) {
            throw new CalculationException("Integer underflow during multiplication");
        } else if (left > 0 && right > 0 && (left > Integer.MAX_VALUE/right)){
            throw new CalculationException("Integer overflow during multiplication");
        } else if (left < 0 && right < 0 && (left < Integer.MAX_VALUE/right)) {
            throw new CalculationException("Integer overflow during multiplication");
        }
        return left * right;
    }
}
