package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.ThreadRunner
import com.fluentbuild.pcas.host.Uuid
import com.fluentbuild.pcas.ledger.messages.MessageSender
import com.fluentbuild.pcas.utils.TimeProvider
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.utils.Timestamp

internal class LedgerWatchdog(
    private val ledgerDb: LedgerDb,
    private val messageSender: MessageSender,
    private val runner: ThreadRunner,
    private val timeProvider: TimeProvider
) {

    private val log = getLog()
    private val lastHostHeartbeatTimestamps = mutableMapOf<Uuid, Timestamp>()

    fun run(): Cancellable {
        runner.runOnMainRepeating(HEARTBEAT_INTERVAL_MILLIS) {
            messageSender.sendHeartbeat()

            val currentTimestamp = timeProvider.currentTimeMillis()
            val deadHosts = lastHostHeartbeatTimestamps.filter { (_, lastHeartbeatTimestamp) ->
                currentTimestamp - lastHeartbeatTimestamp > HOST_TTL_MILLIS
            }.keys

            if(deadHosts.isNotEmpty()) {
                log.warn { "Removing dead hosts: $deadHosts" }
                ledgerDb.delete(deadHosts)
                deadHosts.forEach { lastHostHeartbeatTimestamps.remove(it) }
            }
        }

        return Cancellable {
            runner.cancelAll()
            lastHostHeartbeatTimestamps.clear()
        }
    }

    fun onHeartbeatReceived(hostUuid: Uuid) {
        lastHostHeartbeatTimestamps[hostUuid] = timeProvider.currentTimeMillis()
    }

    companion object {

        private const val HEARTBEAT_INTERVAL_MILLIS = 20 * 1000
        private const val HOST_TTL_MILLIS = HEARTBEAT_INTERVAL_MILLIS * 3
    }
}