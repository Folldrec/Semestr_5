package ua.knu.lab2;

import java.nio.file.Path;

/** Шляхи до файлів, що використовуються в тестах. */
public final class TestPaths {
    public static final Path XML = Path.of("data/tariffs.xml");
    public static final Path XSD = Path.of("data/tariffs.xsd");
    public static final Path XSL = Path.of("data/tariffs-by-operator.xsl");

    private TestPaths() {
    }

    public static Path resource(String name) {
        return Path.of("src/test/resources", name);
    }
}
