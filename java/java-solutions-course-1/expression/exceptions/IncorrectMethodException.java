package expression.exceptions;
public class IncorrectMethodException extends RuntimeException {
    public IncorrectMethodException(String message) {
        super(message);
    }
    public IncorrectMethodException() {
        super("Method was called incorrectly");
    }
}

