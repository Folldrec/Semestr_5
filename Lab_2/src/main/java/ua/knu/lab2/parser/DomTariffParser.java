package ua.knu.lab2.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ua.knu.lab2.model.Tariff;
import ua.knu.lab2.xml.XmlProcessingException;

/** Розбір за допомогою DOM. */
public class DomTariffParser extends AbstractTariffParser {

    @Override
    protected List<Tariff> doParse(InputStream in)
            throws IOException, SAXException, ParserConfigurationException, XmlProcessingException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        Document document = factory.newDocumentBuilder().parse(in);

        NodeList nodes = document.getElementsByTagName(XmlTags.TARIFF);
        List<Tariff> tariffs = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            TariffBuilder builder = new TariffBuilder(element.getAttribute(XmlTags.ID));
            collectLeaves(element, builder);
            tariffs.add(builder.build());
        }
        return tariffs;
    }

    /** Передає будівельнику всі листові елементи піддерева. */
    private static void collectLeaves(Element parent, TariffBuilder builder) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element element = (Element) child;
            if (element.getElementsByTagName("*").getLength() == 0) {
                builder.accept(element.getTagName(), element.getTextContent());
            } else {
                collectLeaves(element, builder);
            }
        }
    }
}
