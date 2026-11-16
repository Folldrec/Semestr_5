package com.program.appliances.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.program.appliances.model.Appliance;
import com.program.appliances.model.Kettle;
import com.program.appliances.model.Refrigerator;
import com.program.appliances.model.Television;
import com.program.appliances.model.VacuumCleaner;
import com.program.appliances.model.WashingMachine;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import org.junit.jupiter.api.Test;

class ApplianceLoaderTest {

    private final ApplianceLoader loader = new ApplianceLoader();

    private List<Appliance> load(String text) throws IOException {
        return loader.load(new StringReader(text));
    }

    @Test
    void loadsAllTypesAndSkipsCommentsAndBlankLines() throws IOException {
        List<Appliance> result = load(String.join("\n",
                "# comment",
                "",
                "REFRIGERATOR;R;150;300;true",
                "WASHING_MACHINE;W;2200;8;false",
                "KETTLE;K;2000;1.7;true",
                "TELEVISION;T;100;43;false",
                "VACUUM_CLEANER;V;600;0.8;false"));

        assertEquals(5, result.size());
        assertInstanceOf(Refrigerator.class, result.get(0));
        assertInstanceOf(WashingMachine.class, result.get(1));
        assertInstanceOf(Kettle.class, result.get(2));
        assertInstanceOf(Television.class, result.get(3));
        assertInstanceOf(VacuumCleaner.class, result.get(4));
        assertTrue(result.get(0).isPluggedIn());
        assertFalse(result.get(1).isPluggedIn());
    }

    @Test
    void rejectsUnknownType() {
        assertThrows(IllegalArgumentException.class,
                () -> load("TOASTER;X;800;1;true"));
    }

    @Test
    void rejectsMalformedLine() {
        assertThrows(IllegalArgumentException.class,
                () -> load("KETTLE;K;2000"));
    }

    @Test
    void rejectsNonNumericPower() {
        assertThrows(NumberFormatException.class,
                () -> load("KETTLE;K;abc;1.5;true"));
    }
}
