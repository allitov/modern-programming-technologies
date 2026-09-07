package io.allitov;

import io.allitov.mpt.OrderCalcImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderCalcImplTest {

    private final OrderCalcImpl orderCalc = new OrderCalcImpl();

    @ParameterizedTest
    @CsvSource({
            "-1,     false, -1.0",
            "10000,  true,  8000.0",
            "10000,  false, 8500.0",
            "5000,   true,  4500.0",
            "5000,   false, 4750.0",
            "0,      false, 0.0"
    })
    void shouldApplyDiscount(double total, boolean isPremium, double expected) {
        double actual = orderCalc.applyDiscount(total, isPremium);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({
            "-1, -1.0",
            "5000, 0.0",
            "4999, 300.0"
    })
    void shouldCalculateShipping(double total, double expected) {
        double actual = orderCalc.calcShipping(total);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({
            "-100, 10, 20, -1",
            "100, -10, 20, -1",
            "100, 10, -20, -1",
            "1000, 200, 50, 850",
            "100, 200, 50, 0"
    })
    void shouldCalculateFinalPrice(double total, double discount, double shipping, double expected) {
        double actual = orderCalc.finalPrice(total, discount, shipping);

        assertEquals(expected, actual);
    }
}
