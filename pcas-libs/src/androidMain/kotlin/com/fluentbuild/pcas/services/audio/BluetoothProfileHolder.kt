package com.fluentbuild.pcas.services.audio

import android.bluetooth.BluetoothProfile
import android.content.Context
import com.fluentbuild.pcas.android.bluetoothAdapter
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.SentinelCancellable
import com.fluentbuild.pcas.logs.logger
import java.util.*

class BluetoothProfileHolder(private val context: Context) {

    private val log by logger()
    private val cachedProfiles = mutableMapOf<ProfileId, BluetoothProfile>()
    private val pendingConsumers = mutableMapOf<ProfileId, Queue<Consumer>>()

    internal fun useProfile(profileId: ProfileId, consumer: Function1<BluetoothProfile, Unit>): Cancellable {
        val cachedProfile = cachedProfiles[profileId]
        return if(cachedProfile != null) {
            log.debug { "Using cached profile: $cachedProfile" }
            consumer(cachedProfile)
            SentinelCancellable
        } else {
            getProfileConsumers(profileId) += consumer
            loadProfile(profileId)
            Cancellable {
                getProfileConsumers(profileId) -= consumer
            }
        }
    }

    private fun getProfileConsumers(profileId: ProfileId): Queue<Consumer> {
        return pendingConsumers.getOrPut(profileId, { ArrayDeque() })
    }

    private fun loadProfile(profileId: ProfileId) {
        val serviceListener = object : BluetoothProfile.ServiceListener {

            override fun onServiceConnected(profileId: Int, proxy: BluetoothProfile) {
                log.debug { "Profile service connected: $profileId" }
                cachedProfiles[profileId] = proxy
                invokePendingConsumers(profileId, proxy)
            }

            override fun onServiceDisconnected(profileId: Int) {
                log.error { "Profile service disconnected: $profileId" }
                cachedProfiles.remove(profileId)
            }
        }

        log.debug { "Loading profile: $profileId" }
        if(!context.bluetoothAdapter.getProfileProxy(context, serviceListener, profileId)) {
            error("BluetoothAdapter.getProfileProxy returned false")
        }
    }

    private fun invokePendingConsumers(profileId: ProfileId, profile: BluetoothProfile) {
        val pendingConsumers = getProfileConsumers(profileId)
        while(pendingConsumers.isNotEmpty()) {
            val consumer = pendingConsumers.remove()
            log.debug { "Invoking pending consumer" }
            consumer(profile)
        }
    }

    fun clearCache(profileId: ProfileId) {
        cachedProfiles[profileId]?.let {
            log.debug { "Dropping cached profile: $profileId" }
            cachedProfiles.remove(profileId)
            context.bluetoothAdapter.closeProfileProxy(profileId, it)
        }
    }
}

private typealias Consumer = Function1<BluetoothProfile, Unit>
private typealias ProfileId = Int