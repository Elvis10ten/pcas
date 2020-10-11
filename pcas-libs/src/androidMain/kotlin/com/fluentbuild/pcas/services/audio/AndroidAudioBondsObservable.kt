package com.fluentbuild.pcas.services.audio

import android.bluetooth.BluetoothProfile
import android.content.Context
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.bluetooth.BluetoothProfileHolder
import com.fluentbuild.pcas.bluetooth.getPeripheralBondState
import com.fluentbuild.pcas.bluetooth.toBluetoothDevice
import com.fluentbuild.pcas.bluetooth.toPeripheralProfile
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.peripheral.PeripheralProfile
import com.fluentbuild.pcas.utils.bluetoothAdapter
import com.fluentbuild.pcas.values.Observable
import com.fluentbuild.pcas.watchers.BluetoothProfileStateWatcher

internal class AndroidAudioBondsObservable(
    private val context: Context,
    private val audioPeripheral: Peripheral,
    private val profileHolder: BluetoothProfileHolder,
    private val a2dpProfileStateWatcher: BluetoothProfileStateWatcher,
    private val hspProfileStateWatcher: BluetoothProfileStateWatcher
): Observable<PeripheralBond> {

    private val log = getLog()
    private val bluetoothDevice by lazy { context.bluetoothAdapter.toBluetoothDevice(audioPeripheral) }

    override fun subscribe(observer: (PeripheralBond) -> Unit): Cancellable {
        log.debug { "Observing AudioBonds" }

        var a2dpCancellable = loadBonds(PeripheralProfile.A2DP, observer)
        a2dpProfileStateWatcher.register(BluetoothProfileStateWatcher.NO_PROFILE_STATE) {
            a2dpCancellable.cancel()
            a2dpCancellable = loadBonds(PeripheralProfile.A2DP, observer)
        }

        var hspCancellable = loadBonds(PeripheralProfile.HSP, observer)
        hspProfileStateWatcher.register(BluetoothProfileStateWatcher.NO_PROFILE_STATE) {
            hspCancellable.cancel()
            hspCancellable = loadBonds(PeripheralProfile.HSP, observer)
        }

        return Cancellable {
            log.debug { "Stop observing AudioBonds" }
            a2dpCancellable.cancel()
            a2dpProfileStateWatcher.unregister()
            profileHolder.clearCache(BluetoothProfile.A2DP)

            hspCancellable.cancel()
            hspProfileStateWatcher.unregister()
            profileHolder.clearCache(BluetoothProfile.HEADSET)
        }
    }

    private fun loadBonds(peripheralProfile: PeripheralProfile, observer: (PeripheralBond) -> Unit): Cancellable {
        return when(peripheralProfile) {
            PeripheralProfile.A2DP -> {
                profileHolder.useProfile(BluetoothProfile.A2DP) { profile ->
                    profile.getPeripheralBond()?.let(observer)
                }
            }
            PeripheralProfile.HSP -> {
                profileHolder.useProfile(BluetoothProfile.HEADSET) { profile ->
                    profile.getPeripheralBond()?.let(observer)
                }
            }
            else -> error("$peripheralProfile not supported in audio service")
        }
    }

    private fun BluetoothProfile.getPeripheralBond(): PeripheralBond? {
        val bondState = getPeripheralBondState(bluetoothDevice) ?: return null
        return PeripheralBond(toPeripheralProfile(), bondState)
    }
}