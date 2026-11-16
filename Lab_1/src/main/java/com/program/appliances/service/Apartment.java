package com.program.appliances.service;

import com.program.appliances.model.Appliance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Apartment that owns a collection of household appliances.
 */
public class Apartment {

    private final List<Appliance> appliances = new ArrayList<>();

    /**
     * Adds an appliance to the apartment.
     *
     * @param appliance appliance to add, must not be null
     */
    public void addAppliance(Appliance appliance) {
        if (appliance == null) {
            throw new IllegalArgumentException("Appliance must not be null");
        }
        appliances.add(appliance);
    }

    public List<Appliance> getAppliances() {
        return Collections.unmodifiableList(appliances);
    }

    /**
     * Calculates the total power drawn by plugged-in appliances.
     *
     * @return total consumed power in watts
     */
    public int calculateConsumedPower() {
        return appliances.stream().mapToInt(Appliance::getConsumedPower).sum();
    }

    /**
     * Sorts appliances in place by rated power in ascending order.
     */
    public void sortByPower() {
        appliances.sort(Comparator.comparingInt(Appliance::getPowerWatts));
    }

    /**
     * Finds appliances whose rated power lies in the given range.
     *
     * @param minWatts lower bound, inclusive
     * @param maxWatts upper bound, inclusive
     * @return matching appliances, possibly empty
     */
    public List<Appliance> findByPowerRange(int minWatts, int maxWatts) {
        if (minWatts > maxWatts) {
            throw new IllegalArgumentException("Min power exceeds max power");
        }
        return appliances.stream()
                .filter(a -> a.getPowerWatts() >= minWatts
                        && a.getPowerWatts() <= maxWatts)
                .toList();
    }
}
