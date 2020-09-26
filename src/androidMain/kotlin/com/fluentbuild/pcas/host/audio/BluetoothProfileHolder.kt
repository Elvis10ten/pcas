package com.fluentbuild.pcas.host.audio

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothProfile
import android.content.Context
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.SentinelCancellable
import com.fluentbuild.pcas.utils.logger

class BluetoothProfileHolder(
    private val context: Context,
    private val bluetoothAdapter: BluetoothAdapter
) {

    private val log by logger()
    private val cachedProfiles = mutableMapOf<ProfileId, BluetoothProfile>()
    private val pendingConsumers = mutableMapOf<ProfileId, MutableList<Consumer>>().withDefault { mutableListOf() }

    fun useProfile(profileId: ProfileId, consumer: Function1<BluetoothProfile, Unit>): Cancellable {
        val cachedProfile = cachedProfiles[profileId]
        return if(cachedProfile != null) {
            log.debug { "Using cached profile: $cachedProfile" }
            consumer(cachedProfile)
            SentinelCancellable
        } else {
            pendingConsumers.getValue(profileId) += consumer
            loadProfile(profileId)
            Cancellable { pendingConsumers.getValue(profileId) -= consumer }
        }
    }

    private fun loadProfile(profileId: ProfileId) {
        val serviceListener = object : BluetoothProfile.ServiceListener {

            override fun onServiceConnected(profileId: Int, proxy: BluetoothProfile) {
                log.debug { "Profile connected: $profileId" }
                cachedProfiles[profileId] = proxy
                invokePendingConsumers(profileId, proxy)
            }

            override fun onServiceDisconnected(profileId: Int) {
                log.error { "Profile disconnected: $profileId" }
                cachedProfiles.remove(profileId)
            }
        }

        log.debug { "Loading profile: $profileId" }
        if(!bluetoothAdapter.getProfileProxy(context, serviceListener, profileId)) {
            error("BluetoothAdapter.getProfileProxy should never return false")
        }
    }

    private fun invokePendingConsumers(profileId: ProfileId, profile: BluetoothProfile) {
        val iterator = pendingConsumers.getValue(profileId).listIterator()
        for (consumer in iterator) {
            consumer(profile)
            iterator.remove()
        }
    }

    fun dropProfile(profileId: ProfileId) {
        cachedProfiles[profileId]?.let {
            log.debug { "Dropping cached profile: $profileId" }
            cachedProfiles.remove(profileId)
            bluetoothAdapter.closeProfileProxy(profileId, it)
        }
    }
}

private typealias Consumer = Function1<BluetoothProfile, Unit>
private typealias ProfileId = Int