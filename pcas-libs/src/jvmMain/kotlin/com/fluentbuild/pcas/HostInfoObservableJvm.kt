package com.fluentbuild.pcas

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.host.HostInfoObservable

class HostInfoObservableJvm: HostInfoObservable {

	override val currentValue: HostInfo
		get() = TODO("Not yet implemented")

	override fun subscribe(observer: (HostInfo) -> Unit): Cancellable {
		TODO("Not yet implemented")
	}
}