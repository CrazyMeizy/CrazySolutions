package expression.exceptions;

import expression.*;

import java.util.*;

public class ExpressionParser implements TripleParser, ListParser {
    @Override
    public TripleExpression parse(String expression) {
        return new HelpParser(new StringSource(expression)).parse();
    }

    @Override
    public ListExpression parse(String expression, List<String> variables) throws Exception {
        return new HelpParser(new StringSource(expression), variables).parse();
    }

    private static class HelpParser extends BaseParser {
        private static final Map<String, Integer> binarySignsPriorities = new HashMap<>(Map.of(
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
        private final List<String> variables;

        public HelpParser(StringSource source) {
            super(source);
            this.variables = null;
        }

        public HelpParser(StringSource source, List<String> variables) {
            super(source);
            this.variables = variables;
        }

        public AnyExpression parse() {
            AnyExpression expression = parseNPriority(maxPriority);

            for (String par : closeParens){
                if(parFlags.get(par)){
                    throw new ParsingException("No opening parentheses for \"" + par + "\"");
                }
            }

            return expression;
        }

        private AnyExpression parseNPriority(int priority) {
            skipWhitespace();
            if (getClosePar()!= null) {
                throw new ParsingException("No Argument in parenthesis");
            }
            AnyExpression expression1 = priority != 1 ?
                    parseNPriority(priority - 1) : parseFirstPriority();
            skipWhitespace();
            if (!checkPar(false) && !test('\0') && !checkSimpleSign()) {
                throw new ParsingException("Sign was expected");
            }
            while (getSimpleSignPriority() == priority) {
                String op = Character.toString(take());
                skipWhitespace();
                if (getClosePar()!= null || take('\0')) {
                    throw new ParsingException("No Argument for operation (last)");
                }
                AnyExpression expression2 = priority != 1 ?
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

        private AnyExpression parseFirstPriority() {
            skipWhitespace();
            if (isDigit()) {
                return new Const(parseInt(false));
            }
            if (take('-')) {
                if (isDigit()) {
                    return new Const(parseInt(true));
                }
                return new CheckedNegate(parseFirstPriority());
            }
            if (takeUnary("log2")) {
                return new CheckedLog2(parseFirstPriority());
            }
            if (takeUnary("pow2")) {
                return new CheckedPow2(parseFirstPriority());
            }
            if (take('~')) {
                return new UnaryNotBitwise(parseFirstPriority());
            }
            {
                String par;
                if ((par = getClosePar()) != null) {
                    throw new ParsingException("No opening parenthesis for \"" + par + "\"");
                }
            }
            {
                String par;
                if ((par = getAndTakeOpenPar()) != null) {
                    AnyExpression insideExpression = parseNPriority(maxPriority);
                    if (!take(parens.get(par))) {
                        throw new ParsingException("No closing parenthesis for \"" + par + "\"");
                    }
                    parFlags.put(parens.get(par),false);
                    return insideExpression;
                }
            }
            {
                int numOfVar;
                if (variables != null && (numOfVar = getNumOfVar()) >= 0) {
                    return new Variable(numOfVar, variables.get(numOfVar));
                }
            }
            if (variables == null && (test('x') || test('y') || test('z'))) {
                return new Variable(Character.toString(take()));
            }
            if (checkPar(false) || getChar() == '\0' || checkSimpleSign()) {
                throw new ParsingException("No Argument for operation (first or middle)");
            } else {
                throw new ParsingException("Unknown argument");
            }
        }

        private AnyExpression createBinaryExpression(AnyExpression expression1, AnyExpression expression2, String op) {
            return switch (op) {
                case "+" -> new CheckedAdd(expression1, expression2);
                case "-" -> new CheckedSubtract(expression1, expression2);
                case "*" -> new CheckedMultiply(expression1, expression2);
                case "/" -> new CheckedDivide(expression1, expression2);
                default -> throw new ParsingException("Unexpected priority");
            };
        }

        private int parseInt(boolean negative) {
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
                    throw new ParsingException("Integer underflow during parsing the number", e);
                } else {
                    throw new ParsingException("Integer overflow during parsing the number", e);
                }
            }
        }

        private boolean checkSimpleSign() {
            return binarySignsPriorities.containsKey(Character.toString(getChar()));
        }

        private Integer getSimpleSignPriority() {
            return binarySignsPriorities.getOrDefault(Character.toString(getChar()), 0);
        }

        private boolean takeUnary(String sign) {
            if (take(sign)) {
                if (test('\0')) {
                    throw new ParsingException("No Argument for operation");
                }
                if (!Character.isWhitespace(getChar()) && !checkPar(true)) {
                    throw new ParsingException("No space or parenthesis after " + sign);
                }
                return true;
            }
            return false;
        }

        private int getNumOfVar() {
            for (String var : variables) {
                if (take(var)) {
                    return variables.indexOf(var);
                }
            }
            return -1;
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
