package sk.ivankohut.quantifa;

import org.cactoos.Text;
import org.json.JSONException;

public class AvValidatedResponse implements Text {

    private final Text response;

    public AvValidatedResponse(Text response) {
        this.response = response;
    }

    @Override
    @SuppressWarnings("java:S2201")
    public String asString() throws Exception {
        var string = response.asString();
        try {
            new AvMarketPriceOfStock(() -> string).price().orElse(null);
            return string;
        } catch (JSONException e) {
            throw new ApplicationException(
                    "Response from AV does not contain price. Invalid JSON structure or the maximum number of requests per minute limit reached.", e
            );
        }
    }
}
