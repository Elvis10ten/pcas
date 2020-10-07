package com.fluentbuild.pcas.services.audio

import android.media.AudioFormat
import android.media.AudioTrack

internal object AudioConfig {

    val sampleRateHz get() = 44100

    val channelMask get() = AudioFormat.CHANNEL_IN_STEREO

    val encoding get() = AudioFormat.ENCODING_PCM_16BIT

    val minBufferSizeBytes get() = AudioTrack.getMinBufferSize(sampleRateHz, channelMask, encoding)

    val audioFormat: AudioFormat
        get() = AudioFormat.Builder()
            .setEncoding(encoding)
            .setChannelMask(channelMask)
            .setSampleRate(sampleRateHz)
            .build()
}