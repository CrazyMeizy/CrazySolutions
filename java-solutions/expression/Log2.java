package expression;

import expression.exceptions.CalculationException;

public class Log2 extends UnaryOperation{
    public Log2(AnyExpression expr) {
        super(expr,"log2");
    }

    @Override
    protected int calculate(int val) {
        return log2(val);
    }

    protected int log2(int x){
        int ans = -1;
        int v = 1;
        while(v <= x){
            if (v <= Integer.MAX_VALUE/2) {
                v *= 2;
            }else{
                ans++;
                break;
            }
            ans++;
        }
        return ans;
    }
}
