package ua.knu.lab2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MainTest {

    @TempDir
    Path tmp;

    private String[] args(Path xml, Path xsl, Path out) {
        return new String[] {xml.toString(), TestPaths.XSD.toString(), xsl.toString(), out.toString()};
    }

    @Test
    void runsFullPipeline() {
        Path out = tmp.resolve("result.xml");
        assertEquals(Main.EXIT_OK, Main.run(args(TestPaths.XML, TestPaths.XSL, out)));
        assertTrue(Files.exists(out));
    }

    @Test
    void invalidXmlStopsWithExitCode1() {
        Path out = tmp.resolve("result.xml");
        assertEquals(Main.EXIT_INVALID,
                Main.run(args(TestPaths.resource("negative-payroll.xml"), TestPaths.XSL, out)));
        assertTrue(Files.notExists(out));
    }

    @Test
    void processingErrorGivesExitCode2() {
        assertEquals(Main.EXIT_ERROR, Main.run(args(TestPaths.XML, TestPaths.resource("broken.xsl"), tmp.resolve("o.xml"))));
        assertEquals(Main.EXIT_ERROR, Main.run(args(tmp.resolve("missing.xml"), TestPaths.XSL, tmp.resolve("o.xml"))));
    }

    @Test
    void defaultArgumentsWork() {
        assertEquals(Main.EXIT_OK, Main.run(new String[0]));
    }

    @Test
    void mainDoesNotExitOnSuccess() {
        Main.main(args(TestPaths.XML, TestPaths.XSL, tmp.resolve("result.xml")));
    }
}
