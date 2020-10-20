package com.fluentbuild.pcas

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

class MainService: Service() {

    private lateinit var notifications: GeneralNotifications
    
    override fun onCreate() {
        super.onCreate()
        notifications = GeneralNotifications(this)
        startForeground(GeneralNotifications.ID, notifications.buildNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val startEngine = intent?.getBooleanExtra(EXTRA_START_ENGINE, false)
        if(startEngine != false) {
            startEngine()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        shutdown()
        super.onDestroy()
    }

    private fun startEngine() {
        try {
            appComponent.engine.stop()
            notifications.update(getString(R.string.notificationRunningMsg))
            appComponent.engine.run()
        } catch (t: Throwable) {
            t.printStackTrace()
            notifications.update(getString(R.string.notificationErrorMsg))
            shutdown()
        }
    }
    
    private fun shutdown() {
        appComponent.engine.stop()
        stopForeground(true)
        stopSelf()
    }

    override fun onBind(intent: Intent): IBinder? = null
    
    companion object {

        private const val EXTRA_START_ENGINE = "EXTRA_START_ENGINE"

        fun dryStart(context: Context) {
            getBaseIntent(context).apply {
                putExtra(EXTRA_START_ENGINE, false)
                context.startForegroundService(this)
            }
        }

        fun startEngine(context: Context) {
            getBaseIntent(context).apply {
                putExtra(EXTRA_START_ENGINE, true)
                context.startForegroundService(this)
            }
        }

        fun stopEngine(context: Context) {
            context.stopService(getBaseIntent(context))
        }

        private fun getBaseIntent(context: Context) = Intent(context, MainService::class.java)
    }
}