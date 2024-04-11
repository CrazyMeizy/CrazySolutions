package expression.generic.generic_operations;

import expression.generic.CalculationException;

import java.math.BigInteger;

public class BigIntegerCalculator implements GenericCalculator<BigInteger> {
    @Override
    public BigInteger add(BigInteger x, BigInteger y) {
        return x.add(y);
    }

    @Override
    public BigInteger subtract(BigInteger x, BigInteger y) {
        return x.subtract(y);
    }

    @Override
    public BigInteger multiply(BigInteger x, BigInteger y) {
        return x.multiply(y);
    }

    @Override
    public BigInteger divide(BigInteger x, BigInteger y) {
        if (y.equals(BigInteger.ZERO)) {
            throw new CalculationException(x.toString() + " divided by zero in unchecked int  mode");
        }
        return x.divide(y);
    }

    @Override
    public BigInteger negate(BigInteger x) {
        return x.negate();
    }

    @Override
    public BigInteger min(BigInteger x, BigInteger y) {
        return x.compareTo(y) < 0 ? x : y;
    }

    @Override
    public BigInteger max(BigInteger x, BigInteger y) {
        return x.compareTo(y) > 0 ? x : y;
    }

    @Override
    public BigInteger count(BigInteger x) {
        return BigInteger.valueOf(x.bitCount());
    }

    @Override
    public BigInteger intToType(int x) {
        return BigInteger.valueOf(x);
    }
}
