package com.program.appliances.model;

/**
 * Television with a given screen diagonal.
 */
public class Television extends Appliance {

    private final int screenInches;

    /**
     * Creates a television.
     *
     * @param name       model name
     * @param powerWatts rated power in watts
     * @param screenInches screen diagonal in inches
     */
    public Television(String name, int powerWatts, int screenInches) {
        super(name, powerWatts);
        if (screenInches <= 0) {
            throw new IllegalArgumentException("screen diagonal in inches must be positive");
        }
        this.screenInches = screenInches;
    }

    public int getScreenInches() {
        return screenInches;
    }

    @Override
    protected String describeSpecifics() {
        return String.format("screen %d inches", screenInches);
    }
}
