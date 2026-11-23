package ua.knu.lab2.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.Test;
import ua.knu.lab2.model.Tarification;
import ua.knu.lab2.model.Tariff;
import ua.knu.lab2.xml.XmlProcessingException;

class TariffBuilderTest {

    private static final Map<String, String> VALID = Map.of(
            XmlTags.NAME, " Smart ", XmlTags.OPERATOR_NAME, "Op", XmlTags.PAYROLL, "10.50",
            XmlTags.INSIDE_NETWORK, "1", XmlTags.OUTSIDE_NETWORK, "2", XmlTags.LANDLINE, "3",
            XmlTags.SMS_PRICE, "0.5", XmlTags.FAVORITE_NUMBER, "4", XmlTags.TARIFICATION, "12-second");

    private static TariffBuilder builderWith(Map<String, String> values) {
        TariffBuilder builder = new TariffBuilder("T001");
        values.forEach(builder::accept);
        return builder;
    }

    @Test
    void buildsTariffAndTrimsText() throws Exception {
        TariffBuilder builder = builderWith(VALID);
        builder.accept(XmlTags.CONNECTION_FEE, "7");
        Tariff t = builder.build();
        assertEquals("Smart", t.name());
        assertEquals(new BigDecimal("10.50"), t.payroll());
        assertEquals(new BigDecimal("3"), t.callPrices().landline());
        assertEquals(4, t.parameters().favoriteNumbers());
        assertEquals(Tarification.TWELVE_SECOND, t.parameters().tarification());
        assertEquals(new BigDecimal("7"), t.parameters().connectionFee());
    }

    @Test
    void missingElementFails() {
        XmlProcessingException e = assertThrows(XmlProcessingException.class, () -> builderWith(VALID).build());
        assertEquals("Invalid tariff 'T001': missing element " + XmlTags.CONNECTION_FEE, e.getMessage());
    }

    @Test
    void badNumberFails() {
        TariffBuilder builder = builderWith(VALID);
        builder.accept(XmlTags.CONNECTION_FEE, "abc");
        assertThrows(XmlProcessingException.class, builder::build);
    }
}
