package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.ThreadRunner
import com.fluentbuild.pcas.host.HostUuid
import com.fluentbuild.pcas.utils.TimeProvider
import com.fluentbuild.pcas.utils.Timestamp
import com.fluentbuild.pcas.utils.logger

internal class LedgerWatchdog(
    private val ledgerDb: LedgerDb,
    private val messageSender: LedgerMessageSender,
    private val runner: ThreadRunner,
    private val timeProvider: TimeProvider
) {

    private val log by logger()
    private val lastHeartbeatTimestamps = mutableMapOf<HostUuid, Timestamp>()

    // todo: Factor in interactivity for TTL. Device that are interactive should have shorter TTL, because
    // there is no OS throttling and more possibility of changes occurring.
    // Battery level can be used to build a probability model for non-interactive, given that changes will only occur
    // for: Disconnected from peripheral, host shutdown, or Bluetooth failure
    fun run(): Cancellable {
        runner.runOnMainRepeating(HEARTBEAT_INTERVAL_MILLIS) {
            messageSender.sendHeartbeat()

            val currentTimestamp = timeProvider.currentTimeMillis()
            val deadHosts = lastHeartbeatTimestamps.filter { currentTimestamp - it.value > HOST_TTL_MILLIS }.keys

            if(deadHosts.isNotEmpty()) {
                log.info { "Removing dead hosts: $deadHosts" }
                ledgerDb.delete(deadHosts)
                deadHosts.forEach { lastHeartbeatTimestamps.remove(it) }
            }
        }

        return Cancellable {
            runner.cancelAll()
            lastHeartbeatTimestamps.clear()
        }
    }

    fun onHostHeartbeatReceived(hostUuid: HostUuid) {
        lastHeartbeatTimestamps[hostUuid] = timeProvider.currentTimeMillis()
    }
}