package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.peripheral.PeripheralProfile

data class AudioProperty(val usages: Set<Usage>) {

    enum class Usage(
        val priority: Int,
        val profile: PeripheralProfile
    ) {
        UNKNOWN(1, PeripheralProfile.A2DP),
        // Unknown media playback. It could be music, movie soundtracks, etc.
        MEDIA_UNKNOWN(2, PeripheralProfile.A2DP),
        // Music playback, eg: Music streaming, local audio playback, etc.
        MUSIC(2, PeripheralProfile.A2DP),
        // Speech playback, eg: Podcasts, Audiobooks, etc
        SPEECH(2, PeripheralProfile.A2DP),
        // Soundtrack, typically accompanying a movie or TV program.
        MOVIE(4, PeripheralProfile.A2DP),
        // Game audio playback
        GAME(4, PeripheralProfile.A2DP),
        // Such as VoIP.
        VOICE_COMMUNICATION(5, PeripheralProfile.HEADSET),
        // Telephony call
        TELEPHONY_CALL(6, PeripheralProfile.HEADSET)
    }
}