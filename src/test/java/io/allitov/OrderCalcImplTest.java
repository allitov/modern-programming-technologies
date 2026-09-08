package io.allitov;

import io.allitov.mpt.OrderCalcImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "-1, -1.0",
            "5000, 0.0",
            "4999, 300.0"
    })
    void shouldCalculateShipping(double total, double expected) {
        double actual = orderCalc.calcShipping(total);

        assertThat(actual).isEqualTo(expected);
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

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("argumentsForShouldCountExpensiveItems")
    void shouldCountExpensiveItems(List<Double> prices, double threshold, int expected) {
        int actual = orderCalc.countExpensiveItems(prices, threshold);

        assertThat(actual).isEqualTo(expected);
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

        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> argumentsForShouldCountProductOddIndices() {
        return Stream.of(
                Arguments.of(List.of(10., 20.), 20.),
                Arguments.of(List.of(), 1.)
        );
    }

    @ParameterizedTest
    @MethodSource("provideMatrices")
    void shouldSumOddBelowMainDiagonal(List<List<Integer>> matrix, int expected) {
        int actual = orderCalc.sumOddBelowMainDiagonal(matrix);

        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideMatrices() {
        return Stream.of(
                Arguments.of(List.of(List.of(2, 4), List.of(3, 6)), 3),
                Arguments.of(List.of(List.of(2, 4), List.of(6, 8)), 0),
                Arguments.of(List.of(List.of(2, 4), List.of()), 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideRotatePricesRightCases")
    void shouldRotatePricesRight(List<Double> prices, int shift, List<Double> expected) {
        orderCalc.rotatePricesRight(prices, shift);

        assertThat(prices).isEqualTo(expected);
    }

    private static Stream<Arguments> provideRotatePricesRightCases() {
        return Stream.of(
                Arguments.of(List.of(), 5, List.of()),
                Arguments.of(List.of(1.0, 2.0, 3.0), 0, List.of(1.0, 2.0, 3.0)),
                Arguments.of(List.of(1.0, 2.0, 3.0), -1, List.of(1.0, 2.0, 3.0)),
                Arguments.of(new ArrayList<>(List.of(1.0, 2.0, 3.0)), 1, List.of(3.0, 1.0, 2.0)),
                Arguments.of(List.of(1.0, 2.0, 3.0), 3, List.of(1.0, 2.0, 3.0))
        );
    }

    @ParameterizedTest
    @MethodSource("provideProductOddIndexSumCases")
    void shouldCalculateProductOddIndexSum(List<List<Double>> matrix, double expected) {
        double actual = orderCalc.productOddIndexSum(matrix);

        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideProductOddIndexSumCases() {
        return Stream.of(
                Arguments.of(List.of(), 1.0),
                Arguments.of(List.of(List.of(1.0)), 1.0),
                Arguments.of(List.of(List.of(1.0, 2.0)), 2.0),
                Arguments.of(List.of(List.of(1.0, 2.0), List.of(3.0, 4.0)), 6.0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideProductEvenIndicesCases")
    void shouldCalculateProductEvenIndices(List<Double> prices, double expected) {
        double actual =  orderCalc.productEvenIndices(prices);

        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideProductEvenIndicesCases() {
        return Stream.of(
                Arguments.of(List.of(), 1.0),
                Arguments.of(List.of(5.0), 5.0),
                Arguments.of(List.of(2.0, 10.0, 3.0, 20.0, 4.0), 24.0),
                Arguments.of(List.of(2.0, 10.0, 0.0, 20.0, 4.0), 0.0)
        );
    }
}
