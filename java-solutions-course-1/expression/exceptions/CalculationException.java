package expression.exceptions;

public class CalculationException extends RuntimeException {
    public CalculationException(String message) {
        super(message);
    }
    public CalculationException() {
        super("Incorrect data to evaluate");
    }
}
