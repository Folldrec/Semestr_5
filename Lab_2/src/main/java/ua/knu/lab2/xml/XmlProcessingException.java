package ua.knu.lab2.xml;

/** Помилка читання, розбору, валідації або перетворення XML. */
public class XmlProcessingException extends Exception {

    private static final long serialVersionUID = 1L;

    public XmlProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
