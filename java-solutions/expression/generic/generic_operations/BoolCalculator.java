package expression.generic.generic_operations;

import expression.generic.CalculationException;

public class BoolCalculator implements GenericCalculator<Boolean> {

    @Override
    public Boolean add(Boolean x, Boolean y) {
        return x | y;
    }

    @Override
    public Boolean subtract(Boolean x, Boolean y) {
        return x ^ y;
    }

    @Override
    public Boolean multiply(Boolean x, Boolean y) {
        return x & y;
    }

    @Override
    public Boolean divide(Boolean x, Boolean y) {
        if(!y){
            throw new CalculationException(y.toString() + " divided by zero");
        }
        return x;
    }

    @Override
    public Boolean negate(Boolean x) {
        return x;
    }

    @Override
    public Boolean min(Boolean x, Boolean y) {
        return x & y;
    }

    @Override
    public Boolean max(Boolean x, Boolean y) {
        return x | y;
    }

    @Override
    public Boolean count(Boolean x) {
        return x;
    }

    @Override
    public Boolean intToType(int x) {
        return x != 0;
    }
}
