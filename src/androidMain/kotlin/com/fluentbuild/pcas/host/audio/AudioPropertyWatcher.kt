package com.fluentbuild.pcas.host.audio

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.Watcher

class AudioPropertyWatcher: Watcher<AudioProperty> {

    override fun watch(consumer: (AudioProperty) -> Unit): Cancellable {
        TODO("Not yet implemented")
    }

    override val currentValue: AudioProperty
        get() = TODO("Not yet implemented")
}