package sk.ivankohut.quantifa;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ApplicationConfiguration implements TwsCoordinates {

    private final String type;
    private final Map<String, String> configuration;

    public ApplicationConfiguration(String type, Map<String, String> configuration) {
        this.type = type;
        this.configuration = new HashMap<>(configuration);
    }

    @Override
    public String hostName() {
        return configuration.getOrDefault("TWS_HOSTNAME", "localhost");
    }

    @Override
    public int port() {
        return Integer.parseInt(configuration.getOrDefault("TWS_PORT", "7496"));
    }

    public StockContract fundamentalsRequest() {
        return new StockContract() {

            @Override
            public String exchange() {
                return mandatory("FUNDAMENTALS_EXCHANGE");
            }

            @Override
            public String symbol() {
                return mandatory("FUNDAMENTALS_SYMBOL");
            }

            @Override
            public String currency() {
                return mandatory("FUNDAMENTALS_CURRENCY");
            }
        };
    }

    public String fmpApiKey() {
        return mandatory("FMP_APIKEY");
    }

    public String avApiKey() {
        return configuration.getOrDefault("AV_APIKEY", "");
    }

    public PriceRequest priceRequest() {
        return new PriceRequest() {

            @Override
            public String source() {
                return mandatory("PRICE_SOURCE");
            }

            @Override
            public String exchange() {
                return mandatory("PRICE_EXCHANGE");
            }

            @Override
            public String symbol() {
                return mandatory("PRICE_SYMBOL");
            }

            @Override
            public String currency() {
                return mandatory("PRICE_CURRENCY");
            }

            @Override
            public int divisor() {
                return Integer.parseInt(configuration.getOrDefault("PRICE_DIVISOR", "1"));
            }

            @Override
            public int sourceCoolDownSeconds() {
                return Integer.parseInt(configuration.getOrDefault("PRICE_SOURCE_COOLDOWN_SECONDS", "0"));
            }
        };
    }

    public Path cacheDirectory() {
        return Path.of(mandatory("CACHE_DIR"));
    }

    private String mandatory(String key) {
        return Optional.ofNullable(configuration.get(key))
                .orElseThrow(() -> new ApplicationException("Configuration value %s of type %s is not defined.".formatted(key, type)));
    }
}
