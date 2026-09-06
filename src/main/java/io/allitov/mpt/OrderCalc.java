package io.allitov.mpt;

import java.util.List;

public interface OrderCalc {

    double applyDiscount(double total, boolean isPremium);

    double calcShipping(double total);

    double finalPrice(double total, double discount, double shipping);

    int countExpensiveItems(List<Double> prices, double threshold);
}
