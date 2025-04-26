package expression.generic.generic_operations;

import expression.generic.CalculationException;

public abstract class AbstractIntCalculator implements GenericCalculator<Integer>{
    protected Integer addCheck(Integer x, Integer y) {
        if (y > 0 && x > 0 && y > Integer.MAX_VALUE - x) {
            return 1;
        } else if (y < 0 && x < 0 && y < Integer.MIN_VALUE - x) {
            return -1;
        }
        return 0;
    }

    protected Integer subtractCheck(Integer x, Integer y) {
        if (x>=0 && y<0 && x>Integer.MAX_VALUE+y) {
           return 1;
        } else if (x<0 && y>0 && x<Integer.MIN_VALUE+y) {
            return -1;
        }
        return 0;
    }

    protected Integer multiplyCheck(Integer x, Integer y) {
        if (x<0 && y>0 && (x<Integer.MIN_VALUE / y)) {
            return -1;
        } else if (x>0 && y<0 && (y<Integer.MIN_VALUE/x)) {
            return -1;
        } else if (x > 0 && y > 0 && (x > Integer.MAX_VALUE/y)){
            return 1;
        } else if (x < 0 && y < 0 && (x < Integer.MAX_VALUE/y)) {
            return 1;
        }
        return 0;
    }

    protected Integer divideCheck(Integer x, Integer y) {
        if (y == 0) {
            return 10;
        } else if (x == Integer.MIN_VALUE && y == -1){
            return 1;
        }
        return 0;
    }

    protected Integer negateCheck(Integer x) {
        if (x == Integer.MIN_VALUE) {
            return 1;
        }
        return 0;
    }

    abstract public Integer add(Integer x, Integer y);

    abstract public Integer subtract(Integer x, Integer y);

    abstract public Integer multiply(Integer x, Integer y);

    abstract public Integer divide(Integer x, Integer y);

    abstract public Integer negate(Integer x);

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
