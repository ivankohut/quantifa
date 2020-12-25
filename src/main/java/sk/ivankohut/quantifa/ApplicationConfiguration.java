package sk.ivankohut.quantifa;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class ApplicationConfiguration implements TwsCoordinates, StockContract {

    private final String type;
    private final Map<String, String> configuration;

    @Override
    public String hostName() {
        return configuration.getOrDefault("TWS_HOSTNAME", "localhost");
    }

    @Override
    public int port() {
        return Integer.parseInt(configuration.getOrDefault("TWS_PORT", "7496"));
    }

    @Override
    public String exchange() {
        return mandatory("EXCHANGE");
    }

    @Override
    public String symbol() {
        return mandatory("SYMBOL");
    }

    @Override
    public String currency() {
        return mandatory("CURRENCY");
    }

    private String mandatory(String key) {
        return Optional.ofNullable(configuration.get(key))
                .orElseThrow(() -> new ApplicationException("Configuration value %s of type %s is not defined.".formatted(key, type)));
    }
}
