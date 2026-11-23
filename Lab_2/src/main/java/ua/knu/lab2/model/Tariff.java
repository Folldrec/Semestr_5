package ua.knu.lab2.model;

import java.math.BigDecimal;
import java.util.Comparator;

/** Тариф мобільного оператора (відповідає елементу Tariff у tariffs.xsd). */
public record Tariff(
        String id,
        String name,
        String operatorName,
        BigDecimal payroll,
        CallPrices callPrices,
        BigDecimal smsPrice,
        Parameters parameters) {

    public static final Comparator<Tariff> BY_NAME = Comparator.comparing(Tariff::name);

    public static final Comparator<Tariff> BY_PAYROLL =
            Comparator.comparing(Tariff::payroll).thenComparing(BY_NAME);

    public static final Comparator<Tariff> BY_OPERATOR_THEN_NAME =
            Comparator.comparing(Tariff::operatorName).thenComparing(BY_NAME);
}
