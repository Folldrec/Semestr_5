package ua.knu.lab2.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ua.knu.lab2.model.Tariff;
import ua.knu.lab2.xml.XmlProcessingException;

/** Розбір за допомогою SAX. */
public class SaxTariffParser extends AbstractTariffParser {

    @Override
    protected List<Tariff> doParse(InputStream in) throws SAXException, ParserConfigurationException, java.io.IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        TariffHandler handler = new TariffHandler();
        factory.newSAXParser().parse(in, handler);
        return handler.tariffs;
    }

    private static final class TariffHandler extends DefaultHandler {
        private final List<Tariff> tariffs = new ArrayList<>();
        private final StringBuilder text = new StringBuilder();
        private TariffBuilder current;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attrs) {
            text.setLength(0);
            if (XmlTags.TARIFF.equals(qName)) {
                current = new TariffBuilder(attrs.getValue(XmlTags.ID));
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            text.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (XmlTags.TARIFF.equals(qName)) {
                try {
                    tariffs.add(current.build());
                } catch (XmlProcessingException e) {
                    throw new SAXException(e.getMessage(), e);
                }
            } else if (!text.toString().isBlank()) {
                current.accept(qName, text.toString());
            }
        }
    }
}
