package expression.exceptions;

import expression.AnyExpression;
import expression.Log2;
public class CheckedLog2 extends Log2{
    public CheckedLog2(AnyExpression expr) {
        super(expr);
    }

    @Override
    protected int calculate(int val) {
        if (val<=0) {
            throw new CalculationException("Logarithm argument is less than or equal to zero");
        }
        return log2(val);
    }
}
