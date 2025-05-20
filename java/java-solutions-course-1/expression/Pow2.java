package expression;

public class Pow2 extends UnaryOperation{
    public Pow2(AnyExpression expr) {
        super(expr,"pow2");
    }

    @Override
    protected int calculate(int val) {
        return 1 << val;
    }
}
