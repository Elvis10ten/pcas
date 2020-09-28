package com.fluentbuild.pcas.services.audio

import android.media.AudioFormat
import android.media.AudioTrack

object AudioConfig {

    val sampleRateHz: Int
        get() = 44100

    val channelMask: Int
        get() = AudioFormat.CHANNEL_IN_STEREO

    val encoding: Int
        get() = AudioFormat.ENCODING_PCM_16BIT

    val minBufferSizeBytes: Int
        get() = AudioTrack.getMinBufferSize(sampleRateHz, channelMask, encoding)

    val audioFormat: AudioFormat
        get() = AudioFormat.Builder()
            .setEncoding(encoding)
            .setChannelMask(channelMask)
            .setSampleRate(sampleRateHz)
            .build()
}