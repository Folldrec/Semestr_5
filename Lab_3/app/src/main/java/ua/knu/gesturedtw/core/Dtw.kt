package ua.knu.gesturedtw.core

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/** Багатовимірний часовий ряд: кожен елемент — вектор (x, y, z). */
typealias Series = List<FloatArray>

/**
 * Dynamic Time Warping з обмеженням смугою Саке–Чиби.
 *
 * Локальна вартість — евклідова відстань між векторами,
 * результат нормується на (n + m), тому не залежить від довжини рядів.
 */
object Dtw {

    fun distance(a: Series, b: Series, bandRatio: Double = 0.2): Double {
        require(a.isNotEmpty() && b.isNotEmpty()) { "Series must not be empty" }
        val n = a.size
        val m = b.size
        val window = max((bandRatio * max(n, m)).toInt(), abs(n - m))

        var prev = DoubleArray(m + 1) { Double.POSITIVE_INFINITY }
        var cur = DoubleArray(m + 1) { Double.POSITIVE_INFINITY }
        prev[0] = 0.0

        for (i in 1..n) {
            cur.fill(Double.POSITIVE_INFINITY)
            val from = max(1, i - window)
            val to = min(m, i + window)
            for (j in from..to) {
                val cost = euclidean(a[i - 1], b[j - 1])
                cur[j] = cost + min(prev[j], min(prev[j - 1], cur[j - 1]))
            }
            val tmp = prev; prev = cur; cur = tmp
        }
        return prev[m] / (n + m)
    }

    private fun euclidean(p: FloatArray, q: FloatArray): Double {
        var sum = 0.0
        for (k in p.indices) {
            val d = (p[k] - q[k]).toDouble()
            sum += d * d
        }
        return sqrt(sum)
    }
}
