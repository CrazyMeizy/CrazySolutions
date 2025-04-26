package info.kgeorgiy.java.advanced.hello;

import info.kgeorgiy.java.advanced.base.BaseTester;

import java.util.List;
import java.util.Map;

/**
 * Test launcher.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public enum Tester {
    ;

    public static final BaseTester TESTER = TesterHelper.tester(Util::setMode, List.of(""), Map.of(
            "server", HelloServerTest.class,
            "client", HelloClientTest.class
    ));

    public static void main(final String... args) {
        TESTER.main(args);
    }
}
