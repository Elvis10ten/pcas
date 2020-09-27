package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.async.Cancellables
import com.fluentbuild.pcas.async.ThreadExecutor
import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.ledger.models.Ledger.Companion.ENTRY_EVICTION_NOTICE_THRESHOLD_MILLIS
import com.fluentbuild.pcas.utils.TimeProvider
import com.fluentbuild.pcas.utils.logger

class LedgerWatchdog(
    private val ledgerStore: LedgerStore,
    private val packetBroadcaster: PacketBroadcaster,
    private val executor: ThreadExecutor,
    private val timerProvider: TimeProvider
) {

    private val log by logger()
    private var lastEvictionNotices = setOf<HostInfo>()

    fun start() {
        val interval = ENTRY_EVICTION_NOTICE_THRESHOLD_MILLIS / 2
        executor.onMainRepeating(interval) {
            val latestEvictionNotices = ledgerStore.get().getEvictionNotices(timerProvider.currentTimeMillis())
            log.debug { "Latest eviction notices: $latestEvictionNotices" }

            latestEvictionNotices.forEach { host ->
                if(latestEvictionNotices.contains(host)) {
                    ledgerStore.evict(host)
                }
            }

            lastEvictionNotices = latestEvictionNotices
            if(latestEvictionNotices.isNotEmpty()) {
                packetBroadcaster.broadcastEvictionNotice()
            }
        }
    }

    fun stop() {
        executor.cancel()
    }
}