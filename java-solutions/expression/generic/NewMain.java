package expression.generic;

import expression.generic.generic_operations.GenericCalculator;
import expression.generic.generic_operations.IntegerCalculator;

public class NewMain {
    public static void main(String[] args) throws ParsingException {
        GenericParser<Integer> parser = new GenericParser<>();
        System.out.println(parser.parse("y +count(y)").evaluate(0,432,0, new IntegerCalculator()));
    }
}
