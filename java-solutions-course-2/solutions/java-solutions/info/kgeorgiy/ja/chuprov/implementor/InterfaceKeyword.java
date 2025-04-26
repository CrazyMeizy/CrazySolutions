package info.kgeorgiy.ja.chuprov.implementor;

/**
 * The {@code InterfaceKeyword} enum defines a set of common Java keywords that are used in source code generation.
 * <p>
 * Each constant in this enum encapsulates the string representation of a Java language keyword. These keywords
 * are typically used when programmatically generating Java source files, such as when implementing interfaces or classes.
 * </p>
 * <p>
 * The keywords defined in this enum include:
 * </p>
 * <ul>
 *   <li>{@link #PACKAGE} - Represents the "package" keyword used to declare a package.</li>
 *   <li>{@link #PUBLIC} - Represents the "public" access modifier.</li>
 *   <li>{@link #CLASS} - Represents the "class" keyword used to declare a class.</li>
 *   <li>{@link #IMPLEMENTS} - Represents the "implements" keyword used to indicate interface implementation.</li>
 *   <li>{@link #RETURN} - Represents the "return" keyword used to return a value from a method.</li>
 * </ul>
 *
 * @see String
 */
public enum InterfaceKeyword {
    /**
     * Represents the {@code package} keyword used to declare a package.
     */
    PACKAGE("package"),

    /**
     * Represents the {@code public} keyword indicating public access.
     */
    PUBLIC("public"),

    /**
     * Represents the {@code class} keyword used to declare a class.
     */
    CLASS("class"),

    /**
     * Represents the {@code implements} keyword used to specify interface implementation.
     */
    IMPLEMENTS("implements"),

    /**
     * Represents the {@code return} keyword used to return a value from a method.
     */
    RETURN("return");

    /**
     * The string representation of the Java keyword.
     */
    private final String keyword;

    /**
     * Constructs an {@code InterfaceKeyword} enum constant with the specified keyword.
     *
     * @param keyword the string representation of the Java keyword
     */
    InterfaceKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Returns the string representation of the keyword.
     *
     * @return the Java keyword as a string
     */
    @Override
    public String toString() {
        return keyword;
    }
}
