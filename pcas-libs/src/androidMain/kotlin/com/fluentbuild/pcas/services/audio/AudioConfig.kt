package com.fluentbuild.pcas.services.audio

import android.media.AudioFormat
import android.media.AudioTrack

internal object AudioConfig {

    const val SAMPLE_RATE_HZ = 44100

    const val CHANNEL_MASK = AudioFormat.CHANNEL_IN_STEREO

    const val ENCODING = AudioFormat.ENCODING_PCM_16BIT

    val minBufferSizeBytes get() = AudioTrack.getMinBufferSize(SAMPLE_RATE_HZ, CHANNEL_MASK, ENCODING)

    val audioFormat: AudioFormat
        get() = AudioFormat.Builder()
            .setEncoding(ENCODING)
            .setChannelMask(CHANNEL_MASK)
            .setSampleRate(SAMPLE_RATE_HZ)
            .build()
}