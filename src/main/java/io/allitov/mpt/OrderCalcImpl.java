package io.allitov.mpt;

import java.util.List;

public class OrderCalcImpl implements OrderCalc {

    /**
     * Рассчитывает стоимость заказа с учётом скидки.
     *
     * <p>Размер скидки зависит от общей стоимости заказа и статуса клиента:</p>
     *
     * <ul>
     *     <li>Если {@code total < 0}, возвращается {@code -1.0}.</li>
     *     <li>Если {@code total >= 10000}:
     *         <ul>
     *             <li>20% скидка для премиум-клиентов;</li>
     *             <li>15% скидка для обычных клиентов.</li>
     *         </ul>
     *     </li>
     *     <li>Если {@code total >= 5000}:
     *         <ul>
     *             <li>10% скидка для премиум-клиентов;</li>
     *             <li>5% скидка для обычных клиентов.</li>
     *         </ul>
     *     </li>
     *     <li>В остальных случаях скидка не предоставляется.</li>
     * </ul>
     *
     * @param total     общая стоимость заказа.
     * @param isPremium {@code true}, если клиент является премиум-клиентом;
     *                  {@code false} в противном случае.
     * @return стоимость заказа после применения скидки; {@code -1.0}, если {@code total} отрицательный.
     */
    @Override
    public double applyDiscount(double total, boolean isPremium) {
        if (total < 0) {
            return -1.0;
        }
        if (total >= 10000) {
            if (isPremium) {
                return total * 0.80;
            } else {
                return total * 0.85;
            }
        } else if (total >= 5000) {
            if (isPremium) {
                return total * 0.90;
            } else {
                return total * 0.95;
            }
        }

        return total;
    }

    /**
     * Рассчитывает стоимость доставки в зависимости от общей стоимости заказа.
     *
     * <p>Правила расчёта:</p>
     *
     * <ul>
     *     <li>Если {@code total < 0}, возвращается {@code -1.0}.</li>
     *     <li>Если {@code total >= 5000}, доставка бесплатная.</li>
     *     <li>В остальных случаях стоимость доставки составляет {@code 300.0}.</li>
     * </ul>
     *
     * @param total общая стоимость заказа.
     * @return стоимость доставки; {@code -1.0}, если {@code total} отрицательный.
     */
    @Override
    public double calcShipping(double total) {
        if (total < 0) {
            return -1.0;
        }
        if (total >= 5000) {
            return 0.0;
        }

        return 300.0;
    }

    /**
     * Рассчитывает итоговую стоимость заказа с учётом скидки и стоимости доставки.
     *
     * <p>Итоговая цена рассчитывается по формуле:</p>
     *
     * <pre>
     * {@code result = total - discount + shipping}
     * </pre>
     *
     * <p>Дополнительные правила:</p>
     *
     * <ul>
     *     <li>Если любой из аргументов отрицательный, возвращается {@code -1.0}.</li>
     *     <li>Если итоговая цена получается отрицательной, она устанавливается в {@code 0.0}.</li>
     *     <li>Результат округляется до двух знаков после запятой.</li>
     * </ul>
     *
     * @param total    общая стоимость заказа.
     * @param discount размер предоставленной скидки.
     * @param shipping стоимость доставки.
     * @return итоговая стоимость заказа, округлённая до двух знаков;
     * {@code -1.0}, если хотя бы один аргумент отрицательный.
     */
    @Override
    public double finalPrice(double total, double discount, double shipping) {
        if (total < 0 || discount < 0 || shipping < 0) {
            return -1.0;
        }

        double result = total - discount + shipping;

        if (result < 0) {
            result = 0.0;
        }

        return Math.round(result * 100) / 100.0;
    }


    /**
     * Подсчитывает количество дорогих товаров в корзине.
     *
     * <p>Товар считается дорогим, если его цена строго
     * превышает заданный порог {@code threshold}.</p>
     *
     * <p>Если список товаров пуст, метод возвращает {@code 0}.</p>
     *
     * @param prices    список цен товаров в корзине.
     * @param threshold порог стоимости товара.
     * @return количество товаров, цена которых строго больше {@code threshold}.
     */
    @Override
    public int countExpensiveItems(List<Double> prices, double threshold) {
        int count = 0;
        for (Double price : prices) {
            if (price > threshold) {
                count++;
            }
        }

        return count;
    }

    /**
     * Вычисляет произведение цен товаров, находящихся на нечётных индексах корзины.
     *
     * <p>Если список пуст или содержит только элементы с чётными индексами,
     * метод возвращает {@code 1.0}.</p>
     *
     * @param prices список цен товаров в корзине.
     * @return произведение цен товаров на нечётных индексах.
     */
    public double productOddIndices(List<Double> prices) {
        double product = 1.0;

        for (int i = 1; i < prices.size(); i += 2) {
            product *= prices.get(i);
        }

        return product;
    }

    /**
     * Вычисляет сумму нечётных элементов матрицы, расположенных
     * ниже главной диагонали.
     *
     * <p>Элемент считается расположенным ниже главной диагонали,
     * если индекс его строки больше индекса столбца:
     * {@code row > column}.</p>
     *
     * <p>Если подходящих элементов нет, метод возвращает {@code 0}.</p>
     *
     * @param matrix двумерный список целых чисел (матрица).
     * @return сумма нечётных элементов, расположенных ниже главной диагонали.
     */
    public int sumOddBelowMainDiagonal(List<List<Integer>> matrix) {
        int sum = 0;
        for (int row = 0; row < matrix.size(); row++) {
            for (int col = 0; col < row && col < matrix.get(row).size(); col++) {
                int element = matrix.get(row).get(col);
                if (element % 2 != 0) {
                    sum += element;
                }
            }
        }

        return sum;
    }
}
