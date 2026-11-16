package com.program.appliances.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ConcreteAppliancesTest {

    @Test
    void refrigeratorKeepsVolume() {
        Refrigerator r = new Refrigerator("R", 150, 300);
        assertEquals(300, r.getVolumeLiters());
        assertTrue(r.toString().contains("300 L"));
        assertThrows(IllegalArgumentException.class,
                () -> new Refrigerator("R", 150, 0));
    }

    @Test
    void washingMachineKeepsMaxLoad() {
        WashingMachine w = new WashingMachine("W", 2200, 8);
        assertEquals(8, w.getMaxLoadKg());
        assertTrue(w.toString().contains("8 kg"));
        assertThrows(IllegalArgumentException.class,
                () -> new WashingMachine("W", 2200, -1));
    }

    @Test
    void kettleKeepsCapacity() {
        Kettle k = new Kettle("K", 2000, 1.7);
        assertEquals(1.7, k.getCapacityLiters(), 1e-9);
        assertThrows(IllegalArgumentException.class,
                () -> new Kettle("K", 2000, 0));
    }

    @Test
    void televisionKeepsDiagonal() {
        Television t = new Television("T", 100, 43);
        assertEquals(43, t.getScreenInches());
        assertThrows(IllegalArgumentException.class,
                () -> new Television("T", 100, 0));
    }

    @Test
    void vacuumCleanerKeepsContainerVolume() {
        VacuumCleaner v = new VacuumCleaner("V", 600, 0.8);
        assertEquals(0.8, v.getDustContainerLiters(), 1e-9);
        assertThrows(IllegalArgumentException.class,
                () -> new VacuumCleaner("V", 600, 0));
    }
}
