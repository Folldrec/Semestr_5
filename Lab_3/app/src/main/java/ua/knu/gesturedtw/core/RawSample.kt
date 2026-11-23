package ua.knu.gesturedtw.core

/** Одне "сире" вимірювання акселерометра. [timeNs] — event.timestamp у наносекундах. */
data class RawSample(val timeNs: Long, val x: Float, val y: Float, val z: Float)
