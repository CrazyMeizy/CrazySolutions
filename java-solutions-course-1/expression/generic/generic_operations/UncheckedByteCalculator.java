package expression.generic.generic_operations;

import expression.generic.CalculationException;

public class UncheckedByteCalculator implements GenericCalculator<Byte>{
    @Override
    public Byte add(Byte x, Byte y) {
        return (byte)(x + y);
    }

    @Override
    public Byte subtract(Byte x, Byte y) {
        return (byte)(x - y);
    }

    @Override
    public Byte multiply(Byte x, Byte y) {
        return (byte)(x * y);
    }

    @Override
    public Byte divide(Byte x, Byte y) {
        if(y == 0){
            throw new CalculationException(y.toString() + "divided by zero in byte mode");
        }
        return (byte)(x / y);
    }

    @Override
    public Byte negate(Byte x) {
        return (byte)-x;
    }

    @Override
    public Byte min(Byte x, Byte y) {
        return x < y ? x : y;
    }

    @Override
    public Byte max(Byte x, Byte y) {
        return x > y ? x : y;
    }

    @Override
    public Byte count(Byte x) {
        return (byte)Integer.bitCount(Byte.toUnsignedInt(x));
    }

    @Override
    public Byte intToType(int x) {
        return (byte)x;
    }
}
