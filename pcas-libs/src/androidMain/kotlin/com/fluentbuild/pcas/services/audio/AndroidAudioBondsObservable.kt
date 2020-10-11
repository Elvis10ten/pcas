package com.fluentbuild.pcas.services.audio

import android.bluetooth.BluetoothProfile
import android.content.Context
import com.fluentbuild.pcas.bluetooth.BluetoothProfileHolder
import com.fluentbuild.pcas.watchers.BluetoothProfileStateWatcher
import com.fluentbuild.pcas.utils.bluetoothAdapter
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.SentinelCancellable
import com.fluentbuild.pcas.bluetooth.getPeripheralBondState
import com.fluentbuild.pcas.bluetooth.toPeripheralProfile
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.peripheral.PeripheralProfile
import com.fluentbuild.pcas.bluetooth.toBluetoothDevice
import com.fluentbuild.pcas.values.Observable

internal class AndroidAudioBondsObservable(
    private val context: Context,
    private val audioPeripheral: Peripheral,
    private val profileHolder: BluetoothProfileHolder
): Observable<PeripheralBond> {

    private val log = getLog()
    private val bluetoothDevice by lazy { context.bluetoothAdapter.toBluetoothDevice(audioPeripheral) }

    it.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED)
    it.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)
    it.addAction(BluetoothHidDevice.ACTION_CONNECTION_STATE_CHANGED)

    override fun subscribe(observer: (PeripheralBond) -> Unit): Cancellable {
        log.debug { "Observing AudioBonds" }
        val profileCancellables = mutableMapOf<PeripheralProfile, Cancellable>()
        val stateCallback = BluetoothProfileStateWatcher(context) { profile ->
            profileCancellables[profile]?.cancel()
            profileCancellables[profile] = loadBonds(profile, observer)
        }

        profileCancellables[PeripheralProfile.A2DP] = loadBonds(PeripheralProfile.A2DP, observer)
        profileCancellables[PeripheralProfile.HSP] = loadBonds(PeripheralProfile.HSP, observer)
        stateCallback.register()

        return Cancellable {
            log.debug { "Cancelling AudioBonds observation" }
            stateCallback.unregister()
            profileCancellables.values.forEach { it.cancel() }
            profileHolder.clearCache(BluetoothProfile.A2DP)
            profileHolder.clearCache(BluetoothProfile.HEADSET)
        }
    }

    private fun loadBonds(profile: PeripheralProfile, observer: (PeripheralBond) -> Unit): Cancellable {
        return when(profile) {
            PeripheralProfile.A2DP -> {
                profileHolder.useProfile(BluetoothProfile.A2DP) {
                    it.getPeripheralBond()?.let(observer)
                }
            }
            PeripheralProfile.HSP -> {
                profileHolder.useProfile(BluetoothProfile.HEADSET) {
                    it.getPeripheralBond()?.let(observer)
                }
            }
            else -> {
                log.warn { "$profile not supported in audio service" }
                SentinelCancellable
            }
        }
    }

    private fun BluetoothProfile.getPeripheralBond(): PeripheralBond? {
        val peripheralBondState = getPeripheralBondState(bluetoothDevice) ?: return null
        return PeripheralBond(toPeripheralProfile(), peripheralBondState)
    }
}