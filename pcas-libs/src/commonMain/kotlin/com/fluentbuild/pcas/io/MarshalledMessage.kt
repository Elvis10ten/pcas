package com.fluentbuild.pcas.io

internal typealias MarshalledMessage = ByteArray

// Marshalled message size is always smaller than the byte buffer of the DatagramPacket
internal typealias MarshalledMessageSize = Int