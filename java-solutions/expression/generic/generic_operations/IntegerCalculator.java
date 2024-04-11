package expression.generic.generic_operations;


import expression.generic.CalculationException;

public class IntegerCalculator implements GenericCalculator<Integer> {

    @Override
    public Integer add(Integer x, Integer y) {
        if (y > 0 && x > 0 && y > Integer.MAX_VALUE - x) {
            throw new CalculationException("Integer overflow during addition");
        } else if (y < 0 && x < 0 && y < Integer.MIN_VALUE - x) {
            throw new CalculationException("Integer underflow during addition");
        }
        return y + x;
    }

    @Override
    public Integer subtract(Integer x, Integer y) {
        if (x>=0 && y<0 && x>Integer.MAX_VALUE+y) {
            throw new CalculationException("Integer overflow during subtraction");
        } else if (x<0 && y>0 && x<Integer.MIN_VALUE+y) {
            throw new CalculationException("Integer underflow during subtraction");
        }
        return x - y;
    }

    @Override
    public Integer multiply(Integer x, Integer y) {
        if (x<0 && y>0 && (x<Integer.MIN_VALUE / y)) {
            throw new CalculationException("Integer underflow during multiplication");
        } else if (x>0 && y<0 && (y<Integer.MIN_VALUE/x)) {
            throw new CalculationException("Integer underflow during multiplication");
        } else if (x > 0 && y > 0 && (x > Integer.MAX_VALUE/y)){
            throw new CalculationException("Integer overflow during multiplication");
        } else if (x < 0 && y < 0 && (x < Integer.MAX_VALUE/y)) {
            throw new CalculationException("Integer overflow during multiplication");
        }
        return x * y;
    }

    @Override
    public Integer divide(Integer x, Integer y) {
        if (y == 0) {
            throw new CalculationException(y.toString() + "divided by zero in int mode");
        } else if (x == Integer.MIN_VALUE && y == -1){
            throw new CalculationException("Integer overflow during division");
        }
        return x / y;
    }

    @Override
    public Integer negate(Integer x) {
        if (x == Integer.MIN_VALUE) {
            throw new CalculationException("Integer overflow during negation of Integer.MIN_VALUE");
        }
        return -x;
    }

    @Override
    public Integer min(Integer x, Integer y) {
        return Integer.min(x,y);
    }

    @Override
    public Integer max(Integer x, Integer y) {
        return Integer.max(x,y);
    }

    @Override
    public Integer count(Integer x) {
        return Integer.bitCount(x);
    }

    @Override
    public Integer intToType(int x) {
        return x;
    }
}
