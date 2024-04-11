package expression.generic.generic_operations;

import expression.generic.CalculationException;

public class CheckedIntCalculator extends AbstractIntCalculator {

    @Override
    public Integer add(Integer x, Integer y) {
        int flag = addCheck(x, y);
        if (flag == 1) {
            throw new CalculationException("Integer overflow during addition");
        } else if (flag == -1) {
            throw new CalculationException("Integer underflow during addition");
        }
        return y + x;
    }

    @Override
    public Integer subtract(Integer x, Integer y) {
        int flag = subtractCheck(x, y);
        if (flag == 1) {
            throw new CalculationException("Integer overflow during subtraction");
        } else if (flag == -1) {
            throw new CalculationException("Integer underflow during subtraction");
        }
        return x - y;
    }

    @Override
    public Integer multiply(Integer x, Integer y) {
        int flag = multiplyCheck(x, y);
        if (flag == 1) {
            throw new CalculationException("Integer overflow during multiplication");
        } else if (flag == -1) {
            throw new CalculationException("Integer underflow during multiplication");
        }
        return x * y;
    }

    @Override
    public Integer divide(Integer x, Integer y) {
        int flag = divideCheck(x, y);
        if (flag == 10) {
            throw new CalculationException(y.toString() + "divided by zero in int mode");
        } else if (flag == 1) {
            throw new CalculationException("Integer overflow during division");
        }
        return x / y;
    }

    @Override
    public Integer negate(Integer x) {
        int flag = negateCheck(x);
        if (flag == 1) {
            throw new CalculationException("Integer overflow during negation of Integer.MIN_VALUE");
        }
        return -x;
    }
}
