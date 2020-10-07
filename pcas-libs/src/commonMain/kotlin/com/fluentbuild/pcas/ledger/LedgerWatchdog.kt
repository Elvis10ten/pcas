package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.ThreadRunner
import com.fluentbuild.pcas.host.Uuid
import com.fluentbuild.pcas.utils.TimeProvider
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.utils.Timestamp

internal class LedgerWatchdog(
    private val ledgerDb: LedgerDb,
    private val messageSender: LedgerMessageSender,
    private val runner: ThreadRunner,
    private val timeProvider: TimeProvider
) {

    private val log = getLog()
    private val lastHeartbeatTimestamps = mutableMapOf<Uuid, Timestamp>()

    fun run(): Cancellable {
        runner.runOnMainRepeating(HEARTBEAT_INTERVAL_MILLIS) {
            messageSender.sendHeartbeat()

            val currentTimestamp = timeProvider.currentTimeMillis()
            val deadHosts = lastHeartbeatTimestamps.filter { currentTimestamp - it.value > HOST_TTL_MILLIS }.keys

            if(deadHosts.isNotEmpty()) {
                log.warn { "Removing dead hosts: $deadHosts" }
                ledgerDb.delete(deadHosts)
                deadHosts.forEach { lastHeartbeatTimestamps.remove(it) }
            }
        }

        return Cancellable {
            runner.cancelAll()
            lastHeartbeatTimestamps.clear()
        }
    }

    fun onHeartbeatReceived(hostUuid: Uuid) {
        lastHeartbeatTimestamps[hostUuid] = timeProvider.currentTimeMillis()
    }
}