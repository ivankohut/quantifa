package sk.ivankohut.quantifa.utils;

import org.cactoos.Text;
import sk.ivankohut.quantifa.ApplicationException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ContentOfUri implements Text {

    private static final int HTTP_STATUS_OK = 200;

    private final Text uri;
    private final Duration timeout;
    private final HttpClient httpClient;

    public ContentOfUri(HttpClient httpClient, Text uri, Duration timeout) {
        this.httpClient = httpClient;
        this.uri = uri;
        this.timeout = timeout;
    }

    @Override
    public String asString() throws Exception {
        var request = HttpRequest.newBuilder(URI.create(uri.asString()))
                .timeout(timeout)
                .GET()
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == HTTP_STATUS_OK) {
            return response.body();
        }
        throw new ApplicationException("Retrieving the content of '%s' failed: %s".formatted(uri.asString(), response.body()));
    }
}
