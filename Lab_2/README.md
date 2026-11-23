# Лабораторна робота 2 — Тарифи мобільних компаній (варіант 3)

XML + XSD, розбір SAX/DOM/StAX, сортування через `Comparator`, валідація за XSD,
XSL-перетворення (групування тарифів за оператором), логування (SLF4J + Logback), JUnit 5 + JaCoCo.

## Структура
- `data/tariffs.xml`, `data/tariffs.xsd` — документ і схема (корінь `Tariffs`, елементи `Tariff`)
- `data/tariffs-by-operator.xsl` — перетворення: список → підсписки за `OperatorName`
- `ua.knu.lab2.model` — клас-модель (`Tariff`, `CallPrices`, `Parameters`, `Tarification`)
- `ua.knu.lab2.parser` — парсери (`SaxTariffParser`, `DomTariffParser`, `StaxTariffParser`);
  спільна логіка в `AbstractTariffParser` і `TariffBuilder`
- `ua.knu.lab2.xml` — `XmlValidator`, `XsltTransformer`
- `ua.knu.lab2.Main` — валідація → розбір трьома парсерами → сортування → XSLT

## Запуск
```
mvn verify                       # збірка, тести, звіт JaCoCo (target/site/jacoco)
mvn exec:java                    # запуск із файлами за замовчуванням
mvn exec:java -Dexec.args="data/tariffs.xml data/tariffs.xsd data/tariffs-by-operator.xsl output/result.xml"
```
Результат перетворення: `output/tariffs-by-operator.xml`, лог: `logs/app.log`.
