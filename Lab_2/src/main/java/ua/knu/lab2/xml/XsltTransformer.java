package ua.knu.lab2.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** XSL-перетворення XML-файлу. */
public final class XsltTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(XsltTransformer.class);

    private XsltTransformer() {
    }

    public static void transform(Path xml, Path xsl, Path output) throws XmlProcessingException {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer transformer = factory.newTransformer(new StreamSource(xsl.toFile()));
            Files.createDirectories(output.toAbsolutePath().getParent());
            try (OutputStream out = Files.newOutputStream(output)) {
                transformer.transform(new StreamSource(xml.toFile()), new StreamResult(out));
            }
            LOG.info("Transformed {} with {} into {}", xml, xsl, output);
        } catch (TransformerException | IOException e) {
            throw new XmlProcessingException("XSLT transformation failed: " + e.getMessage(), e);
        }
    }
}
