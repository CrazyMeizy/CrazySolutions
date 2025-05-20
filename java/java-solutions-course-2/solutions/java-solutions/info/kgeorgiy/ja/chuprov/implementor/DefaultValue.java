package info.kgeorgiy.ja.chuprov.implementor;

/**
 * The {@code DefaultValue} enum represents default values for Java's primitive types and reference types.
 * <p>
 * Each constant in the enum maps a specific Java type to its default value represented as a string.
 * This is useful for generating method implementations that need to return a default value.
 * </p>
 * <p>
 * The following types and their default values are defined:
 * </p>
 * <ul>
 *     <li>{@code void} - represented by an empty string</li>
 *     <li>{@code boolean} - represented by "false"</li>
 *     <li>{@code char} - represented by "'\\u0000'" (the null character)</li>
 *     <li>{@code byte} - represented by "0"</li>
 *     <li>{@code short} - represented by "0"</li>
 *     <li>{@code int} - represented by "0"</li>
 *     <li>{@code long} - represented by "0L"</li>
 *     <li>{@code float} - represented by "0.0f"</li>
 *     <li>{@code double} - represented by "0.0d"</li>
 *     <li>Any reference type - represented by "null"</li>
 * </ul>
 */
public enum DefaultValue {
    /**
     * Default value for {@code void} type.
     */
    VOID(void.class, ""),
    /**
     * Default value for {@code boolean} type.
     */
    BOOLEAN(boolean.class, "false"),
    /**
     * Default value for {@code char} type.
     */
    CHAR(char.class, "'\\u0000'"),
    /**
     * Default value for {@code byte} type.
     */
    BYTE(byte.class, "0"),
    /**
     * Default value for {@code short} type.
     */
    SHORT(short.class, "0"),
    /**
     * Default value for {@code int} type.
     */
    INT(int.class, "0"),
    /**
     * Default value for {@code long} type.
     */
    LONG(long.class, "0L"),
    /**
     * Default value for {@code float} type.
     */
    FLOAT(float.class, "0.0f"),
    /**
     * Default value for {@code double} type.
     */
    DOUBLE(double.class, "0.0d"),
    /**
     * Default value for reference types.
     */
    REFERENCE(Object.class, "null");

    /**
     * The Java type associated with the default value.
     */
    private final Class<?> type;

    /**
     * The default value represented as a string.
     */
    private final String value;

    /**
     * Constructs a {@code DefaultValue} enum constant with the specified type and its default value.
     *
     * @param type  the Java type for which the default value is defined
     * @param value the default value represented as a string
     */
    DefaultValue(Class<?> type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Retrieves the default value for the specified type.
     * <p>
     * The method iterates over the enum constants and returns the associated default value string if the
     * constant's type is assignable from the given {@code token}. If no matching default value is found,
     * an {@link IllegalArgumentException} is thrown.
     * </p>
     *
     * @param token the class object representing the type for which a default value is required
     * @return a string representing the default value for the given type
     * @throws IllegalArgumentException if no default value is found for the specified type
     */
    public static String forType(Class<?> token) {
        for (DefaultValue def : values()) {
            if (def.type.isAssignableFrom(token)) {
                return def.value;
            }
        }
        throw new IllegalArgumentException("No default value found for " + token);
    }

    /**
     * Returns the default value as a string.
     *
     * @return the default value represented as a string
     */
    @Override
    public String toString() {
        return value;
    }
}
