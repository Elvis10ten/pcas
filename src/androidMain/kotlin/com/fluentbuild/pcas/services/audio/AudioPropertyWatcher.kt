package com.fluentbuild.pcas.services.audio

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.AudioPlaybackConfiguration
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.Watcher
import com.fluentbuild.pcas.host.audio.AudioProperty

class AudioPropertyWatcher(
    private val telephonyManager: TelephonyManager,
    private val audioManager: AudioManager
): Watcher<AudioProperty> {

    override fun watch(consumer: (AudioProperty) -> Unit): Cancellable {
        val phoneStateListener = object: PhoneStateListener() {

            override fun onCallStateChanged(state: Int, phoneNumber: String) {
                consumer(audioManager.activePlaybackConfigurations.toAudioProperty())
            }
        }

        val playbackCallback = object: AudioManager.AudioPlaybackCallback() {

            override fun onPlaybackConfigChanged(configs: List<AudioPlaybackConfiguration>) {
                consumer(configs.toAudioProperty())
                // TODO: We don't get notified when playback is dropped, so doing this manually
            }
        }

        audioManager.registerAudioPlaybackCallback(playbackCallback, null)
        // TelephonyManager immediately invokes callback with current value
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)

        return Cancellable {
            audioManager.unregisterAudioPlaybackCallback(playbackCallback)
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
        }
    }

    fun List<AudioPlaybackConfiguration>.toAudioProperty(): AudioProperty {
        val usages = this.mapNotNull { playback ->
            when (playback.audioAttributes.usage) {
                AudioAttributes.USAGE_VOICE_COMMUNICATION -> AudioProperty.Usage.VOICE_COMMUNICATION
                AudioAttributes.USAGE_GAME -> AudioProperty.Usage.GAME
                AudioAttributes.USAGE_UNKNOWN -> AudioProperty.Usage.UNKNOWN
                AudioAttributes.USAGE_MEDIA -> {
                    when(playback.audioAttributes.contentType) {
                        AudioAttributes.CONTENT_TYPE_MOVIE -> AudioProperty.Usage.MOVIE
                        AudioAttributes.CONTENT_TYPE_MUSIC -> AudioProperty.Usage.MUSIC
                        AudioAttributes.CONTENT_TYPE_SPEECH -> AudioProperty.Usage.SPEECH
                        AudioAttributes.CONTENT_TYPE_UNKNOWN -> AudioProperty.Usage.MEDIA_UNKNOWN
                        else -> AudioProperty.Usage.MEDIA_UNKNOWN
                    }
                }
                else -> null
            }
        }.toMutableList()

        if(telephonyManager.callState != TelephonyManager.CALL_STATE_IDLE) {
            usages += AudioProperty.Usage.TELEPHONY_CALL
        }

        return AudioProperty(usages.toSet())
    }
}