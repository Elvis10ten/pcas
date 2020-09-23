package com.fluentbuild.pcas.middleware.resolvers

import com.fluentbuild.pcas.ledger.models.Entry
import com.fluentbuild.pcas.ledger.models.PropertyEntity
import com.fluentbuild.pcas.ledger.models.Ledger
import com.fluentbuild.pcas.utils.filterSet
import com.fluentbuild.pcas.utils.mapSet

class PropertyConflictResolver: Resolver<PropertyEntity> {

    override fun resolve(ledger: Ledger, conflicts: Set<Conflict<PropertyEntity>>): Set<Resolution> {
        return conflicts.mapSet { conflict ->
            val maxOthersPriority = conflict.others.maxOfOrNull { it.entity.priority }
            val selfEntity = conflict.self.entity

            when {
                maxOthersPriority == null && selfEntity.hasUsage -> Resolution.Connect(selfEntity)
                maxOthersPriority == null /* ^Implied && !self.isRequired */ -> Resolution.Nothing(selfEntity)
                !selfEntity.hasUsage -> Resolution.Disconnect(selfEntity)
                // After the above checks: Self has usage and there is at least one other entity with a conflict

                maxOthersPriority > selfEntity.priority -> Resolution.Disconnect(selfEntity)
                maxOthersPriority < selfEntity.priority -> Resolution.Connect(selfEntity)
                maxOthersPriority == selfEntity.priority -> resolveComplex(ledger, conflict)
                else -> throw IllegalStateException("Impossible!")
            }
        }
    }

    private fun resolveComplex(ledger: Ledger, conflict: Conflict<PropertyEntity>): Resolution {
        val apexOther = conflict.getOthersApex(ledger)
        val apexOtherHost = apexOther.host
        val apexOtherEntity = apexOther.entity
        val self = conflict.self
        val selfEntity = self.entity
        val selfHost = self.host

        val shareSinkResolution = Resolution.ShareSink(selfEntity, apexOtherHost)
        val routeSourceResolution = Resolution.RouteSource(selfEntity, apexOtherHost)

        return when {
            selfHost.isInteractive && !apexOtherHost.isInteractive -> shareSinkResolution
            apexOtherHost.isInteractive && !selfHost.isInteractive -> routeSourceResolution

            ledger.hasConnection(self) && !ledger.hasConnection(apexOther) -> shareSinkResolution
            ledger.hasConnection(apexOther) && !ledger.hasConnection(self) -> routeSourceResolution

            selfEntity.timestamp > apexOtherEntity.timestamp -> shareSinkResolution
            selfEntity.timestamp < apexOtherEntity.timestamp -> routeSourceResolution

            else -> Resolution.Ambiguous(selfEntity)
        }
    }

    private fun Conflict<PropertyEntity>.getOthersApex(ledger: Ledger): Entry<PropertyEntity> {
        return others.filterSet { it.host.isInteractive }.ifEmpty { others }
            .filterSet { ledger.hasConnection(it) }.ifEmpty { others }
            .maxByOrNull { it.entity.timestamp }!!
    }
}