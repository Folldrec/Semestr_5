package com.program.appliances.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.program.appliances.model.Appliance;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApartmentTest {

    private Apartment apartment;
    private Appliance weak;
    private Appliance medium;
    private Appliance strong;

    @BeforeEach
    void setUp() {
        apartment = new Apartment();
        weak = mockAppliance(100, 100);
        medium = mockAppliance(1000, 0);
        strong = mockAppliance(2000, 2000);
        apartment.addAppliance(strong);
        apartment.addAppliance(weak);
        apartment.addAppliance(medium);
    }

    private static Appliance mockAppliance(int rated, int consumed) {
        Appliance appliance = mock(Appliance.class);
        when(appliance.getPowerWatts()).thenReturn(rated);
        when(appliance.getConsumedPower()).thenReturn(consumed);
        return appliance;
    }

    @Test
    void sumsOnlyConsumedPower() {
        assertEquals(2100, apartment.calculateConsumedPower());
    }

    @Test
    void emptyApartmentConsumesNothing() {
        assertEquals(0, new Apartment().calculateConsumedPower());
    }

    @Test
    void sortsByPowerAscending() {
        apartment.sortByPower();
        assertEquals(List.of(weak, medium, strong), apartment.getAppliances());
    }

    @Test
    void findsAppliancesInclusiveRange() {
        assertEquals(List.of(strong, medium),
                apartment.findByPowerRange(1000, 2000));
    }

    @Test
    void returnsEmptyListWhenNothingMatches() {
        assertTrue(apartment.findByPowerRange(3000, 4000).isEmpty());
    }

    @Test
    void rejectsInvalidRange() {
        assertThrows(IllegalArgumentException.class,
                () -> apartment.findByPowerRange(10, 5));
    }

    @Test
    void rejectsNullAppliance() {
        assertThrows(IllegalArgumentException.class,
                () -> apartment.addAppliance(null));
    }

    @Test
    void exposedListIsUnmodifiable() {
        assertThrows(UnsupportedOperationException.class,
                () -> apartment.getAppliances().clear());
    }
}
