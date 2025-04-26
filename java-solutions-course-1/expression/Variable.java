package expression;

import expression.exceptions.IncorrectMethodException;

import java.util.List;
import java.util.Objects;

public class Variable implements AnyExpression {
    private final String var;
    private final Integer numOfVar;

    public Variable(String var) {
        this.var = var;
        this.numOfVar = null;
    }

    public Variable(int numOfVar) {
        this.numOfVar = numOfVar;
        this.var = null;
    }

    public Variable(int numOfVar, String var) {
        this.numOfVar = numOfVar;
        this.var = var;
    }

    @Override
    public int evaluate(int x) {
        if (var == null) {
            throw new IncorrectMethodException("Method \"evaluate(List <>)\" was expected");
        }
        return x;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        if (var == null) {
            throw new IncorrectMethodException("Method \"evaluate(List <>)\" was expected");
        }
        switch (var) {
            case "x" -> {
                return x;
            }
            case "y" -> {
                return y;
            }
            case "z" -> {
                return z;
            }
            default -> throw new IllegalArgumentException("Bad argument " + var);
        }
    }

    @Override
    public int evaluate(List<Integer> variables) {
        if (numOfVar == null) {
            throw new IncorrectMethodException("Method \"evaluate(List <>)\" was not expected");
        }
        return variables.get(numOfVar);
    }

    @Override
    public String toString() {
        return var;
    }

    @Override
    public boolean equals(Object otherExpr) {
        return this == otherExpr || ((otherExpr instanceof Variable) &&
                Objects.equals(((Variable) otherExpr).var, this.var) &&
                Objects.equals(((Variable) otherExpr).numOfVar, this.numOfVar));
    }

    @Override
    public int hashCode() {
        return Objects.hash(var, numOfVar);
    }

}
