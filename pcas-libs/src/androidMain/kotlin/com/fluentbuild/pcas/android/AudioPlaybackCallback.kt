package com.fluentbuild.pcas.android

import android.content.Context
import android.media.AudioManager
import android.media.AudioPlaybackConfiguration
import android.telephony.PhoneStateListener

class AudioPlaybackCallback(
    private val context: Context,
    private val onChanged: () -> Unit
): AudioManager.AudioPlaybackCallback() {

    override fun onPlaybackConfigChanged(configs: MutableList<AudioPlaybackConfiguration>?) {
        onChanged()
    }

    fun register() {
        context.audioManager.registerAudioPlaybackCallback(this, null)
    }

    fun unregister() {
        context.audioManager.unregisterAudioPlaybackCallback(this)
    }
}