package kr.co.bepo.fooddeliveryapp

import android.app.Application
import android.content.Context

class MainApplication : Application() {

    companion object {
        var appContext: Context? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    override fun onTerminate() {
        super.onTerminate()
        appContext = null
    }
}