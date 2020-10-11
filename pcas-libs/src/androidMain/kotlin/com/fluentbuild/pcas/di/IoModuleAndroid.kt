package com.fluentbuild.pcas.di

import android.content.Context
import com.fluentbuild.pcas.HostConfig
import com.fluentbuild.pcas.async.ThreadRunner
import com.fluentbuild.pcas.io.Address
import com.fluentbuild.pcas.io.MulticastChannelAndroid
import com.fluentbuild.pcas.values.Provider
import java.security.SecureRandom

internal class IoModuleAndroid(
    appContext: Context,
    hostConfig: HostConfig,
    threadRunnerProvider: () -> ThreadRunner,
    secureRandom: SecureRandom,
    hostAddressProvider: Provider<Address.Ipv4>
): IoModuleJvm(hostConfig, threadRunnerProvider, secureRandom, hostAddressProvider) {

    override val multicastChannel = MulticastChannelAndroid(appContext, hostAddressProvider, getSocketWrapper())
}