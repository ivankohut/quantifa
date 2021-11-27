package sk.ivankohut.quantifa;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AvValidatedResponseTest {

    @Test
    void failsIfGivenResponseDoesNotContainsPriceNorIndicatesMissingPrice() {
        var response = """
                {
                  "Note": "Thank you for using Alpha Vantage! Our standard API call frequency is 5 calls per minute and 500 calls per day..."
                }""";
        var sut = new AvValidatedResponse(() -> response);
        // exercise
        // verify
        assertThatThrownBy(sut::asString)
                .isInstanceOf(ApplicationException.class)
                .hasMessage("Response from AV does not contain price. Invalid JSON structure or the maximum number of requests per minute limit reached.");
    }

    @Test
    void givenResponseIfTheContainsPrice() throws Exception {
        var response = """
                {"Global Quote": { "05. price": "1.23" }}""";
        var sut = new AvValidatedResponse(() -> response);
        // exercise
        var result = sut.asString();
        // verify
        assertThat(result).isEqualTo(response);
    }
}
