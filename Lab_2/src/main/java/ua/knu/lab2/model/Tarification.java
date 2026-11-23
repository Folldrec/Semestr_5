package ua.knu.lab2.model;

import java.util.Arrays;

/** Тарифікація: 12-секундна або похвилинна. */
public enum Tarification {
    TWELVE_SECOND("12-second"),
    PER_MINUTE("per-minute");

    private final String xmlValue;

    Tarification(String xmlValue) {
        this.xmlValue = xmlValue;
    }

    public String getXmlValue() {
        return xmlValue;
    }

    /** Повертає значення переліку за текстом з XML (див. enumeration в XSD). */
    public static Tarification fromXml(String value) {
        return Arrays.stream(values())
                .filter(t -> t.xmlValue.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown tarification: " + value));
    }
}
