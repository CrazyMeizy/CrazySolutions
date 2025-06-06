package info.kgeorgiy.java.advanced.hello;

import info.kgeorgiy.java.advanced.base.BaseTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntFunction;

/**
 * Basic tests for {@link HelloServer}.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class HelloServerTest extends BaseTest {
    private static final AtomicInteger port = new AtomicInteger(28800);
    public static final String REQUEST = HelloServerTest.class.getName();

    public HelloServerTest() {
    }

    @Test
    public void test01_singleRequest() throws IOException {
        test(1, port -> socket -> checkResponse(port, socket, REQUEST));
    }

    @Test
    public void test02_multipleClients() throws IOException {
        try (final HelloServer server = createCUT()) {
            final int port = getPort();
            server.start(port, 1);
            for (int i = 0; i < 10; i++) {
                client(port, REQUEST + i);
            }
        }
    }

    @Test
    public void test04_parallelRequests() throws IOException {
        test(1, port -> socket -> {
            final Set<String> responses = new HashSet<>();
            for (int i = 0; i < 10; i++) {
                final String request = REQUEST + i;
                responses.add(Util.response(request));
                send(port, socket, request);
            }
            for (int i = 0; i < 10; i++) {
                final String response = Util.receive(socket);
                Assertions.assertTrue(responses.remove(response), "Unexpected response " + response);
            }
        });
    }

    private static void send(final int port, final DatagramSocket socket, final String request) throws IOException {
        Util.send(socket, request, new InetSocketAddress("localhost", port));
    }

    private static void client(final int port, final String request) throws IOException {
        try (final DatagramSocket socket = new DatagramSocket(null)) {
            checkResponse(port, socket, request);
        }
    }

    public static void test(final int workers, final IntFunction<ConsumerCommand<DatagramSocket, IOException>> command) throws IOException {
        try (final HelloServer server = createCUT()) {
            final int port = getPort();
            server.start(port, workers);
            try (final DatagramSocket socket = new DatagramSocket()) {
                command.apply(port).run(socket);
            }
        }
    }

    private static void checkResponse(final int port, final DatagramSocket socket, final String request) throws IOException {
        final String response = Util.request(request, socket, new InetSocketAddress("localhost", port));
        Assertions.assertEquals(Util.response(request), response, "Invalid response");
    }

    private static int getPort() {
        return port.getAndIncrement();
    }
}
