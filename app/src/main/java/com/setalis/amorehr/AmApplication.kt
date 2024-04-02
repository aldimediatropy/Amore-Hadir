package com.setalis.amorehr

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.lifecycle.LifecycleObserver
import androidx.work.Configuration
import com.setalis.amorehr.applications.AppModule
import com.setalis.amorehr.commons.managers.SessionManager
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

/**
 * Crafted by Al (ismealdi) on 04/02/24.
 * Setalis Digital
 */

@SuppressLint("Registered, StaticFieldLeak")
class AmApplication : Application(), LifecycleObserver, Configuration.Provider {

    private val session: SessionManager by inject()

    companion object {
        var mContext: Context? = null
        const val CHANNEL_ID = "hr_setalis"
    }

    override fun onCreate() {
        super.onCreate()
        setKoin()

        mContext = this.applicationContext
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Amore Notification",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()


    private fun setKoin() {
        startKoin {
            this.androidContext(this@AmApplication)
            this.androidLogger(Level.DEBUG)
            this.androidFileProperties()

            modules(AppModule.getModule()).also {
                AppModule.loadNeeded = false
            }
        }
    }
}