package expression.generic.generic_operations;

import expression.generic.CalculationException;

public class UncheckedIntCalculator extends AbstractIntCalculator {

    @Override
    public Integer add(Integer x, Integer y) {
        return x + y;
    }

    @Override
    public Integer subtract(Integer x, Integer y) {
        return x - y;
    }

    @Override
    public Integer multiply(Integer x, Integer y) {
        return x * y;
    }

    @Override
    public Integer divide(Integer x, Integer y) {
        if(divideCheck(x,y) == 10){
            throw new CalculationException("Division by zero");
        }
        return x / y;
    }

    @Override
    public Integer negate(Integer x) {
        return -x;
    }
}
