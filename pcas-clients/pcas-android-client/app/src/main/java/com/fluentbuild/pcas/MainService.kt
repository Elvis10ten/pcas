package com.fluentbuild.pcas

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.annotation.StringRes
import com.fluentbuild.pcas.ui.GeneralNotifications

class MainService: Service() {

    private var numRetries = 0
    private val handler = Handler(Looper.getMainLooper())
    private val retryRunnable = Runnable { startEngine() }
    private lateinit var notifications: GeneralNotifications
    
    override fun onCreate() {
        super.onCreate()
        notifications = GeneralNotifications(this)
        startForeground(GeneralNotifications.ID, notifications.builder.build())
        startEngine()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopEngine()
    }

    private fun startEngine() {
        try {
            updateMessage(R.string.notificationDefaultMsg)
            getApp().engineController.startEngine()
        } catch (t: Throwable) {
            t.printStackTrace()
            updateMessage(R.string.notificationErrorMsg, t.message)
            getApp().engineController.stopEngine()
            getApp().engineController.release()

            if(numRetries < MAX_TOTAL_RETRIES) {
                numRetries++
                handler.postDelayed(retryRunnable, RETRY_DELAY_MILLIS)
            } else {
                updateMessage(R.string.notificationErrorShutdownMsg)
                stopSelf()
            }
        }
    }
    
    private fun stopEngine() {
        getApp().engineController.stopEngine()
        getApp().engineController.release()
        handler.removeCallbacks(retryRunnable)
    }

    private fun updateMessage(@StringRes stringId: Int, vararg formatArgs: String?) {
        notifications.notify(stringId, *formatArgs)
    }

    override fun onBind(intent: Intent): IBinder? = null
    
    companion object {

        private const val MAX_TOTAL_RETRIES = 10
        private const val RETRY_DELAY_MILLIS = 2000L

        fun start(context: Context) {
            context.startForegroundService(getIntent(context))
        }

        fun stop(context: Context) {
            context.stopService(getIntent(context))
        }

        private fun getIntent(context: Context) = Intent(context, MainService::class.java)
    }
}