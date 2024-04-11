package expression.generic;

public class UnknownModificationException extends Exception{
    public UnknownModificationException(String message) {
        super(message);
    }
    public UnknownModificationException() {
        super("Unknown modification to evaluate");
    }
}
