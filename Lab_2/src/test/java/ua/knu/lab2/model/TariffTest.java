package ua.knu.lab2.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class TariffTest {

    private static Tariff tariff(String id, String name, String operator, String payroll) {
        return new Tariff(id, name, operator, new BigDecimal(payroll),
                new CallPrices(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE), BigDecimal.ONE,
                new Parameters(1, Tarification.PER_MINUTE, BigDecimal.ZERO));
    }

    @Test
    void sortsByPayrollThenName() {
        List<Tariff> list = new ArrayList<>(List.of(
                tariff("T1", "B", "Op", "20"), tariff("T2", "Z", "Op", "10"), tariff("T3", "A", "Op", "20")));
        list.sort(Tariff.BY_PAYROLL);
        assertEquals(List.of("T2", "T3", "T1"), list.stream().map(Tariff::id).toList());
    }

    @Test
    void sortsByNameAndByOperatorThenName() {
        List<Tariff> list = new ArrayList<>(List.of(
                tariff("T1", "B", "Y", "1"), tariff("T2", "A", "Y", "1"), tariff("T3", "C", "X", "1")));
        list.sort(Tariff.BY_NAME);
        assertEquals(List.of("T2", "T1", "T3"), list.stream().map(Tariff::id).toList());
        list.sort(Tariff.BY_OPERATOR_THEN_NAME);
        assertEquals(List.of("T3", "T2", "T1"), list.stream().map(Tariff::id).toList());
    }

    @ParameterizedTest
    @EnumSource(Tarification.class)
    void tarificationRoundTrip(Tarification value) {
        assertEquals(value, Tarification.fromXml(value.getXmlValue()));
    }

    @Test
    void unknownTarificationIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> Tarification.fromXml("per-hour"));
    }
}
