package ua.knu.gesturedtw.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import timber.log.Timber
import ua.knu.gesturedtw.core.RawSample

/** Записує дані акселерометра між викликами [start] і [stop]. */
class AccelerometerRecorder(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val buffer = ArrayList<RawSample>()
    private var recording = false

    val isAvailable: Boolean get() = accelerometer != null

    fun start(): Boolean {
        if (accelerometer == null) {
            Timber.w("Accelerometer is not available on this device")
            return false
        }
        buffer.clear()
        recording = sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        Timber.d("Recording started (registered=%b)", recording)
        return recording
    }

    fun stop(): List<RawSample> {
        if (recording) sensorManager.unregisterListener(this)
        recording = false
        Timber.d("Recording stopped, %d samples", buffer.size)
        return buffer.toList()
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (!recording) return
        buffer += RawSample(event.timestamp, event.values[0], event.values[1], event.values[2])
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Timber.v("Accuracy changed: %d", accuracy)
    }
}
