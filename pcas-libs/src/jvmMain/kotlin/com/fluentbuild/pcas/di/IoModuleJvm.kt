package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.HostConfig
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
) {

	private val parceler = Parceler(config, secureRandom, BufferObjectPool)

	val unicastChannel = JvmUnicastChannel(getSocketWrapper())

	open val multicastChannel = JvmMulticastChannel(
		socketWrapper = getSocketWrapper(),
		hostAddressProvider = hostAddressProvider
	)

	protected fun <SocketT: DatagramSocket> getSocketWrapper() =
		SocketWrapper<SocketT>(parceler, BufferObjectPool, threadRunnerProvider())
}