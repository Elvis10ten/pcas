package com.fluentbuild.pcas.services.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioPlaybackConfiguration
import android.os.Handler
import android.telephony.TelephonyManager
import com.fluentbuild.pcas.android.AudioPlaybackCallback
import com.fluentbuild.pcas.android.CallStateCallback
import com.fluentbuild.pcas.android.audioManager
import com.fluentbuild.pcas.android.telephonyManager
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.values.Observable
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.utils.mapSetNotNullMutable

internal class AudioPropertyObservable(
    private val context: Context,
    private val mainHandler: Handler
): Observable<AudioProperty> {

    private val log = getLog()

    override fun subscribe(observer: (AudioProperty) -> Unit): Cancellable {
        log.debug { "Observing AudioProperty" }
        val notifyObserver = { observer(getCurrentAudioProperty()) }

        val callStateCallback = CallStateCallback(context, notifyObserver)
        val playbackCallback = AudioPlaybackCallback(context, mainHandler, notifyObserver)

        playbackCallback.register()
        callStateCallback.register()
        notifyObserver()

        return Cancellable {
            log.debug { "Cancelling AudioProperty observation" }
            playbackCallback.unregister()
            callStateCallback.unregister()
        }
    }

    private fun getCurrentAudioProperty(): AudioProperty {
        val usages = context.audioManager.activePlaybackConfigurations.mapSetNotNullMutable { it.toAudioPropertyUsage() }

        if(context.telephonyManager.callState != TelephonyManager.CALL_STATE_IDLE) {
            usages += AudioProperty.Usage.TELEPHONY_CALL
        }

        return AudioProperty(usages)
    }

    private fun AudioPlaybackConfiguration.toAudioPropertyUsage(): AudioProperty.Usage? {
        val usage = audioAttributes.usage
        val contentType = audioAttributes.contentType

        return when {
            usage == AudioAttributes.USAGE_VOICE_COMMUNICATION -> AudioProperty.Usage.VOICE_COMMUNICATION
            usage == AudioAttributes.USAGE_GAME -> AudioProperty.Usage.GAME

            contentType == AudioAttributes.CONTENT_TYPE_MOVIE -> AudioProperty.Usage.MOVIE
            contentType == AudioAttributes.CONTENT_TYPE_SPEECH -> AudioProperty.Usage.SPEECH
            contentType == AudioAttributes.CONTENT_TYPE_MUSIC -> AudioProperty.Usage.MUSIC

            usage == AudioAttributes.USAGE_UNKNOWN -> AudioProperty.Usage.UNKNOWN
            usage == AudioAttributes.USAGE_MEDIA -> AudioProperty.Usage.MEDIA_UNKNOWN
            else -> null
        }
    }
}