package ua.knu.lab2.parser;

import java.nio.file.Path;
import java.util.List;
import ua.knu.lab2.model.Tariff;
import ua.knu.lab2.xml.XmlProcessingException;

/** Розбирає XML-файл у колекцію тарифів. */
public interface TariffParser {

    List<Tariff> parse(Path xml) throws XmlProcessingException;
}
