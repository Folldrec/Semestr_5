package ua.knu.gesturedtw.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Random
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin

class GestureClassifierTest {

    private val pre = Preprocessor()
    private val classifier = GestureClassifier()

    /** Генерує сирий запис жесту з нерівномірним темпом, шумом і гравітацією. */
    private fun record(shape: (Double) -> DoubleArray, seed: Long, amp: Double = 1.0, n: Int = 120): List<RawSample> {
        val rnd = Random(seed)
        val speed = 0.8 + rnd.nextDouble() * 0.4
        return (0 until n).map { i ->
            val u = i / (n - 1.0)
            val t = Math.pow(u, speed) // нерівномірний темп
            val v = shape(t)
            RawSample(
                timeNs = i * 10_000_000L,
                x = (amp * v[0] + rnd.nextGaussian() * 0.15).toFloat(),
                y = (amp * v[1] + rnd.nextGaussian() * 0.15).toFloat(),
                z = (9.81 + amp * v[2] + rnd.nextGaussian() * 0.15).toFloat(),
            )
        }
    }

    private val circle = { t: Double -> doubleArrayOf(5 * cos(2 * PI * t), 5 * sin(2 * PI * t), 0.0) }
    private val shake = { t: Double -> doubleArrayOf(8 * sin(6 * PI * t), 0.0, 0.0) }
    private val swipeUp = { t: Double -> doubleArrayOf(0.0, 9 * exp(-80 * (t - 0.5) * (t - 0.5)), 0.0) }

    private fun series(shape: (Double) -> DoubleArray, seed: Long, amp: Double = 1.0): Series {
        val s = pre.process(record(shape, seed, amp))
        assertNotNull(s)
        return s!!
    }

    private val templates = listOf(
        Template("circle", series(circle, 1)),
        Template("shake", series(shake, 2)),
        Template("swipe", series(swipeUp, 3)),
    )

    @Test
    fun dtwOfIdenticalSeriesIsZero() {
        val s = series(circle, 1)
        assertEquals(0.0, Dtw.distance(s, s), 1e-9)
    }

    @Test
    fun dtwIsSymmetric() {
        val a = series(circle, 1)
        val b = series(shake, 2)
        assertEquals(Dtw.distance(a, b), Dtw.distance(b, a), 1e-9)
    }

    @Test
    fun dtwHandlesDifferentLengths() {
        val a = listOf(floatArrayOf(0f, 0f, 0f), floatArrayOf(1f, 0f, 0f), floatArrayOf(2f, 0f, 0f))
        val b = listOf(
            floatArrayOf(0f, 0f, 0f), floatArrayOf(0f, 0f, 0f), floatArrayOf(1f, 0f, 0f),
            floatArrayOf(1f, 0f, 0f), floatArrayOf(2f, 0f, 0f),
        )
        assertEquals(0.0, Dtw.distance(a, b), 1e-9)
    }

    @Test
    fun recognizesEachGestureWithNoiseAndDifferentSpeedAndAmplitude() {
        for ((name, shape) in listOf("circle" to circle, "shake" to shake, "swipe" to swipeUp)) {
            for (seed in 10L..19L) {
                val sample = series(shape, seed, amp = 0.6 + (seed % 5) * 0.2)
                val result = classifier.classify(sample, templates)
                assertTrue("$name seed=$seed -> $result", result is Recognition.Recognized)
                assertEquals("seed=$seed", name, (result as Recognition.Recognized).best.name)
            }
        }
    }

    @Test
    fun rejectsUnrelatedMotion() {
        val rnd = Random(99)
        val noise = { _: Double -> doubleArrayOf(rnd.nextGaussian() * 5, rnd.nextGaussian() * 5, rnd.nextGaussian() * 5) }
        val sample = series(noise, 5)
        assertTrue(classifier.classify(sample, templates) is Recognition.Unknown)
    }

    @Test
    fun reportsNoTemplates() {
        assertEquals(Recognition.NoTemplates, classifier.classify(series(circle, 1), emptyList()))
    }

    @Test
    fun preprocessorRejectsStillPhoneAndShortRecordings() {
        val still = (0 until 100).map { RawSample(it * 10_000_000L, 0f, 0f, 9.81f) }
        assertNull(pre.process(still))
        assertNull(pre.process(still.take(3)))
    }

    @Test
    fun preprocessorProducesFixedLength() {
        assertEquals(Preprocessor.DEFAULT_LENGTH, series(circle, 1).size)
    }
}
