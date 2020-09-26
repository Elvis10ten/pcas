package com.fluentbuild.pcas.host.audio

import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHeadset
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.Cancellables
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.host.PeripheralBondsWatcher
import com.fluentbuild.pcas.host.profileIdToAudioProfile
import com.fluentbuild.pcas.host.toPeripheral
import com.fluentbuild.pcas.host.toPeripheralState
import com.fluentbuild.pcas.utils.logger

class AudioBondsWatcher(
    private val context: Context,
    private val profileHolder: BluetoothProfileHolder
): PeripheralBondsWatcher {

    private val log by logger()
    private val connectionStates = intArrayOf(
        BluetoothProfile.STATE_CONNECTED,
        BluetoothProfile.STATE_CONNECTING,
        BluetoothProfile.STATE_DISCONNECTED,
        BluetoothProfile.STATE_DISCONNECTING,
    )

    override fun watch(consumer: (Set<PeripheralBond>) -> Unit): Cancellable {
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
                    consumer(mapToBonds(BluetoothProfile.A2DP, it))
                }
            }
            BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED -> {
                cancellables += profileHolder.useProfile(BluetoothProfile.HEADSET) {
                    consumer(mapToBonds(BluetoothProfile.HEADSET, it))
                }
            }
        }
    }

    private fun mapToBonds(profileId: Int, profile: BluetoothProfile): Set<PeripheralBond> {
        return profile.getDevicesMatchingConnectionStates(connectionStates).map {
            PeripheralBond(
                peripheral = it.toPeripheral(),
                bondId = profileIdToAudioProfile(profileId).bondId,
                state = profile.getConnectionState(it).toPeripheralState()
            )
        }.toSet()
    }
}