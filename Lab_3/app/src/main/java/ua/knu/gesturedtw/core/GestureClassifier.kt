package ua.knu.gesturedtw.core

/** Еталонний жест, записаний користувачем. */
data class Template(val name: String, val series: Series)

/** Відстань DTW до найближчого шаблону жесту [name]. */
data class Match(val name: String, val distance: Double)

sealed interface Recognition {
    /** Немає жодного шаблону. */
    data object NoTemplates : Recognition

    /** Найближчий жест у межах порога. [ranking] відсортований за зростанням відстані. */
    data class Recognized(val best: Match, val ranking: List<Match>) : Recognition

    /** Найближчий жест надто далекий — рух не схожий на жоден шаблон. */
    data class Unknown(val closest: Match, val ranking: List<Match>) : Recognition
}

/** Класифікатор "найближчий сусід" на основі DTW. */
class GestureClassifier(private val threshold: Double = DEFAULT_THRESHOLD) {

    fun classify(sample: Series, templates: List<Template>): Recognition {
        if (templates.isEmpty()) return Recognition.NoTemplates

        val ranking = templates
            .groupBy { it.name }
            .map { (name, group) -> Match(name, group.minOf { Dtw.distance(sample, it.series) }) }
            .sortedBy { it.distance }

        val best = ranking.first()
        return if (best.distance <= threshold) {
            Recognition.Recognized(best, ranking)
        } else {
            Recognition.Unknown(best, ranking)
        }
    }

    companion object {
        /** Підібрано на синтетичних даних (див. GestureClassifierTest); для реальних жестів може потребувати налаштування. */
        const val DEFAULT_THRESHOLD = 0.9
    }
}
