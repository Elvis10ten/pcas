package com.fluentbuild.pcas.io

import com.fluentbuild.pcas.host.HostInfo
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
class Parcel(
    @ProtoId(1)
    val sender: HostInfo,
    @ProtoId(2)
    val payload: ByteArray
)