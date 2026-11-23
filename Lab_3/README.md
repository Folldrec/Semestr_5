# Gesture DTW (Лабораторна робота 3, варіант 3)

Android-застосунок (Kotlin), який визначає жест, яким переміщували смартфон, за допомогою
**акселерометра** та алгоритму **DTW** (Dynamic Time Warping).

## Як це працює
1. **Запис шаблону** - вводите назву, затискаєте кнопку «Записати», рухаєте телефон, відпускаєте.
2. **Обробка** (`Preprocessor`): видалення гравітації (high-pass) → ресемплінг до 64 точок за часом → нормування амплітуди (RMS = 1).
3. **Розпізнавання** (`GestureClassifier`): DTW (смуга Саке–Чиби 20 %, евклідова локальна вартість) до всіх шаблонів, обирається найближчий; якщо відстань > порога — «невідомий жест».

## Вимоги лабораторної
| Вимога | Реалізація |
|---|---|
| Kotlin | увесь код - Kotlin |
| Git + gitignore + commit-повідомлення | `.gitignore` для Android/Gradle, Conventional Commits |
| Logger | [Timber](https://github.com/JakeWharton/timber): `DebugTree` у debug, `ReleaseTree` (WARN+) у release; див. `GestureApp` |
| i18n | `values/strings.xml` (en), `values-uk/strings.xml` (uk), `locales_config.xml`, перемикач мови в меню через `AppCompatDelegate.setApplicationLocales` |

## Структура
```
app/src/main/java/ua/knu/gesturedtw/
├── core/    Dtw, Preprocessor, GestureClassifier (чиста логіка, без Android)
├── sensor/  AccelerometerRecorder
├── data/    TemplateStore (JSON)
└── ui/      MainActivity
app/src/test/  юніт-тести DTW та класифікатора (синтетичні жести з шумом)
```

## Збірка
Відкрити в Android Studio (JDK 17) → Run, або `./gradlew assembleDebug`; тести - `./gradlew test`.
Потрібен фізичний пристрій з акселерометром (в емуляторі - Virtual sensors).
