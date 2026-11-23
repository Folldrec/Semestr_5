package ua.knu.lab2.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import ua.knu.lab2.model.Tariff;
import ua.knu.lab2.xml.XmlProcessingException;

/** Розбір за допомогою StAX (курсорний API). */
public class StaxTariffParser extends AbstractTariffParser {

    @Override
    protected List<Tariff> doParse(InputStream in) throws XMLStreamException, XmlProcessingException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        XMLStreamReader reader = factory.createXMLStreamReader(in);

        List<Tariff> tariffs = new ArrayList<>();
        TariffBuilder current = null;
        String tag = null;
        try {
            while (reader.hasNext()) {
                switch (reader.next()) {
                    case XMLStreamConstants.START_ELEMENT -> {
                        tag = reader.getLocalName();
                        if (XmlTags.TARIFF.equals(tag)) {
                            current = new TariffBuilder(reader.getAttributeValue(null, XmlTags.ID));
                        }
                    }
                    case XMLStreamConstants.CHARACTERS -> {
                        if (current != null && !reader.isWhiteSpace()) {
                            current.accept(tag, reader.getText());
                        }
                    }
                    case XMLStreamConstants.END_ELEMENT -> {
                        if (XmlTags.TARIFF.equals(reader.getLocalName())) {
                            tariffs.add(current.build());
                        }
                    }
                    default -> { }
                }
            }
        } finally {
            reader.close();
        }
        return tariffs;
    }
}
