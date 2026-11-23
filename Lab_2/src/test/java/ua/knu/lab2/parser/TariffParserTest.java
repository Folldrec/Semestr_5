package ua.knu.lab2.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ua.knu.lab2.TestPaths;
import ua.knu.lab2.model.Tarification;
import ua.knu.lab2.model.Tariff;
import ua.knu.lab2.xml.XmlProcessingException;

class TariffParserTest {

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void parsesAllTariffs(ParserType type) throws Exception {
        List<Tariff> tariffs = type.create().parse(TestPaths.XML);
        assertEquals(6, tariffs.size());
        Tariff first = tariffs.get(0);
        assertEquals("T001", first.id());
        assertEquals("Smart Start", first.name());
        assertEquals("Kyivstar", first.operatorName());
        assertEquals(new BigDecimal("150.00"), first.payroll());
        assertEquals(new BigDecimal("1.20"), first.callPrices().insideNetwork());
        assertEquals(new BigDecimal("2.50"), first.callPrices().outsideNetwork());
        assertEquals(new BigDecimal("3.00"), first.callPrices().landline());
        assertEquals(new BigDecimal("0.80"), first.smsPrice());
        assertEquals(3, first.parameters().favoriteNumbers());
        assertEquals(Tarification.PER_MINUTE, first.parameters().tarification());
        assertEquals(new BigDecimal("0.00"), first.parameters().connectionFee());
    }

    @Test
    void allParsersProduceTheSameResult() throws Exception {
        List<Tariff> sax = ParserType.SAX.create().parse(TestPaths.XML);
        assertEquals(sax, ParserType.DOM.create().parse(TestPaths.XML));
        assertEquals(sax, ParserType.STAX.create().parse(TestPaths.XML));
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void parsesEmptyList(ParserType type) throws Exception {
        assertEquals(List.of(), type.create().parse(TestPaths.resource("empty.xml")));
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void missingFileFails(ParserType type) {
        assertThrows(XmlProcessingException.class, () -> type.create().parse(Path.of("no-such-file.xml")));
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void malformedXmlFails(ParserType type) {
        assertThrows(XmlProcessingException.class,
                () -> type.create().parse(TestPaths.resource("malformed.xml")));
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void incompleteTariffFails(ParserType type) {
        assertThrows(XmlProcessingException.class,
                () -> type.create().parse(TestPaths.resource("missing-element.xml")));
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void badNumberFails(ParserType type) {
        assertThrows(XmlProcessingException.class,
                () -> type.create().parse(TestPaths.resource("bad-number.xml")));
    }

    @Test
    void factoryCreatesExpectedImplementations() {
        assertInstanceOf(SaxTariffParser.class, ParserType.SAX.create());
        assertInstanceOf(DomTariffParser.class, ParserType.DOM.create());
        assertInstanceOf(StaxTariffParser.class, ParserType.STAX.create());
    }
}
