package info.kgeorgiy.java.advanced.crawler;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Stream;

/**
 * Utility class for <a href="http://tools.ietf.org/html/rfc3986">URL</a> manipulations.
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class URLUtils {
    // Utility class
    private URLUtils() {}

    /**
     * Returns host part of the specified URL.
     *
     * @param url url to get host part for.
     *
     * @return host part of the provided URL or empty string if URL has no host part.
     *
     * @throws MalformedURLException if specified URL is invalid.
     */
    public static String getHost(final String url) throws MalformedURLException {
        return getURI(url).getHost();
    }

    /**
     * Converts string representation of the URL to {@link URI}.
     * Converted URI always has non-empty path.
     * If original URL has empty path, the "{@code /}" path is used.
     *
     * @param url url to convert.
     *
     * @return converted URL.
     *
     * @throws MalformedURLException if specified URL is invalid.
     */
    public static URI getURI(final String url) throws MalformedURLException {
        final String fragmentless = removeFragment(url);
        try {
            final URI uri = new URI(fragmentless);
            return uri.getPath() == null || uri.getPath().isEmpty() ? new URI(fragmentless + "/") : uri;
        } catch (final URISyntaxException e) {
            throw new MalformedURLException(e.getMessage());
        }
    }

    /**
     * Removes fragment part of the URL.
     *
     * @param url url to remove fragment from.
     *
     * @return URL without fragment part
     */
    public static String removeFragment(final String url) {
        final int index = url.indexOf('#');
        return index >= 0 ? url.substring(0, index) : url;
    }

    /**
     * Extract links from the HTML document.
     *
     * @param url base URL for relative links.
     * @param is document stream.
     *
     * @return all links in the document.
     *
     * @throws IOException if an error occurred during link extraction.
     */
    public static List<String> extractLinks(final URI url, final InputStream is) throws IOException {
        try {
            final List<String> result = Jsoup.parse(is, null, url.toString())
                    .select("a[href]").stream()
                    .flatMap(element -> {
                        try {
                            return Stream.of(url.resolve(element.attr("href")));
                        } catch (final IllegalArgumentException ignored) {
                            return Stream.of();
                        }
                    })
                    .filter(href -> href.getHost() != null && href.getScheme() != null)
                    .filter(href -> ("http".equalsIgnoreCase(href.getScheme()) || "https".equalsIgnoreCase(href.getScheme())))
                    .map(href -> removeFragment(href.normalize().toString()))
                    .toList();
//        System.out.println("Links for " + url + ": " + result);
            return result;
        } catch (final IOException | RuntimeException e) {
            throw new IOException("Cannot extract links for " + url + ": " + e.getMessage(), e);
        }
    }
}
