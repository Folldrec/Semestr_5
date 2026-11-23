package ua.knu.lab2.parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import ua.knu.lab2.model.Tariff;
import ua.knu.lab2.xml.XmlProcessingException;

/** Шаблонний метод: відкриття файлу, логування та обробка помилок спільні для всіх парсерів. */
abstract class AbstractTariffParser implements TariffParser {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public final List<Tariff> parse(Path xml) throws XmlProcessingException {
        log.info("Parsing {}", xml);
        try (InputStream in = Files.newInputStream(xml)) {
            List<Tariff> tariffs = doParse(in);
            log.info("Parsed {} tariffs", tariffs.size());
            return tariffs;
        } catch (IOException | SAXException | ParserConfigurationException | XMLStreamException e) {
            throw new XmlProcessingException("Cannot parse " + xml + ": " + e.getMessage(), e);
        }
    }

    protected abstract List<Tariff> doParse(InputStream in)
            throws IOException, SAXException, ParserConfigurationException, XMLStreamException, XmlProcessingException;
}
