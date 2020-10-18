package com.fluentbuild.pcas.services.audio

import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothHeadset
import android.bluetooth.BluetoothProfile
import android.content.Context
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.bluetooth.BluetoothProfileHolder
import com.fluentbuild.pcas.bluetooth.getAndroidProfileId
import com.fluentbuild.pcas.bluetooth.getPeripheralBondState
import com.fluentbuild.pcas.bluetooth.toBluetoothDevice
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.peripheral.PeripheralProfile
import com.fluentbuild.pcas.utils.bluetoothAdapter
import com.fluentbuild.pcas.values.Observable
import com.fluentbuild.pcas.watchers.BluetoothProfileStateWatcher

internal class AudioBondsObservableAndroid(
    private val context: Context,
    private val audioPeripheral: Peripheral,
    private val profileHolder: BluetoothProfileHolder,
    private val profileStateWatchers: Map<PeripheralProfile, BluetoothProfileStateWatcher>
): Observable<PeripheralBond> {

    private val log = getLog()
    private val bluetoothDevice by lazy { context.bluetoothAdapter.toBluetoothDevice(audioPeripheral) }

    override fun subscribe(observer: (PeripheralBond) -> Unit): Cancellable {
        log.debug { "Observing AudioBonds" }
        val cancellables = LinkedHashMap<PeripheralProfile, Cancellable>(profileStateWatchers.size)

        profileStateWatchers.forEach { (profile, watcher) ->
            cancellables[profile] = loadBonds(profile, observer)
            watcher.register(BluetoothProfileStateWatcher.NO_PROFILE_STATE) {
                cancellables[profile]?.cancel()
                cancellables[profile] = loadBonds(profile, observer)
            }
        }

        return Cancellable {
            log.debug { "Stop observing AudioBonds" }
            profileStateWatchers.forEach { (profile, watcher) ->
                cancellables[profile]?.cancel()
                watcher.unregister()
                profileHolder.clearCache(profile.getAndroidProfileId())
            }
        }
    }

    private fun loadBonds(profile: PeripheralProfile, observer: (PeripheralBond) -> Unit): Cancellable {
        return profileHolder.useProfile(profile.getAndroidProfileId()) {
            observer(it.getPeripheralBond())
        }
    }

    private fun BluetoothProfile.getPeripheralBond(): PeripheralBond {
        val bondHotState = getPeripheralBondState(bluetoothDevice)
        return PeripheralBond(toPeripheralProfile(), bondHotState, bondHotState.getSteadyState())
    }

    private fun BluetoothProfile.toPeripheralProfile(): PeripheralProfile {
        return when (this) {
            is BluetoothA2dp -> PeripheralProfile.A2DP
            is BluetoothHeadset -> PeripheralProfile.HEADSET
            else -> error("Unsupported bluetooth profile: $this")
        }
    }
}