package com.fluentbuild.pcas.services.audio

import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothHeadset
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.fluentbuild.pcas.android.bluetoothAdapter
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.Cancellables
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.utils.logger
import com.fluentbuild.pcas.utils.unsafeLazy

internal class AndroidAudioBondsObservable(
    private val context: Context,
    private val audioPeripheral: Peripheral,
    private val profileHolder: BluetoothProfileHolder
): AudioBondsObservable {

    private val log by logger()
    private val bluetoothDevice by unsafeLazy { context.bluetoothAdapter.toBluetoothDevice(audioPeripheral) }

    private val bonds = mutableSetOf<PeripheralBond>()

    override fun subscribe(consumer: (Set<PeripheralBond>) -> Unit): Cancellable {
        val cancellables = Cancellables()
        val connectionChangeReceiver = object: BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                log.debug(::onReceive, intent)
                val action = intent.action ?: return
                retrieveBonds(action, cancellables, consumer)
            }
        }

        retrieveBonds(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED, cancellables, consumer)
        retrieveBonds(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED, cancellables, consumer)

        IntentFilter().run {
            addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED)
            addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)
            context.registerReceiver(connectionChangeReceiver, this)
        }

        return Cancellable {
            log.debug { "Cancelling watcher" }
            cancellables.cancel()
            context.unregisterReceiver(connectionChangeReceiver)
        }
    }

    private fun retrieveBonds(action: String, cancellables: Cancellables, consumer: (Set<PeripheralBond>) -> Unit) {
        when(action) {
            BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED -> {
                cancellables += profileHolder.useProfile(BluetoothProfile.A2DP) {
                    consumer(retrieveBonds(BluetoothProfile.A2DP, it))
                }
            }
            BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED -> {
                cancellables += profileHolder.useProfile(BluetoothProfile.HEADSET) {
                    consumer(retrieveBonds(BluetoothProfile.HEADSET, it))
                }
            }
        }
    }

    private fun retrieveBonds(profileId: Int, profile: BluetoothProfile): Set<PeripheralBond> {
        val updatedBond = mapToBonds(profileId, profile)
        bonds -= updatedBond
        bonds += updatedBond
        return bonds
    }

    private fun mapToBonds(profileId: Int, profile: BluetoothProfile): PeripheralBond {
        return PeripheralBond(
            peripheral = audioPeripheral,
            bondId = profileId.bluetoothProfileIdToAudioProfile().bondId,
            state = profile.getConnectionState(bluetoothDevice).bluetoothProfileStateToPeripheralState()
        )
    }
}