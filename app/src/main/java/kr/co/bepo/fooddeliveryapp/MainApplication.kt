package kr.co.bepo.fooddeliveryapp

import android.app.Application
import android.content.Context
import kr.co.bepo.fooddeliveryapp.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication : Application() {

    companion object {
        var appContext: Context? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this

        startKoin {
            androidLogger(
                if (BuildConfig.DEBUG) Level.DEBUG
                else Level.NONE
            )
            androidContext(this@MainApplication)
            modules(appModule + dataModule + domainModule + presenterModule + utilModule)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        appContext = null
    }
}