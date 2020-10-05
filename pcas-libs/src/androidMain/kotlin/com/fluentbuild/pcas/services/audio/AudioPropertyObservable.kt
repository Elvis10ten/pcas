package com.fluentbuild.pcas.services.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioPlaybackConfiguration
import android.telephony.TelephonyManager
import com.fluentbuild.pcas.android.AudioPlaybackCallback
import com.fluentbuild.pcas.android.CallStateCallback
import com.fluentbuild.pcas.android.audioManager
import com.fluentbuild.pcas.android.telephonyManager
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.Observable
import com.fluentbuild.pcas.logs.getLog

internal class AudioPropertyObservable(
    private val context: Context
): Observable<AudioProperty> {

    private val log = getLog()

    override fun subscribe(observer: (AudioProperty) -> Unit): Cancellable {
        log.debug { "Watching AudioProperty" }
        val notifyConsumer = {
            context.audioManager.activePlaybackConfigurations.toAudioProperty().let {
                log.debug { "Current AudioProperty: $it" }
                observer(it)
            }
        }

        val callStateCallback = CallStateCallback(context, notifyConsumer)
        val playbackCallback = AudioPlaybackCallback(context, notifyConsumer)

        playbackCallback.register()
        callStateCallback.register()

        return Cancellable {
            log.debug { "Stopping AudioProperty watch" }
            playbackCallback.unregister()
            callStateCallback.unregister()
        }
    }

    private fun List<AudioPlaybackConfiguration>.toAudioProperty(): AudioProperty {
        var usages = mapNotNull { it.toAudioPropertyUsage() }

        if(context.telephonyManager.callState != TelephonyManager.CALL_STATE_IDLE) {
            usages = usages + AudioProperty.Usage.TELEPHONY_CALL
        }

        return AudioProperty(usages.toSet())
    }

    private fun AudioPlaybackConfiguration.toAudioPropertyUsage(): AudioProperty.Usage? {
        val usage = audioAttributes.usage
        val contentType = audioAttributes.usage

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