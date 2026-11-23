package ua.knu.gesturedtw.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import timber.log.Timber
import ua.knu.gesturedtw.R
import ua.knu.gesturedtw.core.GestureClassifier
import ua.knu.gesturedtw.core.Preprocessor
import ua.knu.gesturedtw.core.Recognition
import ua.knu.gesturedtw.core.Template
import ua.knu.gesturedtw.data.TemplateStore
import ua.knu.gesturedtw.sensor.AccelerometerRecorder
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var recorder: AccelerometerRecorder
    private lateinit var store: TemplateStore
    private val preprocessor = Preprocessor()
    private val classifier = GestureClassifier()
    private val templates = mutableListOf<Template>()

    private lateinit var nameInput: EditText
    private lateinit var resultText: TextView
    private lateinit var listAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recorder = AccelerometerRecorder(this)
        store = TemplateStore(this)
        templates += store.load()

        nameInput = findViewById(R.id.gestureName)
        resultText = findViewById(R.id.resultText)
        val list = findViewById<ListView>(R.id.templateList)
        listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        list.adapter = listAdapter
        list.emptyView = findViewById<View>(R.id.emptyText)
        list.setOnItemLongClickListener { _, _, position, _ -> confirmDelete(position); true }
        refreshList()

        if (!recorder.isAvailable) {
            resultText.setText(R.string.no_accelerometer)
            Timber.w("No accelerometer, recording buttons disabled")
        }
        bindHoldButton(findViewById(R.id.recordButton), ::onTemplateRecorded)
        bindHoldButton(findViewById(R.id.recognizeButton), ::onRecognizeRecorded)
        Timber.d("MainActivity created with %d templates", templates.size)
    }

    /** Кнопка "затисни і рухай телефон": запис триває, поки кнопка затиснута. */
    @SuppressLint("ClickableViewAccessibility")
    private fun bindHoldButton(button: Button, onFinished: (List<ua.knu.gesturedtw.core.RawSample>) -> Unit) {
        button.isEnabled = recorder.isAvailable
        button.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (button.id == R.id.recordButton && nameInput.text.isBlank()) {
                        nameInput.error = getString(R.string.error_name_required)
                    } else if (recorder.start()) {
                        resultText.setText(R.string.recording)
                    }
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    val raw = recorder.stop()
                    if (raw.isNotEmpty()) onFinished(raw)
                    v.performClick()
                    true
                }
                else -> false
            }
        }
    }

    private fun onTemplateRecorded(raw: List<ua.knu.gesturedtw.core.RawSample>) {
        val series = preprocessor.process(raw)
        if (series == null) {
            Timber.w("Template rejected: %d samples, too short or too still", raw.size)
            resultText.setText(R.string.error_too_short)
            return
        }
        val name = nameInput.text.toString().trim()
        templates += Template(name, series)
        store.save(templates)
        refreshList()
        Timber.i("Template '%s' added", name)
        resultText.text = getString(R.string.template_saved, name)
    }

    private fun onRecognizeRecorded(raw: List<ua.knu.gesturedtw.core.RawSample>) {
        val series = preprocessor.process(raw)
        if (series == null) {
            Timber.w("Recognition skipped: %d samples, too short or too still", raw.size)
            resultText.setText(R.string.error_too_short)
            return
        }
        val result = classifier.classify(series, templates)
        Timber.i("Recognition result: %s", result)
        resultText.text = when (result) {
            Recognition.NoTemplates -> getString(R.string.no_templates)
            is Recognition.Recognized -> getString(
                R.string.recognized, result.best.name, result.best.distance
            ) + "\n" + ranking(result.ranking)
            is Recognition.Unknown -> getString(
                R.string.unknown_gesture, result.closest.name, result.closest.distance
            ) + "\n" + ranking(result.ranking)
        }
    }

    private fun ranking(r: List<ua.knu.gesturedtw.core.Match>) =
        r.joinToString("\n") { String.format(Locale.getDefault(), "%s — %.3f", it.name, it.distance) }

    private fun refreshList() {
        listAdapter.clear()
        listAdapter.addAll(templates.map { it.name })
    }

    private fun confirmDelete(position: Int) {
        val t = templates[position]
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.delete_confirm, t.name))
            .setPositiveButton(R.string.delete) { _, _ ->
                templates.removeAt(position)
                store.save(templates)
                refreshList()
                Timber.i("Template '%s' deleted", t.name)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val tag = when (item.itemId) {
            R.id.lang_uk -> "uk"
            R.id.lang_en -> "en"
            R.id.lang_system -> ""
            else -> return super.onOptionsItemSelected(item)
        }
        Timber.i("Language changed to '%s'", tag.ifEmpty { "system" })
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
        return true
    }
}
