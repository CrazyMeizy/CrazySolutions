package info.kgeorgiy.ja.chuprov.crawler;

import info.kgeorgiy.java.advanced.crawler.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

public class WebCrawler implements NewCrawler {

    private final Downloader downloader;
    private final int downloaders;
    private final int extractors;
    private final int perHosts;

    public WebCrawler(Downloader downloader, int downloaders, int extractors, int perHosts) {
        this.downloader = downloader;
        this.downloaders = downloaders;
        this.extractors = extractors;
        this.perHosts = perHosts;

    }

    @Override
    public Result download(String url, int depth, List<String> excludes) {
        HashSet<String> set = new HashSet<>();
        dfs(url, 1, depth, set, excludes);
        return new Result(new ArrayList<>(set), Map.of());
    }

    private void dfs(String url, int currDepth, int maxDepth, HashSet<String> set, List<String> excludes) {
        try {
            String host = new URI(url).toURL().getHost();
            for (String sub : excludes) {
                if (host.contains(sub)) {
                    return;
                }
            }
            set.add(url);
        } catch (URISyntaxException | MalformedURLException e) {
            throw new RuntimeException(e);
        }

        if(currDepth == maxDepth){
            return;
        }
        List<String> links;
        try{
            Document document = downloader.download(url);
            links = document.extractLinks();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        links.forEach(link -> dfs(link, currDepth + 1, maxDepth, set, excludes));
        System.err.println(url);
        if(Objects.equals(url, "https://minobrnauki.gov.ru/")){
            System.err.println("Here");
        }
    }

    @Override
    public void close() {

    }
}
