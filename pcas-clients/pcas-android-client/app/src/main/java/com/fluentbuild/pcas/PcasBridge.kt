package com.fluentbuild.pcas

import android.content.Context
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.di.AppContainer
import com.fluentbuild.pcas.models.EngineState

class PcasBridge(private val appContext: Context) {

    private var appContainer: AppContainer? = null
    private var runStateCallback: ((EngineState) -> Unit)? = null

    private var engineCancellable: Cancellable? = null
        private set(value) {
            field = value
            runStateCallback?.invoke(engineState)
        }

    private val engineState
        get() = if(engineCancellable == null) EngineState.IDLE else EngineState.STARTED

    fun setRunCallback(callback: ((EngineState) -> Unit)? = null) {
        runStateCallback = callback
        runStateCallback?.invoke(engineState)
    }

    fun getHostConfigStore() = getAppContainer().hostConfigStore

    fun startEngine() {
        setCancellable(getAppContainer().engine.run())
    }

    fun stopEngine() {
        engineCancellable?.cancel()
        setCancellable(null)
    }

    fun setCancellable(cancellable: Cancellable?) {
        this.engineCancellable = cancellable
        runStateCallback?.invoke(engineState)
    }

    private fun getAppContainer(): AppContainer {
        val container = appContainer ?: AppContainer(appContext)
        appContainer = container
        return container
    }

    fun release() {
        if(engineState == EngineState.IDLE) {
            appContainer?.release()
            appContainer = null
        }
    }
}