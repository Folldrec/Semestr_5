package com.program.appliances.model;

/**
 * Vacuum cleaner with a given dust container volume.
 */
public class VacuumCleaner extends Appliance {

    private final double dustContainerLiters;

    /**
     * Creates a vacuum cleaner.
     *
     * @param name       model name
     * @param powerWatts rated power in watts
     * @param dustContainerLiters dust container volume in liters
     */
    public VacuumCleaner(String name, int powerWatts, double dustContainerLiters) {
        super(name, powerWatts);
        if (dustContainerLiters <= 0) {
            throw new IllegalArgumentException("dust container volume in liters must be positive");
        }
        this.dustContainerLiters = dustContainerLiters;
    }

    public double getDustContainerLiters() {
        return dustContainerLiters;
    }

    @Override
    protected String describeSpecifics() {
        return String.format("dust container %.2f L", dustContainerLiters);
    }
}
