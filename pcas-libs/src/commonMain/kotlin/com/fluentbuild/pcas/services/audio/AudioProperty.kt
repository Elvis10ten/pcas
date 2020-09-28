package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.utils.filterSet
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
data class AudioProperty(
    @ProtoNumber(1)
    val usages: Set<Usage>
) {

    val unidirectionalUsages = usages.filterSet { it.direction == Direction.UNIDIRECTIONAL }

    val bidirectionalUsages = usages.filterSet { it.direction == Direction.BIDIRECTIONAL }

    enum class Usage(
        val priority: Int,
        val direction: Direction
    ) {
        UNKNOWN(1, Direction.UNIDIRECTIONAL),
        // Unknown media playback. It could be music, movie soundtracks, etc.
        MEDIA_UNKNOWN(2, Direction.UNIDIRECTIONAL),
        // Music playback, eg: Music streaming, local audio playback, etc.
        MUSIC(3, Direction.UNIDIRECTIONAL),
        // Speech playback, eg: Podcasts, Audiobooks, etc
        SPEECH(3, Direction.UNIDIRECTIONAL),
        // Soundtrack, typically accompanying a movie or TV program.
        MOVIE(4, Direction.UNIDIRECTIONAL),
        // Game audio playback
        GAME(4, Direction.UNIDIRECTIONAL),
        // Such as VoIP.
        VOICE_COMMUNICATION(5, Direction.BIDIRECTIONAL),
        // Telephony call
        TELEPHONY_CALL(6, Direction.BIDIRECTIONAL)
    }

    enum class Direction {
        UNIDIRECTIONAL,
        BIDIRECTIONAL
    }

    companion object {

        const val NO_PRIORITY = 0
    }
}