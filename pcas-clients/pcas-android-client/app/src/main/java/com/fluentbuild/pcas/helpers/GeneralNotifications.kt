package com.fluentbuild.pcas.helpers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.fluentbuild.pcas.MainActivity
import com.fluentbuild.pcas.R

class GeneralNotifications(private val context: Context) {

    private val Context.notificationManager
        get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val builder: NotificationCompat.Builder
    
    init {
        val channelName = context.getString(R.string.generalNotificationName)
        val channelDescription = context.getString(R.string.generalNotificationDesc)
        val importance = NotificationManager.IMPORTANCE_LOW

        NotificationChannel(CHANNEL_ID, channelName, importance).apply {
            description = channelDescription
            context.notificationManager.createNotificationChannel(this)
        }

        builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.appName))
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentText(context.getString(R.string.notificationIdleMsg))
            .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    MainActivity.getIntent(context),
                    PendingIntent.FLAG_CANCEL_CURRENT
                ))
    }

    fun notify(@StringRes stringId: Int, vararg formatArgs: String?) {
        val notification = builder.setContentText(context.getString(stringId, *formatArgs)).build()
        context.notificationManager.notify(ID, notification)
    }

    fun buildNotification(): Notification = builder.build()

    companion object {

        private const val CHANNEL_ID = "general"
        const val ID = 1
    }
}