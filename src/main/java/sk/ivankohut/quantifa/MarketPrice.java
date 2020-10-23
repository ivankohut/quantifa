package sk.ivankohut.quantifa;

import java.math.BigDecimal;
import java.util.Optional;

public interface MarketPrice {

    Optional<BigDecimal> price();
}
