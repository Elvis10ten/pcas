package com.fluentbuild.pcas.services.audio

import android.bluetooth.BluetoothProfile
import android.content.Context
import com.fluentbuild.pcas.android.BluetoothProfileStateCallback
import com.fluentbuild.pcas.android.bluetoothAdapter
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.Cancellables
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.peripheral.PeripheralProfile
import com.fluentbuild.pcas.services.AndroidBluetoothProfileId
import com.fluentbuild.pcas.values.Observable

internal class AndroidAudioBondsObservable(
    private val context: Context,
    private val audioPeripheral: Peripheral,
    private val profileHolder: BluetoothProfileHolder
): Observable<PeripheralBond> {

    private val log = getLog()
    private val bluetoothDevice by lazy { context.bluetoothAdapter.toBluetoothDevice(audioPeripheral) }

    override fun subscribe(observer: (PeripheralBond) -> Unit): Cancellable {
        val loadBondsCancellable = Cancellables()
        val stateCallback = BluetoothProfileStateCallback(context) { profile ->
            loadBonds(profile, loadBondsCancellable, observer)
        }

        loadBonds(PeripheralProfile.A2DP, loadBondsCancellable, observer)
        loadBonds(PeripheralProfile.HSP, loadBondsCancellable, observer)
        stateCallback.register()

        return Cancellable {
            log.debug { "Cancelling watcher" }
            stateCallback.unregister()
            loadBondsCancellable.cancel()
            profileHolder.clearCache(BluetoothProfile.A2DP)
            profileHolder.clearCache(BluetoothProfile.HEADSET)
        }
    }

    private fun loadBonds(profile: PeripheralProfile, cancellables: Cancellables, observer: (PeripheralBond) -> Unit) {
        when(profile) {
            PeripheralProfile.A2DP -> {
                cancellables += profileHolder.useProfile(BluetoothProfile.A2DP) {
                    mapToBonds(BluetoothProfile.A2DP, it)?.let(observer)
                }
            }
            PeripheralProfile.HSP -> {
                cancellables += profileHolder.useProfile(BluetoothProfile.HEADSET) {
                    mapToBonds(BluetoothProfile.HEADSET, it)?.let(observer)
                }
            }
            else -> log.error { "$profile not supported in audio service" }
        }
    }

    private fun mapToBonds(profileId: AndroidBluetoothProfileId, profile: BluetoothProfile): PeripheralBond? {
        val state = profile.getConnectionState(bluetoothDevice).toPeripheralState() ?: return null
        return PeripheralBond(
            profile = profileId.toPeripheralProfile(),
            state = state
        )
    }
}