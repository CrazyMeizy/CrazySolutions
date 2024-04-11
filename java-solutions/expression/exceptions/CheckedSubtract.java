package expression.exceptions;

import expression.AnyExpression;
import expression.Subtract;

public class CheckedSubtract extends Subtract {
    public CheckedSubtract(AnyExpression expr1, AnyExpression expr2) {
        super(expr1, expr2);
    }
    @Override
    protected int calculate(int left, int right) {
        if (left>=0 && right<0 && left>Integer.MAX_VALUE+right) {
            throw new CalculationException("Integer overflow during subtraction");
        } else if (left<0 && right>0 && left<Integer.MIN_VALUE+right) {
            throw new CalculationException("Integer underflow during subtraction");
        }
        return left - right;
    }
}
