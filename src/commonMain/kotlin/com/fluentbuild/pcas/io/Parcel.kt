package com.fluentbuild.pcas.io

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
class Parcel(
    @ProtoId(1)
    val payload: ByteArray
)