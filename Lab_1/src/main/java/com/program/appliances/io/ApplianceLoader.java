package com.program.appliances.io;

import com.program.appliances.model.Appliance;
import com.program.appliances.model.Kettle;
import com.program.appliances.model.Refrigerator;
import com.program.appliances.model.Television;
import com.program.appliances.model.VacuumCleaner;
import com.program.appliances.model.WashingMachine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads appliances from a semicolon-separated text source.
 *
 * <p>Line format: {@code TYPE;NAME;POWER_WATTS;SPECIFIC;PLUGGED_IN}.
 * Empty lines and lines starting with '#' are ignored.
 */
public class ApplianceLoader {

    private static final int FIELD_COUNT = 5;

    /**
     * Loads appliances from the given reader.
     *
     * @param reader source of appliance lines
     * @return loaded appliances
     * @throws IOException              if reading fails
     * @throws IllegalArgumentException if a line is malformed
     */
    public List<Appliance> load(Reader reader) throws IOException {
        List<Appliance> result = new ArrayList<>();
        BufferedReader buffered = new BufferedReader(reader);
        String line;
        while ((line = buffered.readLine()) != null) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                result.add(parseLine(trimmed));
            }
        }
        return result;
    }

    private Appliance parseLine(String line) {
        String[] parts = line.split(";");
        if (parts.length != FIELD_COUNT) {
            throw new IllegalArgumentException("Malformed line: " + line);
        }
        String name = parts[1].trim();
        int power = Integer.parseInt(parts[2].trim());
        String specific = parts[3].trim();
        Appliance appliance = switch (parts[0].trim()) {
            case "REFRIGERATOR" ->
                    new Refrigerator(name, power, Integer.parseInt(specific));
            case "WASHING_MACHINE" ->
                    new WashingMachine(name, power, Integer.parseInt(specific));
            case "KETTLE" ->
                    new Kettle(name, power, Double.parseDouble(specific));
            case "TELEVISION" ->
                    new Television(name, power, Integer.parseInt(specific));
            case "VACUUM_CLEANER" ->
                    new VacuumCleaner(name, power, Double.parseDouble(specific));
            default -> throw new IllegalArgumentException(
                    "Unknown appliance type: " + parts[0]);
        };
        if (Boolean.parseBoolean(parts[4].trim())) {
            appliance.plugIn();
        }
        return appliance;
    }
}
