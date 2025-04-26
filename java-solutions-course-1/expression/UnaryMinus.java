package expression;

import java.util.Objects;

public class UnaryMinus extends  UnaryOperation{

    public UnaryMinus(AnyExpression expr) {
        super(expr,"-");
    }

    @Override
    protected int calculate(int val) {
        return -val;
    }
}
