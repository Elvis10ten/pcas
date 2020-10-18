package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.host.HostConfig
import com.fluentbuild.pcas.async.ThreadRunner
import com.fluentbuild.pcas.io.*
import com.fluentbuild.pcas.values.Provider
import java.net.DatagramSocket
import java.security.SecureRandom

internal open class IoModuleJvm(
	config: HostConfig,
	private val threadRunnerProvider: () -> ThreadRunner,
	secureRandom: SecureRandom,
	hostAddressProvider: Provider<Address.Ipv4>
): IoModule() {

	private val parceler = Parceler(config.networkKey, secureRandom, BufferObjectPool)

	val unicastChannel = UnicastChannelJvm(getSocketWrapper())

	open val multicastChannel = MulticastChannelJvm(
		socketWrapper = getSocketWrapper(),
		hostAddressProvider = hostAddressProvider
	)

	protected fun <SocketT: DatagramSocket> getSocketWrapper() =
		SocketWrapper<SocketT>(parceler, BufferObjectPool, threadRunnerProvider())
}