package ua.knu.gesturedtw

import android.app.Application
import android.util.Log
import timber.log.Timber

class GestureApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Debug: усі повідомлення з тегом "Клас.метод"; release: лише WARN і вище.
        Timber.plant(if (BuildConfig.DEBUG) Timber.DebugTree() else ReleaseTree())
        Timber.i("Application started, version %s", BuildConfig.VERSION_NAME)
    }

    private class ReleaseTree : Timber.Tree() {
        override fun isLoggable(tag: String?, priority: Int) = priority >= Log.WARN

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            Log.println(priority, tag ?: "GestureDtw", if (t != null) "$message\n${Log.getStackTraceString(t)}" else message)
        }
    }
}
