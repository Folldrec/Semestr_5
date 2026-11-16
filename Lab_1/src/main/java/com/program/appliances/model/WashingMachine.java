package com.program.appliances.model;

/**
 * Washing machine with a given maximum load.
 */
public class WashingMachine extends Appliance {

    private final int maxLoadKg;

    /**
     * Creates a washing machine.
     *
     * @param name       model name
     * @param powerWatts rated power in watts
     * @param maxLoadKg maximum load in kilograms
     */
    public WashingMachine(String name, int powerWatts, int maxLoadKg) {
        super(name, powerWatts);
        if (maxLoadKg <= 0) {
            throw new IllegalArgumentException("maximum load in kilograms must be positive");
        }
        this.maxLoadKg = maxLoadKg;
    }

    public int getMaxLoadKg() {
        return maxLoadKg;
    }

    @Override
    protected String describeSpecifics() {
        return String.format("max load %d kg", maxLoadKg);
    }
}
