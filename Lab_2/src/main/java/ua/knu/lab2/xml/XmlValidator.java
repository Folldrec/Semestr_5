package ua.knu.lab2.xml;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/** Валідація XML-документа за XSD. */
public final class XmlValidator {

    private static final Logger LOG = LoggerFactory.getLogger(XmlValidator.class);

    private XmlValidator() {
    }

    /**
     * Перевіряє XML за схемою.
     *
     * @return список повідомлень про помилки; порожній, якщо документ валідний
     */
    public static List<String> validate(Path xml, Path xsd) throws XmlProcessingException {
        Schema schema = loadSchema(xsd);
        CollectingErrorHandler handler = new CollectingErrorHandler();
        Validator validator = schema.newValidator();
        validator.setErrorHandler(handler);
        try {
            validator.validate(new StreamSource(xml.toFile()));
        } catch (SAXException e) {
            LOG.debug("Validation stopped: {}", e.getMessage());
        } catch (IOException e) {
            throw new XmlProcessingException("Cannot read XML file " + xml, e);
        }
        if (handler.errors.isEmpty()) {
            LOG.info("{} is valid against {}", xml, xsd);
        } else {
            handler.errors.forEach(err -> LOG.error("Validation error in {}: {}", xml, err));
        }
        return handler.errors;
    }

    private static Schema loadSchema(Path xsd) throws XmlProcessingException {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            return factory.newSchema(xsd.toFile());
        } catch (SAXException e) {
            throw new XmlProcessingException("Cannot load schema " + xsd, e);
        }
    }

    private static final class CollectingErrorHandler implements ErrorHandler {
        private final List<String> errors = new ArrayList<>();

        @Override
        public void warning(SAXParseException e) {
            LOG.warn("Validation warning: {}", e.getMessage());
        }

        @Override
        public void error(SAXParseException e) {
            errors.add(format(e));
        }

        @Override
        public void fatalError(SAXParseException e) throws SAXException {
            errors.add(format(e));
            throw e;
        }

        private static String format(SAXParseException e) {
            return "line " + e.getLineNumber() + ": " + e.getMessage();
        }
    }
}
