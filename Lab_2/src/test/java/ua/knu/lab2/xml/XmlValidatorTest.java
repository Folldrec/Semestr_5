package ua.knu.lab2.xml;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ua.knu.lab2.TestPaths;

class XmlValidatorTest {

    @Test
    void sampleDocumentIsValid() throws Exception {
        assertTrue(XmlValidator.validate(TestPaths.XML, TestPaths.XSD).isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"negative-payroll.xml", "duplicate-id.xml", "bad-enum.xml",
            "missing-id.xml", "empty.xml", "malformed.xml"})
    void invalidDocumentsReportErrors(String file) throws Exception {
        List<String> errors = XmlValidator.validate(TestPaths.resource(file), TestPaths.XSD);
        assertFalse(errors.isEmpty(), file);
    }

    @Test
    void missingXmlFileFails() {
        assertThrows(XmlProcessingException.class,
                () -> XmlValidator.validate(Path.of("no-such-file.xml"), TestPaths.XSD));
    }

    @ParameterizedTest
    @ValueSource(strings = {"not-a-schema.xsd", "missing.xsd"})
    void brokenSchemaFails(String schema) {
        assertThrows(XmlProcessingException.class,
                () -> XmlValidator.validate(TestPaths.XML, TestPaths.resource(schema)));
    }
}
