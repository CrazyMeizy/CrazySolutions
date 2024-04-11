package expression.exceptions;

import expression.AnyExpression;
import expression.Divide;

public class CheckedDivide extends Divide {
    public CheckedDivide(AnyExpression expr1, AnyExpression expr2) {
        super(expr1, expr2);
    }

    @Override
    protected int calculate(int left, int right) {
        if (right == 0) {
            throw new CalculationException("Division by zero");
        } else if (left == Integer.MIN_VALUE && right == -1){
            throw new CalculationException("Integer overflow during division");
        }
        return left / right;
    }
}
