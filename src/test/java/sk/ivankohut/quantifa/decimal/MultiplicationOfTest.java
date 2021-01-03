package sk.ivankohut.quantifa.decimal;

import org.cactoos.list.ListOf;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MultiplicationOfTest {

    @Test
    void multiplicationOfGivenMultiplicands() throws Exception {
        var sut = new MultiplicationOf(new DecimalOf(2), new DecimalOf(3.5), new DecimalOf(1), new DecimalOf(1.4));
        // exercise
        var result = sut.value();
        // verify
        assertThat(result).isEqualByComparingTo("9.8");
    }

    @Test
    void oneWhenNoMultiplicandsGiven() throws Exception {
        var sut = new MultiplicationOf(new ListOf<>());
        // exercise
        var result = sut.value();
        // verify
        assertThat(result).isEqualByComparingTo("1");
    }
}
