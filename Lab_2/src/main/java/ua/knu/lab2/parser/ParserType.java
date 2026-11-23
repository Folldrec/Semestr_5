package ua.knu.lab2.parser;

import java.util.function.Supplier;

/** Доступні реалізації парсера. */
public enum ParserType {
    SAX(SaxTariffParser::new),
    DOM(DomTariffParser::new),
    STAX(StaxTariffParser::new);

    private final Supplier<TariffParser> factory;

    ParserType(Supplier<TariffParser> factory) {
        this.factory = factory;
    }

    public TariffParser create() {
        return factory.get();
    }
}
