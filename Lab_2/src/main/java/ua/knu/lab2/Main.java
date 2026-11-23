package ua.knu.lab2;

import java.nio.file.Path;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.knu.lab2.model.Tariff;
import ua.knu.lab2.parser.ParserType;
import ua.knu.lab2.xml.XmlProcessingException;
import ua.knu.lab2.xml.XmlValidator;
import ua.knu.lab2.xml.XsltTransformer;

/**
 * Точка входу.
 * Аргументи (необов'язкові): xml xsd xsl output.
 */
public final class Main {

    static final int EXIT_OK = 0;
    static final int EXIT_INVALID = 1;
    static final int EXIT_ERROR = 2;

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private Main() {
    }

    public static void main(String[] args) {
        int code = run(args);
        if (code != EXIT_OK) {
            System.exit(code);
        }
    }

    static int run(String[] args) {
        Path xml = Path.of(arg(args, 0, "data/tariffs.xml"));
        Path xsd = Path.of(arg(args, 1, "data/tariffs.xsd"));
        Path xsl = Path.of(arg(args, 2, "data/tariffs-by-operator.xsl"));
        Path output = Path.of(arg(args, 3, "output/tariffs-by-operator.xml"));
        try {
            List<String> errors = XmlValidator.validate(xml, xsd);
            if (!errors.isEmpty()) {
                LOG.error("{} is not valid ({} errors), stopping", xml, errors.size());
                return EXIT_INVALID;
            }
            for (ParserType type : ParserType.values()) {
                List<Tariff> tariffs = type.create().parse(xml);
                tariffs.sort(Tariff.BY_PAYROLL);
                LOG.info("[{}] tariffs sorted by payroll:", type);
                tariffs.forEach(t -> LOG.info("  {} {} / {} - {} UAH", t.id(), t.operatorName(), t.name(), t.payroll()));
            }
            XsltTransformer.transform(xml, xsl, output);
            return EXIT_OK;
        } catch (XmlProcessingException e) {
            LOG.error("Processing failed: {}", e.getMessage(), e);
            return EXIT_ERROR;
        }
    }

    private static String arg(String[] args, int index, String defaultValue) {
        return args.length > index ? args[index] : defaultValue;
    }
}
