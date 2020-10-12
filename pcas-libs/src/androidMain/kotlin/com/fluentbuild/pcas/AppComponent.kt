package com.fluentbuild.pcas

import android.content.Context
import com.fluentbuild.pcas.host.HostConfigStore
import com.fluentbuild.pcas.utils.bluetoothAdapter
import kotlinx.serialization.protobuf.ProtoBuf

class AppComponent(private val appContext: Context) {

	private val protoBuf = ProtoBuf

	val hostConfigStore = HostConfigStore(protoBuf, appContext.filesDir) { appContext.bluetoothAdapter.name }

	private val engineComponentProvider = { EngineComponentAndroid(appContext, hostConfigStore.get(), protoBuf) }

	val engine = Engine(engineComponentProvider)
}