package ua.knu.lab2.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ua.knu.lab2.TestPaths;

class XsltTransformerTest {

    @TempDir
    Path tmp;

    @Test
    void groupsTariffsByOperator() throws Exception {
        Path out = tmp.resolve("nested/dir/out.xml");
        XsltTransformer.transform(TestPaths.XML, TestPaths.XSL, out);

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(out.toFile());
        NodeList operators = doc.getElementsByTagName("Operator");
        assertEquals(3, operators.getLength());
        assertEquals("Kyivstar", ((Element) operators.item(0)).getAttribute("name"));
        assertEquals("lifecell", ((Element) operators.item(1)).getAttribute("name"));
        assertEquals("Vodafone", ((Element) operators.item(2)).getAttribute("name"));
        for (int i = 0; i < operators.getLength(); i++) {
            assertEquals(2, ((Element) operators.item(i)).getElementsByTagName("Tariff").getLength());
        }
        assertEquals(0, doc.getElementsByTagName("OperatorName").getLength());
    }

    @Test
    void resultIsValidXmlWithTariffsSortedByPayroll() throws Exception {
        Path out = tmp.resolve("out.xml");
        XsltTransformer.transform(TestPaths.XML, TestPaths.XSL, out);
        String text = Files.readString(out);
        assertTrue(text.indexOf("T001") < text.indexOf("T002"));
        assertTrue(text.indexOf("T003") < text.indexOf("T004"));
    }

    @Test
    void brokenStylesheetFails() {
        assertThrows(XmlProcessingException.class, () -> XsltTransformer.transform(
                TestPaths.XML, TestPaths.resource("broken.xsl"), tmp.resolve("out.xml")));
    }

    @Test
    void missingInputFails() {
        assertThrows(XmlProcessingException.class, () -> XsltTransformer.transform(
                Path.of("missing.xml"), TestPaths.XSL, tmp.resolve("out.xml")));
    }

    @Test
    void unwritableOutputFails() throws Exception {
        Path file = Files.createFile(tmp.resolve("file"));
        assertThrows(XmlProcessingException.class, () -> XsltTransformer.transform(
                TestPaths.XML, TestPaths.XSL, file.resolve("out.xml")));
    }
}
