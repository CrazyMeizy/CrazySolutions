package expression;

public class UnaryNotBitwise extends UnaryOperation {
    public UnaryNotBitwise(AnyExpression expr) {
        super(expr, "~");
    }

    @Override
    protected int calculate(int val) {
        return ~val;
    }
}
