package expression;

public class Multiply extends BinaryOperation{
    public Multiply(AnyExpression expr1, AnyExpression expr2) {
        super(expr1, expr2,"*");
    }
    @Override
    protected int calculate(int left, int right) {
        return left * right;
    }
}
