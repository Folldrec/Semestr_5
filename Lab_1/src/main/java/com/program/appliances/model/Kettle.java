package com.program.appliances.model;

/**
 * Electric kettle with a given capacity.
 */
public class Kettle extends Appliance {

    private final double capacityLiters;

    /**
     * Creates a kettle.
     *
     * @param name       model name
     * @param powerWatts rated power in watts
     * @param capacityLiters capacity in liters
     */
    public Kettle(String name, int powerWatts, double capacityLiters) {
        super(name, powerWatts);
        if (capacityLiters <= 0) {
            throw new IllegalArgumentException("capacity in liters must be positive");
        }
        this.capacityLiters = capacityLiters;
    }

    public double getCapacityLiters() {
        return capacityLiters;
    }

    @Override
    protected String describeSpecifics() {
        return String.format("capacity %.2f L", capacityLiters);
    }
}
