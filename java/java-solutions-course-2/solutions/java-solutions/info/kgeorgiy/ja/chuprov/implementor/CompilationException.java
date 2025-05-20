package info.kgeorgiy.ja.chuprov.implementor;

/**
 * The {@code CompilationException} is a custom exception that signals issues
 * encountered during the compilation process. It is typically thrown when an error occurs
 * while compiling the generated source code.
 * <p>
 * This exception provides constructors to allow for flexible error reporting, either with
 * just an error message or with both an error message and a cause.
 * </p>
 */
public class CompilationException extends Exception {

    /**
     * Constructs a new {@code CompilationException} with {@code null} as its detail message.
     */
    public CompilationException() {
    }

    /**
     * Constructs a new {@code CompilationException} with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CompilationException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new {@code CompilationException} with the specified detail message and cause.
     *
     * @param msg the detail message.
     * @param e   the cause of this exception.
     */
    public CompilationException(String msg, Exception e) {
        super(msg, e);
    }
}
