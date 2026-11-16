package com.program.appliances.model;

/**
 * Base class for all household electrical appliances.
 */
public abstract class Appliance {

    private final String name;
    private final int powerWatts;
    private boolean pluggedIn;

    /**
     * Creates an appliance that is initially unplugged.
     *
     * @param name       appliance model name, must not be blank
     * @param powerWatts rated power in watts, must be positive
     */
    protected Appliance(String name, int powerWatts) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        if (powerWatts <= 0) {
            throw new IllegalArgumentException("Power must be positive");
        }
        this.name = name;
        this.powerWatts = powerWatts;
    }

    public String getName() {
        return name;
    }

    public int getPowerWatts() {
        return powerWatts;
    }

    public boolean isPluggedIn() {
        return pluggedIn;
    }

    public void plugIn() {
        pluggedIn = true;
    }

    public void unplug() {
        pluggedIn = false;
    }

    /**
     * Returns the power currently drawn from the mains.
     *
     * @return rated power if plugged in, otherwise zero
     */
    public int getConsumedPower() {
        return pluggedIn ? powerWatts : 0;
    }

    /**
     * Returns a description of the parameters specific to the subclass.
     *
     * @return human-readable specific parameters
     */
    protected abstract String describeSpecifics();

    @Override
    public String toString() {
        return String.format("%s '%s', %d W, %s, %s",
                getClass().getSimpleName(), name, powerWatts,
                describeSpecifics(), pluggedIn ? "plugged in" : "unplugged");
    }
}
