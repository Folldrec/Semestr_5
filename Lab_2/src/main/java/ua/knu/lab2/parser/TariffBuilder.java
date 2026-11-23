package ua.knu.lab2.parser;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import ua.knu.lab2.model.CallPrices;
import ua.knu.lab2.model.Parameters;
import ua.knu.lab2.model.Tarification;
import ua.knu.lab2.model.Tariff;
import ua.knu.lab2.xml.XmlProcessingException;

/**
 * Спільна для SAX, DOM та StAX логіка побудови об'єкта Tariff:
 * парсери лише передають пари «елемент - текст», а перетворення типів зібрано тут.
 */
final class TariffBuilder {

    private final String id;
    private final Map<String, String> values = new HashMap<>();

    TariffBuilder(String id) {
        this.id = id;
    }

    void accept(String tag, String text) {
        values.put(tag, text.trim());
    }

    Tariff build() throws XmlProcessingException {
        try {
            return new Tariff(
                    id,
                    text(XmlTags.NAME),
                    text(XmlTags.OPERATOR_NAME),
                    money(XmlTags.PAYROLL),
                    new CallPrices(
                            money(XmlTags.INSIDE_NETWORK),
                            money(XmlTags.OUTSIDE_NETWORK),
                            money(XmlTags.LANDLINE)),
                    money(XmlTags.SMS_PRICE),
                    new Parameters(
                            Integer.parseInt(text(XmlTags.FAVORITE_NUMBER)),
                            Tarification.fromXml(text(XmlTags.TARIFICATION)),
                            money(XmlTags.CONNECTION_FEE)));
        } catch (IllegalArgumentException e) {
            throw new XmlProcessingException("Invalid tariff '" + id + "': " + e.getMessage(), e);
        }
    }

    private String text(String tag) {
        String value = values.get(tag);
        if (value == null) {
            throw new IllegalArgumentException("missing element " + tag);
        }
        return value;
    }

    private BigDecimal money(String tag) {
        return new BigDecimal(text(tag));
    }
}
