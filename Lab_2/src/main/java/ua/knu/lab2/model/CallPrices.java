package ua.knu.lab2.model;

import java.math.BigDecimal;

/** Ціни на дзвінки, грн за хвилину. */
public record CallPrices(BigDecimal insideNetwork, BigDecimal outsideNetwork, BigDecimal landline) {
}
