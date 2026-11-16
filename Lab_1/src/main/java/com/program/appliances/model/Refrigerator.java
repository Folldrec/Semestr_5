package com.program.appliances.model;

/**
 * Refrigerator with a given useful volume.
 */
public class Refrigerator extends Appliance {

    private final int volumeLiters;

    /**
     * Creates a refrigerator.
     *
     * @param name       model name
     * @param powerWatts rated power in watts
     * @param volumeLiters volume in liters
     */
    public Refrigerator(String name, int powerWatts, int volumeLiters) {
        super(name, powerWatts);
        if (volumeLiters <= 0) {
            throw new IllegalArgumentException("volume in liters must be positive");
        }
        this.volumeLiters = volumeLiters;
    }

    public int getVolumeLiters() {
        return volumeLiters;
    }

    @Override
    protected String describeSpecifics() {
        return String.format("volume %d L", volumeLiters);
    }
}
