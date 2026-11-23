package ua.knu.lab2.model;

import java.math.BigDecimal;

/** Параметри тарифу. */
public record Parameters(int favoriteNumbers, Tarification tarification, BigDecimal connectionFee) {
}
