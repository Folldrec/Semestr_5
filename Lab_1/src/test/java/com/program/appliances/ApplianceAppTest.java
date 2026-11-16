package com.program.appliances;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

class ApplianceAppTest {

    @Test
    void printsTotalPowerSortedListAndSearchResult() throws IOException {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true));
        try {
            ApplianceApp.main(new String[] {"1000", "2500"});
        } finally {
            System.setOut(original);
        }
        String output = buffer.toString();
        assertTrue(output.contains("Total consumed power: 4270 W"));
        assertTrue(output.contains("sorted by power"));
        assertTrue(output.contains("[1000, 2500] W"));
    }
}
