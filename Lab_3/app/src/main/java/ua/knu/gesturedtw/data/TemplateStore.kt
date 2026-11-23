package ua.knu.gesturedtw.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import ua.knu.gesturedtw.core.Series
import ua.knu.gesturedtw.core.Template
import java.io.File

/** Зберігає шаблони жестів у JSON-файлі у внутрішньому сховищі застосунку. */
class TemplateStore(context: Context) {

    private val file = File(context.filesDir, "templates.json")

    fun load(): MutableList<Template> {
        if (!file.exists()) return mutableListOf()
        return try {
            val arr = JSONArray(file.readText())
            MutableList(arr.length()) { i -> fromJson(arr.getJSONObject(i)) }
        } catch (e: Exception) {
            Timber.e(e, "Failed to read templates, starting with an empty list")
            mutableListOf()
        }
    }

    fun save(templates: List<Template>) {
        val arr = JSONArray()
        templates.forEach { arr.put(toJson(it)) }
        file.writeText(arr.toString())
        Timber.d("Saved %d templates", templates.size)
    }

    private fun toJson(t: Template) = JSONObject().apply {
        put("name", t.name)
        val pts = JSONArray()
        t.series.forEach { p -> pts.put(JSONArray(p.map { it.toDouble() })) }
        put("points", pts)
    }

    private fun fromJson(o: JSONObject): Template {
        val pts = o.getJSONArray("points")
        val series: Series = List(pts.length()) { i ->
            val p = pts.getJSONArray(i)
            FloatArray(3) { p.getDouble(it).toFloat() }
        }
        return Template(o.getString("name"), series)
    }
}
