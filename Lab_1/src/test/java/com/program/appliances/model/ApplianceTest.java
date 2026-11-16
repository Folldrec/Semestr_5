package com.program.appliances.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ApplianceTest {

    @Test
    void consumesNothingWhenUnplugged() {
        Kettle kettle = new Kettle("K", 2000, 1.5);
        assertFalse(kettle.isPluggedIn());
        assertEquals(0, kettle.getConsumedPower());
    }

    @Test
    void consumesRatedPowerWhenPluggedIn() {
        Kettle kettle = new Kettle("K", 2000, 1.5);
        kettle.plugIn();
        assertTrue(kettle.isPluggedIn());
        assertEquals(2000, kettle.getConsumedPower());
    }

    @Test
    void unplugStopsConsumption() {
        Kettle kettle = new Kettle("K", 2000, 1.5);
        kettle.plugIn();
        kettle.unplug();
        assertEquals(0, kettle.getConsumedPower());
    }

    @Test
    void rejectsBlankName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Kettle(" ", 2000, 1.5));
        assertThrows(IllegalArgumentException.class,
                () -> new Kettle(null, 2000, 1.5));
    }

    @Test
    void rejectsNonPositivePower() {
        assertThrows(IllegalArgumentException.class,
                () -> new Kettle("K", 0, 1.5));
    }

    @Test
    void toStringContainsKeyData() {
        Television tv = new Television("LG", 120, 55);
        tv.plugIn();
        String text = tv.toString();
        assertTrue(text.contains("Television"));
        assertTrue(text.contains("LG"));
        assertTrue(text.contains("120 W"));
        assertTrue(text.contains("55 inches"));
        assertTrue(text.contains("plugged in"));
    }
}
