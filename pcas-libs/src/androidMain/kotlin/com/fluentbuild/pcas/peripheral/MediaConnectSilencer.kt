package com.fluentbuild.pcas.peripheral

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Handler
import com.fluentbuild.pcas.io.OFFSET_ZERO
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.services.audio.AudioConfig
import com.fluentbuild.pcas.utils.audioManager

// TODO: Experimental
class MediaConnectSilencer(
	private val context: Context,
	private val handler: Handler
): ConnectSilencer {

	private val log = getLog()
	private val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).build()

	private val silence by lazy { ByteArray(AudioConfig.getIdealBufferSizeBytes()) }
	private val audioTrack by lazy {
		AudioTrack(
			audioAttributes,
			AudioConfig.audioFormat,
			silence.size,
			AudioTrack.MODE_STATIC,
			AudioManager.AUDIO_SESSION_ID_GENERATE
		).apply {
			write(silence, OFFSET_ZERO, silence.size)
			setLoopPoints(0, this.bufferSizeInFrames, LOOP_COUNT_INFINITE)
		}
	}

	private var hasAudioFocus = false

	private val focusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
		when(focusChange) {
			AudioManager.AUDIOFOCUS_LOSS -> {
				stop()
			}
		}
	}

	private val transientFocusRequest by lazy {
		AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE)
			.setOnAudioFocusChangeListener(focusChangeListener, handler)
			.setAudioAttributes(audioAttributes)
			.build()
	}

	override fun start() {
		if(hasAudioFocus || !isActivePlaybackSupported()) return

		val requestStatus = context.audioManager.requestAudioFocus(transientFocusRequest)
		log.info { "Obtain audio focus status: $requestStatus" }
		hasAudioFocus = requestStatus == AudioManager.AUDIOFOCUS_REQUEST_GRANTED

		if(hasAudioFocus) {
			audioTrack.play()
		}
	}

	override fun stop() {
		if(!hasAudioFocus) return

		log.info { "Abandoning audio focus" }
		hasAudioFocus = false
		audioTrack.stop()
		context.audioManager.abandonAudioFocusRequest(transientFocusRequest)
	}

	override fun onBondUpdated(bond: PeripheralBond) {
		if(bond.hotState != PeripheralBond.State.CONNECTING) {
			stop()
		}
	}

	override fun release() {
		stop()
		audioTrack.release()
	}

	private fun isActivePlaybackSupported(): Boolean {
		val activePlayback = context.audioManager.activePlaybackConfigurations
		return activePlayback.isNotEmpty() && activePlayback.all { it.audioAttributes.usage == AudioAttributes.USAGE_MEDIA }
	}

	companion object {

		private const val LOOP_COUNT_INFINITE = -1
	}
}