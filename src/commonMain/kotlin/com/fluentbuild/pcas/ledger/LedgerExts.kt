package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.ledger.models.Entity
import com.fluentbuild.pcas.ledger.models.Entry
import com.fluentbuild.pcas.utils.filterSet

internal fun <EntityT: Entity> Set<Entry<EntityT>>.filterByNotHost(predicateHost: HostInfo) =
    filterSet { it.host != predicateHost }

internal fun <EntityT: Entity> Set<Entry<EntityT>>.filterByHost(predicateHost: HostInfo) =
    filterSet { it.host == predicateHost }