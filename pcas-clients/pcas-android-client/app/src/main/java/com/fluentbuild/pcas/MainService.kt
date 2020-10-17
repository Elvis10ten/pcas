package com.fluentbuild.pcas

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.fluentbuild.pcas.helpers.GeneralNotifications

class MainService: Service() {

    private lateinit var notifications: GeneralNotifications
    
    override fun onCreate() {
        super.onCreate()
        notifications = GeneralNotifications(this)
        startForeground(GeneralNotifications.ID, notifications.buildNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent?.getBooleanExtra(EXTRA_DRY_START, false) != true) {
            startEngine()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        stopForeground(true)
        stopEngine()
        super.onDestroy()
    }

    private fun startEngine() {
        try {
            notifications.notify(R.string.notificationRunningMsg)
            appComponent.engine.run()
        } catch (t: Throwable) {
            notifications.notify(R.string.notificationErrorMsg)
            stopEngine()
            stopSelf()
        }
    }
    
    private fun stopEngine() {
        appComponent.engine.stop()
    }

    override fun onBind(intent: Intent): IBinder? = null
    
    companion object {

        private const val EXTRA_DRY_START = "EXTRA_DRY_START"

        fun start(context: Context, dryStart: Boolean) {
            getBaseIntent(context).apply {
                putExtra(EXTRA_DRY_START, dryStart)
                context.startForegroundService(this)
            }
        }

        fun stop(context: Context, interrupt: Boolean) {
            val engineStatus = context.appComponent.appStateObservable.currentAppState.engineStatus
            if(interrupt || (!interrupt && engineStatus == Engine.Status.IDLE)) {
                context.stopService(getBaseIntent(context))
            }
        }

        private fun getBaseIntent(context: Context) = Intent(context, MainService::class.java)
    }
}