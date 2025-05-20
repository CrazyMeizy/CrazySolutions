package info.kgeorgiy.java.advanced.implementor.tools;

import info.kgeorgiy.java.advanced.implementor.AdvancedImplementorTest;
import info.kgeorgiy.java.advanced.implementor.BaseImplementorTest;
import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.tools.full.Arabic;
import info.kgeorgiy.java.advanced.implementor.tools.full.Greek;
import info.kgeorgiy.java.advanced.implementor.tools.full.Hebrew;
import info.kgeorgiy.java.advanced.implementor.tools.full.Russian;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

/**
 * Full {@link JarImpler} tests for advanced version
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class AdvancedJarImplementorTest extends AdvancedImplementorTest {
    @SuppressWarnings("this-escape")
    public AdvancedJarImplementorTest() {
        addDependency(JarImpler.class);
    }

    @Test
    @Override
    public void test01_constructor() {
        BaseImplementorTest.assertConstructor(Impler.class, JarImpler.class);
    }

    @Test
    public void test21_encoding() {
        testOk(
                Arabic.class, Arabic.مرحبا.class,
                Hebrew.class, Hebrew.הי.class,
                Greek.class, Greek.γεια.class,
                Russian.class, Russian.Привет.class
        );
    }

    @Override
    protected void implement(final Path root, final Impler implementor, final Class<?> clazz) throws ImplerException {
        super.implement(root, implementor, clazz);
        InterfaceJarImplementorTest.implementJar(root, implementor, clazz);
    }
}
