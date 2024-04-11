package expression;

public class XorBitwise extends BinaryOperation{
    public XorBitwise(AnyExpression expr1, AnyExpression expr2) {
        super(expr1, expr2,"^");
    }
    @Override
    protected int calculate(int left, int right) {
        return left ^ right;
    }
}
