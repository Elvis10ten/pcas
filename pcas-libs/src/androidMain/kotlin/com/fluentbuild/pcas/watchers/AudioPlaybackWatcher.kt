package com.fluentbuild.pcas.watchers

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.AudioPlaybackConfiguration
import android.os.Handler
import com.fluentbuild.pcas.utils.audioManager
import com.fluentbuild.pcas.logs.getLog

internal class AudioPlaybackWatcher(
    private val context: Context,
    private val mainHandler: Handler
): Watcher<List<AudioAttributes>>() {

    private val log = getLog()
    private val audioManager get() = context.audioManager
    private val playbackCallback = object: AudioManager.AudioPlaybackCallback() {

        override fun onPlaybackConfigChanged(configs: List<AudioPlaybackConfiguration>) {
            log.debug(::onPlaybackConfigChanged, configs)
            onUpdated(configs.map { it.audioAttributes })
        }
    }

    override fun registerInternal() {
        audioManager.registerAudioPlaybackCallback(playbackCallback, mainHandler)
    }

    override fun unregisterInternal() {
        audioManager.unregisterAudioPlaybackCallback(playbackCallback)
    }
}