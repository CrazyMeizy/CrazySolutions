package expression.exceptions;

public class Main {
    public static void main(String[] args){
        System.out.printf("%s\t%s%n", "x", "f");
        for(int x=0;x<10;x+=1) {
            try {
                System.out.printf("%d\t%d%n", x, new ExpressionParser().parse("1000000*x*x*x*x*x/(x-1)").evaluate(x,0,0));
            } catch (ParsingException | CalculationException e) {
                System.out.printf("%d\t%s%n", x, e.getMessage());
            }
        }
    }
}
