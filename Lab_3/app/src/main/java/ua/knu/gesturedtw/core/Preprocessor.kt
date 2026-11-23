package ua.knu.gesturedtw.core

import kotlin.math.sqrt

/**
 * Перетворює сирі дані акселерометра на нормалізований ряд фіксованої довжини:
 *  1. прибирає гравітацію (високочастотний фільтр);
 *  2. ресемплює до [targetLength] точок за часом (лінійна інтерполяція);
 *  3. нормує амплітуду (RMS = 1), щоб слабкий і сильний рух одного жесту збігались.
 */
class Preprocessor(
    private val targetLength: Int = DEFAULT_LENGTH,
    private val gravityAlpha: Float = 0.9f,
) {

    /** @return `null`, якщо запис закороткий або майже нерухомий. */
    fun process(raw: List<RawSample>): Series? {
        if (raw.size < MIN_SAMPLES) return null
        val linear = removeGravity(raw)
        val resampled = resample(raw, linear)
        return normalize(resampled)
    }

    private fun removeGravity(raw: List<RawSample>): List<FloatArray> {
        var gx = raw[0].x
        var gy = raw[0].y
        var gz = raw[0].z
        return raw.map { s ->
            gx = gravityAlpha * gx + (1 - gravityAlpha) * s.x
            gy = gravityAlpha * gy + (1 - gravityAlpha) * s.y
            gz = gravityAlpha * gz + (1 - gravityAlpha) * s.z
            floatArrayOf(s.x - gx, s.y - gy, s.z - gz)
        }
    }

    private fun resample(raw: List<RawSample>, values: List<FloatArray>): List<FloatArray> {
        val t0 = raw.first().timeNs
        val duration = (raw.last().timeNs - t0).toDouble()
        if (duration <= 0.0) return emptyList()
        val out = ArrayList<FloatArray>(targetLength)
        var idx = 0
        for (k in 0 until targetLength) {
            val t = t0 + duration * k / (targetLength - 1)
            while (idx < raw.size - 2 && raw[idx + 1].timeNs < t) idx++
            val ta = raw[idx].timeNs.toDouble()
            val tb = raw[idx + 1].timeNs.toDouble()
            val f = if (tb > ta) ((t - ta) / (tb - ta)).coerceIn(0.0, 1.0).toFloat() else 0f
            val a = values[idx]
            val b = values[idx + 1]
            out += FloatArray(3) { c -> a[c] + (b[c] - a[c]) * f }
        }
        return out
    }

    private fun normalize(series: List<FloatArray>): Series? {
        if (series.isEmpty()) return null
        var sumSq = 0.0
        for (p in series) for (v in p) sumSq += v * v
        val rms = sqrt(sumSq / (series.size * 3))
        if (rms < MIN_RMS) return null
        val scale = (1.0 / rms).toFloat()
        return series.map { p -> FloatArray(3) { p[it] * scale } }
    }

    companion object {
        const val DEFAULT_LENGTH = 64
        const val MIN_SAMPLES = 10
        /** Нижче цього рівня (м/с²) вважаємо, що телефон не рухали. */
        const val MIN_RMS = 0.3
    }
}
