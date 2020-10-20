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
        val power = intent?.getSerializableExtra(EXTRA_POWER) as Power?
        if(power != Power.SOFT) {
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

        private const val EXTRA_POWER = "EXTRA_POWER"

        fun start(context: Context, power: Power) {
            getBaseIntent(context).apply {
                putExtra(EXTRA_POWER, power)
                context.startForegroundService(this)
            }
        }

        fun stop(context: Context, power: Power) {
            val engineStatus = context.appComponent.engineStateObservable.currentState.engineStatus
            if(power == Power.HARD || (power == Power.SOFT && engineStatus == Engine.Status.IDLE)) {
                context.stopService(getBaseIntent(context))
            }
        }

        private fun getBaseIntent(context: Context) = Intent(context, MainService::class.java)
    }

    enum class Power {
        // Starts or stops the engine immediately
        HARD,
        // When starting, starts only this service. When stopping, stops only when IDLE.
        SOFT
    }
}