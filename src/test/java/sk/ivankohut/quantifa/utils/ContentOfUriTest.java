package sk.ivankohut.quantifa.utils;

import org.cactoos.Text;
import org.cactoos.text.UncheckedText;
import org.junit.jupiter.api.Test;
import sk.ivankohut.quantifa.ApplicationException;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("java:S5786")
public class ContentOfUriTest {

    @Test
    void bodyOfResponseIfStatusIsOk() throws Exception {
        var timeout = Duration.ofSeconds(1);
        var body = "body";
        var uri = "http://path";
        var sut = new ContentOfUri(createHttpClient(uri, timeout, 200, body), () -> uri, timeout);
        // exercise
        var result = sut.asString();
        // verify
        assertThat(result).isEqualTo(body);
    }

    @Test
    void failsIfStatusIsNotOk() {
        var timeout = Duration.ofSeconds(1);
        var body = "body";
        var uri = "http://path";
        var sut = new ContentOfUri(createHttpClient(uri, timeout, 400, body), () -> uri, timeout);
        // exercise
        // verify
        assertThatThrownBy(sut::asString)
                .isInstanceOf(ApplicationException.class)
                .hasMessage("Retrieving the content of 'http://path' failed: body");
    }

    public static HttpClient createHttpClient(String uri, Duration timeout, int responseStatusCode, Text responseBody) {
        return createHttpClient(uri, timeout, responseStatusCode, new UncheckedText(responseBody).asString());
    }

    public static HttpClient createHttpClient(String uri, Duration timeout, int responseStatusCode, String responseBody) {
        return createHttpClient(Map.of(uri, responseBody), timeout, responseStatusCode);
    }

    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    public static HttpClient createHttpClient(Map<String, String> uriToResponseBody, Duration timeout, int responseStatusCode) {
        try {
            return when(mock(HttpClient.class).send(any(), any())).thenAnswer(invocation -> {
                var request = invocation.getArgument(0, HttpRequest.class);
                assertThat(request.timeout()).contains(timeout);
                var responseBody = uriToResponseBody.get(request.uri().toString());
                assertThat(responseBody).isNotNull();
                return createHttpResponse(responseBody, responseStatusCode);
            }).getMock();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static HttpResponse<String> createHttpResponse(String body, int statusCode) {
        var response = (HttpResponse<String>) mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(statusCode);
        when(response.body()).thenReturn(body);
        return response;
    }
}
