package io.allitov;

import io.allitov.mpt.OrderCalcImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource("argumentsForShouldCountExpensiveItems")
    void shouldCountExpensiveItems(List<Double> prices, double threshold, int expected) {
        int actual = orderCalc.countExpensiveItems(prices, threshold);

        assertEquals(expected, actual);
    }

    private static Stream<Arguments> argumentsForShouldCountExpensiveItems() {
        return Stream.of(
                Arguments.of(List.of(), 50., 0),
                Arguments.of(List.of(100., 50.), 50., 1)
        );
    }

    @ParameterizedTest
    @MethodSource("argumentsForShouldCountProductOddIndices")
    void shouldCountProductOddIndices(List<Double> prices, double expected) {
        double actual = orderCalc.productOddIndices(prices);

        assertEquals(expected, actual);
    }

    private static Stream<Arguments> argumentsForShouldCountProductOddIndices() {
        return Stream.of(
                Arguments.of(List.of(10., 20.), 20.),
                Arguments.of(List.of(), 1.)
        );
    }

    @ParameterizedTest
    @MethodSource("provideMatrices")
    void shouldSumOddBelowMainDiagonal(List<List<Integer>> matrix, int expected ) {
        int actual = orderCalc.sumOddBelowMainDiagonal(matrix);

        assertEquals(expected, actual); 
    }

    private static Stream<Arguments> provideMatrices() {
        return Stream.of(
                Arguments.of(List.of(List.of(2, 4), List.of(3, 6)), 3),
                Arguments.of(List.of(List.of(2, 4), List.of(6, 8)), 0),
                Arguments.of(List.of(List.of(2, 4), List.of()), 0)
        );
    }
}
