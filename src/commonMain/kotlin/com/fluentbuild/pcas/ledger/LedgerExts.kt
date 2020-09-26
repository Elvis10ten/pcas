package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.ledger.models.Entity
import com.fluentbuild.pcas.ledger.models.Entry
import com.fluentbuild.pcas.utils.filterSet
import com.fluentbuild.pcas.utils.mapSet

internal fun <EntityT: Entity> Set<Entry<EntityT>>.filterNotHost(predicate: HostInfo) =
    filterSet { it.host != predicate }

internal fun <EntityT: Entity> Set<Entry<EntityT>>.filterHost(predicate: HostInfo) =
    filterSet { it.host == predicate }

internal fun <EntityT: Entity> Set<Entry<EntityT>>.mapToEntities() = mapSet { it.entity }

internal fun <EntityT: Entity> Set<Entry<EntityT>>.singleEntry(predicate: Entry<*>) = single { predicate == it }

internal fun <EntityT: Entity> Set<Entry<EntityT>>.singleEntity(predicate: Entity) = single { predicate == it.entity }

internal fun <EntityT: Entity> Set<Entry<EntityT>>.findEntity(predicate: Entity) = find { predicate == it.entity }