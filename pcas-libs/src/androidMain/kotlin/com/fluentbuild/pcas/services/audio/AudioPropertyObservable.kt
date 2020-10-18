package com.fluentbuild.pcas.services.audio

import android.content.Context
import android.media.AudioAttributes
import android.telephony.TelephonyManager
import com.fluentbuild.pcas.watchers.AudioPlaybackWatcher
import com.fluentbuild.pcas.watchers.CallStateWatcher
import com.fluentbuild.pcas.utils.audioManager
import com.fluentbuild.pcas.utils.telephonyManager
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.values.Observable
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.utils.mapSetNotNullMutable

internal class AudioPropertyObservable(
    private val context: Context,
    private val callStateWatcher: CallStateWatcher,
    private val audioPlaybackWatcher: AudioPlaybackWatcher
): Observable<AudioProperty> {

    private val log = getLog()

    private val telephonyManager get() = context.telephonyManager
    private val currentTelephonyState get() = telephonyManager.callState

    private val audioManager get() = context.audioManager
    private val currentAudioAttributes get() = audioManager.activePlaybackConfigurations.map { it.audioAttributes }

    override fun subscribe(observer: (AudioProperty) -> Unit): Cancellable {
        log.debug { "Observing AudioProperty" }
        val initialAudioAttributes = currentAudioAttributes
        val initialTelephonyState = currentTelephonyState
        observer(getAudioProperty(initialAudioAttributes, initialTelephonyState))

        val notifyObserver = { observer(getAudioProperty(currentAudioAttributes, currentTelephonyState)) }
        callStateWatcher.register(initialTelephonyState, notifyObserver)
        audioPlaybackWatcher.register(initialAudioAttributes, notifyObserver)

        return Cancellable {
            log.debug { "Stop observing AudioProperty" }
            callStateWatcher.unregister()
            audioPlaybackWatcher.unregister()
        }
    }

    private fun getAudioProperty(audioAttributes: List<AudioAttributes>, telephonyState: Int): AudioProperty {
        val audioUsages = audioAttributes.mapSetNotNullMutable { it.toAudioPropertyUsage() }

        if(telephonyState != TelephonyManager.CALL_STATE_IDLE) {
            audioUsages += AudioProperty.Usage.TELEPHONY_CALL
        }

        return AudioProperty(audioUsages)
    }

    private fun AudioAttributes.toAudioPropertyUsage(): AudioProperty.Usage? {
        return when {
            usage == AudioAttributes.USAGE_VOICE_COMMUNICATION -> AudioProperty.Usage.VOICE_COMMUNICATION
            usage == AudioAttributes.USAGE_GAME -> AudioProperty.Usage.GAME

            contentType == AudioAttributes.CONTENT_TYPE_MOVIE -> AudioProperty.Usage.MOVIE
            contentType == AudioAttributes.CONTENT_TYPE_SPEECH -> AudioProperty.Usage.SPEECH
            contentType == AudioAttributes.CONTENT_TYPE_MUSIC -> AudioProperty.Usage.MUSIC

            usage == AudioAttributes.USAGE_MEDIA -> AudioProperty.Usage.MEDIA_UNKNOWN
            usage == AudioAttributes.USAGE_UNKNOWN -> AudioProperty.Usage.UNKNOWN
            else -> null
        }
    }
}