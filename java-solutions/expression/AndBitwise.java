package expression;

public class AndBitwise extends BinaryOperation{
    public AndBitwise(AnyExpression expr1, AnyExpression expr2) {
        super(expr1, expr2,"&");
    }
    @Override
    protected int calculate(int left, int right) {
        return left & right;
    }
}
