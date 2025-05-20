package info.kgeorgiy.ja.chuprov.implementor;

import info.kgeorgiy.java.advanced.implementor.*;
import info.kgeorgiy.java.advanced.implementor.tools.JarImpler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Stream;

import static info.kgeorgiy.ja.chuprov.implementor.InterfaceKeyword.*;

/**
 * The {@code Implementor} class implements the {@link JarImpler} interface, providing functionality to automatically
 * generate an implementation of a given interface. The generated implementation appends the suffix "Impl" to the
 * original interface's name and provides default implementations for all its methods.
 * <p>
 * The class supports:
 * <ul>
 *   <li>Generating a Java source file for the interface with default implementations.</li>
 *   <li>Compiling the generated source file and packaging the compiled class file into a JAR file.</li>
 * </ul>
 * <p>
 * <b>Usage:</b>
 * <pre>
 *   java info.kgeorgiy.ja.chuprov.implementor.Implementor &lt;fully-qualified-interface-name&gt;
 *   java info.kgeorgiy.ja.chuprov.implementor.Implementor -jar &lt;fully-qualified-interface-name&gt; &lt;jar-file-name&gt;
 * </pre>
 * <p>
 * The interface to be implemented must be public (and non-private) as the implementor verifies these conditions.
 *
 * @see JarImpler
 * @author <a href="https://www.youtube.com/watch?v=xvFZjo5PgG0">CrazyMeizy</a>
 */
public class Implementor implements JarImpler {
    /**
     * The default indentation offset used when printing class content.
     */
    private static final int DEFAULT_OFFSET = 0;

    /**
     * The additional indentation level (tab offset) used for nested elements.
     */
    private static final int TAB_OFFSET = 1;

    /**
     * An empty {@link Path} used as a default root path for file operations.
     */
    private static final Path EMPTY_PATH = Path.of("");

    /**
     * The suffix appended to the name of the generated implementation class.
     */
    private static final String NAME_SUFFIX = "Impl";

    /**
     * The file extension for generated Java source files.
     */
    private static final String JAVA_EXTENSION = ".java";

    /**
     * The file extension for compiled class files.
     */
    private static final String CLASS_EXTENSION = ".class";

    /**
     * Constructs a new instance of {@code Implementor}.
     * <p>
     * This default constructor is explicitly defined to provide proper documentation.
     * </p>
     */
    public Implementor() {
    }

    /**
     * The entry point of the application. Expects a single command-line argument representing the fully-qualified name
     * of the interface to implement.
     *
     * @param args Command-line arguments; must contain exactly one argument.
     */
    public static void main(String[] args) {
        try {
            if (args.length == 1) {
                new Implementor().implement(Class.forName(args[0]), EMPTY_PATH);
            }else if (args.length == 3 && args[0].equals("-jar")) {
                new Implementor().implement(Class.forName(args[1]), Path.of(args[2]));
            }else{
                System.err.println("Usage:");
                System.err.println("  To generate a .java file: <FullyQualifiedClassName>");
                System.err.println("  To generate a .jar file: -jar <FullyQualifiedClassName> <OutputJarFile>");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("No class found: " + e.getMessage());
        } catch (ImplerException e) {
            System.err.println("Cannot implement interface: " + e.getCause().getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        if (!token.isInterface()) {
            throw new ImplerException(token.getName() + " is not an interface");
        }
        if (Modifier.isPrivate(token.getModifiers())) {
            throw new ImplerException(token.getName() + " is private");
        }

        Path fileName = getClassName(token, root, JAVA_EXTENSION, File.separatorChar);
        try {
            Files.createDirectories(fileName.getParent());
            Files.createFile(fileName);
        } catch (IOException e) {
            throw new ImplerException("Can not create implemented file", e);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(fileName, StandardCharsets.UTF_8)) {
            printClass(writer, token, DEFAULT_OFFSET);
        } catch (IOException e) {
            throw new ImplerException("Error writing to file: " + fileName, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void implementJar(Class<?> token, Path jarFile) throws ImplerException {
        try {
            Path dirToPack = Files.createTempDirectory(jarFile.getParent(), "dirToPack");
            try {
                implement(token, dirToPack);
                compile(token, dirToPack);
                createJar(token, jarFile, dirToPack);
            }catch (CompilationException e) {
                throw new ImplerException("Cannot compile temp file: " + e.getMessage(), e);
            }finally {
                deleteDir(dirToPack);
            }
        } catch (IOException e) {
            throw new ImplerException("Cannot create temp directory: " + e.getMessage(), e);
        }

    }

    /**
     * Recursively deletes the specified directory and all its contents.
     *
     * @param directory the directory to delete
     * @throws ImplerException if an I/O error occurs during deletion
     */
    private void deleteDir(Path directory) throws ImplerException {
        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new ImplerException("Cannot delete temp directory: " + e.getMessage(), e);
        }
    }

    /**
     * Compiles the generated Java source file.
     *
     * @param token     the interface to implement
     * @param dirToPack the temporary directory containing the generated source file
     * @throws CompilationException if the Java compiler is not available or if compilation fails
     */
    private void compile(Class<?> token, Path dirToPack) throws CompilationException {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new CompilationException("Could not find java compiler, include tools.jar to classpath");
        }
        Path classpath;
        try {
            classpath = Path.of(token.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new CompilationException("Could not find interface code source location: " + e.getMessage(), e);
        }
        String[] args = Stream.concat(
                Stream.of("-cp", classpath.toString(), "-d", dirToPack.toString(), "-encoding", StandardCharsets.UTF_8.name()),
                Stream.of(getClassName(token, dirToPack, JAVA_EXTENSION, File.separatorChar).toString())
        ).toArray(String[]::new);
        int exitCode = compiler.run(null, null, null, args);
        if (exitCode != 0) {
            throw new CompilationException("Compilation failed");
        }
    }

    /**
     * Creates a JAR file containing the compiled class file.
     *
     * @param token     the interface being implemented
     * @param jarFile   the destination path for the JAR file
     * @param dirToPack the directory containing the compiled class file
     * @throws ImplerException if an I/O error occurs during JAR file creation
     */
    private void createJar(Class<?> token, Path jarFile, Path dirToPack) throws ImplerException {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        try (JarOutputStream jarOut = new JarOutputStream(Files.newOutputStream(jarFile), manifest)) {
            jarOut.putNextEntry(new JarEntry(getClassNameForJar(token)));
            Files.copy(getClassName(token, dirToPack, CLASS_EXTENSION, '/'), jarOut);
        } catch (IOException e) {
            throw new ImplerException("Cannot create temp directory: " + e.getMessage(), e);
        }
    }

    /**
     * Constructs the file path for the generated file based on the interface's package and name.
     *
     * @param token     the interface being implemented
     * @param root      the root directory
     * @param extension the file extension (e.g., ".java", ".class")
     * @param separator the file separator to use in the package path
     * @return the path to the generated file
     */
    private Path getClassName(Class<?> token, Path root, String extension, char separator) {
        String simpleFileName = token.getSimpleName() + NAME_SUFFIX + extension;
        Path fileDir = root.resolve(token.getPackageName().replace('.', separator));
        return fileDir.resolve(simpleFileName);
    }

    /**
     * Constructs the entry name for the class file inside the JAR.
     *
     * @param token the interface being implemented
     * @return the JAR entry name for the compiled class file
     */
    private String getClassNameForJar(Class<?> token) {
        return String.format("%s/%s%s%s",
                token.getPackageName().replace('.', '/'),
                token.getSimpleName(), NAME_SUFFIX, CLASS_EXTENSION);
    }

    /**
     * Writes the implementation of the given interface to the provided writer. This method prints the package declaration,
     * class declaration, and the default implementations of all non-static methods.
     *
     * @param writer the writer to which the class implementation is printed
     * @param token  the interface being implemented
     * @param offset the indentation offset
     * @throws IOException if an I/O error occurs during writing
     */
    private void printClass(BufferedWriter writer, Class<?> token, int offset) throws IOException {
        printPackage(writer, token.getPackageName(), offset);
        String className = token.getSimpleName() + NAME_SUFFIX;
        printLine(writer, offset, "%s %s %s %s %s {",
                PUBLIC, CLASS, className, IMPLEMENTS, token.getCanonicalName());

        for (var method : token.getMethods()) {
            if (!Modifier.isStatic(method.getModifiers())) {
                printMethod(writer, method, offset + TAB_OFFSET);
            }
        }

        printLine(writer, offset, "}");
    }

    /**
     * Writes the package declaration to the provided writer.
     *
     * @param writer      the writer to which the package declaration is printed
     * @param packageName the package name
     * @param offset      the indentation offset
     * @throws IOException if an I/O error occurs during writing
     */
    private void printPackage(BufferedWriter writer, String packageName, int offset) throws IOException {
        if (!packageName.isEmpty()) {
            printLine(writer, offset, "%s %s;", PACKAGE, packageName);
            newLine(writer);
        }
    }

    /**
     * Writes the implementation of a method to the provided writer. This includes the {@code @Override} annotation,
     * the method signature, a default return statement (if needed), and proper formatting.
     *
     * @param writer the writer to which the method implementation is printed
     * @param method the method to implement
     * @param offset the indentation offset
     * @throws IOException if an I/O error occurs during writing
     */
    private void printMethod(BufferedWriter writer, Method method, int offset) throws IOException {
        printLine(writer, offset, "@Override");
        printMethodSignature(writer, method, offset);
        printReturn(writer, method.getReturnType(), offset + TAB_OFFSET);
        printLine(writer, offset, "}");
        newLine(writer);
    }

    /**
     * Writes the method signature for the given method to the writer.
     *
     * @param writer the writer to which the signature is printed
     * @param method the method for which the signature is generated
     * @param offset the indentation offset
     * @throws IOException if an I/O error occurs during writing
     */
    private void printMethodSignature(BufferedWriter writer, Method method, int offset) throws IOException {
        String args = String.join(", ", getArgs(method));
        String returnType = method.getReturnType().getCanonicalName();
        String methodName = method.getName();
        printLine(writer, offset, "%s %s %s (%s) {",
                PUBLIC, returnType, methodName, args);
    }

    /**
     * Constructs a list of parameter declarations for the given method.
     *
     * @param method the method whose parameters are to be listed
     * @return a list of strings representing the method parameters in the format "Type name"
     */
    private List<String> getArgs(Method method) {
        return Arrays.stream(method.getParameters())
                .map(param -> param.getType().getCanonicalName() + " " + param.getName())
                .toList();
    }

    /**
     * Writes the default return statement for a method based on its return type. If the return type is not {@code void},
     * a default value is returned; otherwise, an empty line is printed.
     *
     * @param writer     the writer to which the return statement is printed
     * @param returnType the return type of the method
     * @param offset     the indentation offset
     * @throws IOException if an I/O error occurs during writing
     */
    private void printReturn(BufferedWriter writer, Class<?> returnType, int offset) throws IOException {
        if (!returnType.equals(void.class)) {
            String defaultValue = DefaultValue.forType(returnType);
            printLine(writer, offset, "%s %s;", RETURN, defaultValue);
        } else {
            printLine(writer, offset, "");
        }
    }

    /**
     * Writes a new line to the writer.
     *
     * @param writer the writer to which the new line is written
     * @throws IOException if an I/O error occurs during writing
     */
    private void newLine(BufferedWriter writer) throws IOException {
        printLine(writer, DEFAULT_OFFSET, "");
    }

    /**
     * Writes a formatted line to the writer with the specified indentation.
     *
     * @param writer the writer to which the line is written
     * @param offset the indentation offset
     * @param format the format string
     * @param args   arguments referenced by the format specifiers in the format string
     * @throws IOException if an I/O error occurs during writing
     */
    private void printLine(BufferedWriter writer, int offset, String format, Object... args) throws IOException {
        StringBuilder line = new StringBuilder();
        try {
            line.append("\t".repeat(offset))
                    .append(String.format(format, args))
                    .append(System.lineSeparator());
            writer.write(line.toString());
        } catch (IOException e) {
            throw new IOException("Error writing line: " + line, e);
        }
    }
}
