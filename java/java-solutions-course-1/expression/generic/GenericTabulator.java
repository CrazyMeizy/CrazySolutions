package expression.generic;

import expression.generic.generic_operations.*;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class GenericTabulator implements Tabulator {
    Map<String, GenericCalculator<?>> calculateMap = new HashMap<>(Map.of(
            "i", new CheckedIntCalculator(),
            "d", new DoubleCalculator(),
            "bi", new BigIntegerCalculator(),
            "u", new UncheckedIntCalculator(),
            "b", new UncheckedByteCalculator(),
            "bool", new BoolCalculator()

    ));

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        fillResult(result, new GenericParser<>(), expression, calculateMap.get(mode), x1, x2, y1, y2, z1, z2);
        return result;
    }

    private <T> void fillResult(Object[][][] result, GenericParser<T> parser,
                                String expression, GenericCalculator<T> calculator,
                                int x1, int x2, int y1, int y2, int z1, int z2) throws ParsingException {
        AnyExpression<T> expr = parser.parse(expression);
        for (int x = 0; x <= x2 - x1; x++) {
            for (int y = 0; y <= y2 - y1; y++) {
                for (int z = 0; z <= z2 - z1; z++) {
                    try {
                        result[x][y][z] = expr.evaluate(calculator.intToType(x1 + x),
                                calculator.intToType(y1 + y), calculator.intToType(z1 + z), calculator);
                    } catch (CalculationException e) {
                        result[x][y][z] = null;
                    }
                }
            }
        }
    }
}
