package com.program.appliances;

import com.program.appliances.io.ApplianceLoader;
import com.program.appliances.model.Appliance;
import com.program.appliances.service.Apartment;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Console entry point: loads appliances, prints power consumption,
 * sorted list and search result.
 */
public final class ApplianceApp {

    private static final String CONFIG_FILE = "/appliances.csv";
    private static final int DEFAULT_MIN_WATTS = 500;
    private static final int DEFAULT_MAX_WATTS = 2000;

    private ApplianceApp() {
    }

    /**
     * Program entry point.
     *
     * @param args optional: min and max power for the search
     * @throws IOException if the configuration cannot be read
     */
    public static void main(String[] args) throws IOException {
        int min = args.length >= 2 ? Integer.parseInt(args[0]) : DEFAULT_MIN_WATTS;
        int max = args.length >= 2 ? Integer.parseInt(args[1]) : DEFAULT_MAX_WATTS;

        Apartment apartment = new Apartment();
        try (InputStream in = ApplianceApp.class.getResourceAsStream(CONFIG_FILE)) {
            if (in == null) {
                throw new IOException("Missing resource " + CONFIG_FILE);
            }
            new ApplianceLoader()
                    .load(new InputStreamReader(in, StandardCharsets.UTF_8))
                    .forEach(apartment::addAppliance);
        }

        System.out.println("Total consumed power: "
                + apartment.calculateConsumedPower() + " W");

        apartment.sortByPower();
        System.out.println("Appliances sorted by power:");
        apartment.getAppliances().forEach(System.out::println);

        System.out.printf("Appliances with power in [%d, %d] W:%n", min, max);
        for (Appliance a : apartment.findByPowerRange(min, max)) {
            System.out.println(a);
        }
    }
}
