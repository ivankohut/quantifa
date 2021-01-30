package sk.ivankohut.quantifa.utils;

import org.junit.jupiter.api.Test;
import sk.ivankohut.quantifa.ApplicationException;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ContentOfUriTest {

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
    void failsIfStatusIsNotOk() throws Exception {
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

    private static HttpClient createHttpClient(String uri, Duration timeout, int responseStatusCode, String responseBody)
            throws IOException, InterruptedException {
        return when(mock(HttpClient.class).send(any(), any())).thenAnswer(invocation -> {
            var request = invocation.getArgument(0, HttpRequest.class);
            assertThat(request.uri()).hasToString(uri);
            assertThat(request.timeout()).contains(timeout);
            return createHttpResponse(responseBody, responseStatusCode);
        }).getMock();
    }

    @SuppressWarnings("unchecked")
    private static HttpResponse<String> createHttpResponse(String body, int statusCode) {
        var response = (HttpResponse<String>) mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(statusCode);
        when(response.body()).thenReturn(body);
        return response;
    }
}
