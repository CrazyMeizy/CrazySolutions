package expression.generic;

public class ParsingException extends Exception{
    public ParsingException(String message){
        super(message);
    }
    public ParsingException(String message, Throwable cause){
        super(message,cause);
    }
    public ParsingException() {
        super("Incorrect expression structure");
    }
}
