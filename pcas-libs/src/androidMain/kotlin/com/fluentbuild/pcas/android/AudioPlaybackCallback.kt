package com.fluentbuild.pcas.android

import android.content.Context
import android.media.AudioManager
import android.media.AudioPlaybackConfiguration
import android.os.Handler
import android.telephony.PhoneStateListener
import com.fluentbuild.pcas.logs.getLog

internal class AudioPlaybackCallback(
    private val context: Context,
    private val mainHandler: Handler,
    private val onChanged: () -> Unit
): AudioManager.AudioPlaybackCallback() {

    private val log = getLog()

    override fun onPlaybackConfigChanged(configs: List<AudioPlaybackConfiguration>?) {
        log.debug { "Playback config changed: $configs" }
        onChanged()
    }

    fun register() {
        context.audioManager.registerAudioPlaybackCallback(this, mainHandler)
    }

    fun unregister() {
        context.audioManager.unregisterAudioPlaybackCallback(this)
    }
}