package expression.generic;

import expression.generic.generic_operations.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class GenericParser<T>{
    public AnyExpression<T> parse(String expression) throws ParsingException {
        return new HelpParser<T>(new StringSource(expression)).parse();
    }

    private static class HelpParser<T> extends BaseParser<T> {
        private static final Map<String, Integer> binarySignsPriorities = new HashMap<>(Map.of(
                "max",4,
                "min",4,
                "+", 3,
                "-", 3,
                "*", 2,
                "/", 2
        ));
        private static final Map<String, String> parens = new HashMap<>(Map.of(
                "(", ")",
                "[", "]",
                "{", "}"
        ));
        private final Map<String, Boolean> parFlags = new HashMap<>(Map.of(
                ")", false,
                "]", false,
                "}", false
        ));
        private static final String[] openParens = parens.keySet().toArray(new String[0]);
        private static final String[] closeParens = parens.values().toArray(new String[0]);
        private static final int maxPriority = Collections.max(binarySignsPriorities.values());
        private final char END = getEnd();
        public HelpParser(StringSource source) {
            super(source);
        }

        public AnyExpression<T> parse() throws ParsingException{
            AnyExpression<T> expression = parseNPriority(maxPriority);

            for (String par : closeParens){
                if(parFlags.get(par)){
                    throw error("No opening parentheses for \"" + par + "\"");
                }
            }

            return expression;
        }

        private AnyExpression<T> parseNPriority(int priority) throws ParsingException{
            skipWhitespace();
            if (getClosePar()!= null) {
                throw error("No Argument in parenthesis");
            }
            AnyExpression<T> expression1 = priority != 1 ?
                    parseNPriority(priority - 1) : parseFirstPriority();
            skipWhitespace();
            if (!checkPar(false) && !test(END) && !checkSign()) {
                throw error("Sign was expected");
            }
//            while (getSimpleSignPriority() == priority) {
            while (getSignPriority() == priority) {
                String op = getSign(true);
                assert op != null;
                skipWhitespace();
                if (getClosePar()!= null || take(END)) {
                    throw error("No Argument for operation (last)");
                }
                AnyExpression<T> expression2 = priority != 1 ?
                        parseNPriority(priority - 1) : parseFirstPriority();
                expression1 = createBinaryExpression(expression1, expression2, op);
            }
            skipWhitespace();
            String par;
            if ((par = getClosePar())!= null) {
                parFlags.put(par,true);
            }
            return expression1;
        }

        private AnyExpression<T> parseFirstPriority() throws ParsingException{
            skipWhitespace();
            if (isDigit()) {
                return new Const<>(parseInt(false));
            }
            if (take('-')) {
                if (isDigit()) {
                    return new Const<>(parseInt(true));
                }
                return new UnaryMinus<>(parseFirstPriority());
            }
            if (takeUnary("count")) {
                return new Count<>(parseFirstPriority());
            }
            {
                String par;
                if ((par = getClosePar()) != null) {
                    throw error("No opening parenthesis for \"" + par + "\"");
                }
            }
            {
                String par;
                if ((par = getAndTakeOpenPar()) != null) {
                    AnyExpression<T> insideExpression = parseNPriority(maxPriority);
                    if (!take(parens.get(par))) {
                        throw error("No closing parenthesis for \"" + par + "\"");
                    }
                    parFlags.put(parens.get(par),false);
                    return insideExpression;
                }
            }
            if ((test('x') || test('y') || test('z'))) {
                return new Variable<>(Character.toString(take()));
            }
            if (checkPar(false) || getChar() == END || checkSign()) {
                throw error("No Argument for operation (first or middle)");
            } else {
                throw error("Unknown argument");
            }
        }

        private AnyExpression<T> createBinaryExpression(AnyExpression<T> expression1,
                                                     AnyExpression<T> expression2,
                                                     String op) throws ParsingException{
            return switch (op) {
                case "+" -> new Add<T>(expression1, expression2);
                case "-" -> new Subtract<T>(expression1, expression2);
                case "*" -> new Multiply<T>(expression1, expression2);
                case "/" -> new Divide<T>(expression1, expression2);
                case "min" -> new Min<T>(expression1, expression2);
                case "max" -> new Max<T>(expression1, expression2);
                default -> throw error("Unexpected priority");
            };
        }

        private int parseInt(boolean negative) throws ParsingException{
            StringBuilder sb = new StringBuilder();
            if (negative) {
                sb.append('-');
            }
            while (isDigit()) {
                sb.append(take());
            }
            try {
                return Integer.parseInt(sb.toString());
            } catch (NumberFormatException e) {
                if (negative) {
                    throw error("Integer underflow during parsing the number");
                } else {
                    throw error("Integer overflow during parsing the number");
                }
            }
        }

        private boolean checkSimpleSign() {
            return binarySignsPriorities.containsKey(Character.toString(getChar()));
        }

        private Integer getSimpleSignPriority() {
            return binarySignsPriorities.getOrDefault(Character.toString(getChar()), 0);
        }
        private boolean checkSign() {
            return !Objects.isNull(getSign(false));
        }
        private String getSign(boolean taken) {
            for(String sign : binarySignsPriorities.keySet()){
                if(test(sign)){
                    if(taken){
                        take(sign);
                    }
                    return sign;
                }
            }
            return null;
        }
        private Integer getSignPriority(){
            for(String sign : binarySignsPriorities.keySet()){
                if(test(sign)){
                    return binarySignsPriorities.get(sign);
                }
            }
            return 0;
        }

        private boolean takeUnary(String sign) throws ParsingException{
            if (take(sign)) {
                if (test(END)) {
                    throw error("No Argument for operation");
                }
                if (!Character.isWhitespace(getChar()) && !checkPar(true)) {
                    throw error("No space or parenthesis after " + sign);
                }
                return true;
            }
            return false;
        }

        private String getAndTakeOpenPar() {
            for (String par : openParens) {
                if (take(par)) {
                    return par;
                }
            }
            return null;
        }
        private String getClosePar(){
            for (String par : closeParens) {
                if (test(par)) {
                    return par;
                }
            }
            return null;
        }
        private boolean checkPar(boolean open){
            for (String par : open ? openParens : closeParens) {
                if (test(par)) {
                    return true;
                }
            }
            return false;
        }
    }
}
