package com.fluentbuild.pcas

import com.fluentbuild.pcas.peripheral.PeripheralProfile
import com.fluentbuild.pcas.services.audio.AudioProperty

sealed class Event {

	class Connecting(val profile: PeripheralProfile): Event()

	class Disconnecting(val profile: PeripheralProfile): Event()

	class Streaming(val hostName: String): Event()

	class OnConnected(val profile: PeripheralProfile): Event()

	class OnDisconnected(val profile: PeripheralProfile): Event()

	class OnAudioPlaybackChanged(val audioProperty: AudioProperty): Event()
}