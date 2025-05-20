package expression.exceptions;

import expression.AnyExpression;
import expression.Pow2;
public class CheckedPow2 extends Pow2{
    public CheckedPow2(AnyExpression expr) {
        super(expr);
    }

    @Override
    protected int calculate(int val) {
        if (val>30) {
            throw new CalculationException("Integer overflow during pow2 calculation");
        }
        if(val < 0){
            throw new CalculationException("Power of 2 is less then 0");
        }
        return 1 << val;
    }
}
