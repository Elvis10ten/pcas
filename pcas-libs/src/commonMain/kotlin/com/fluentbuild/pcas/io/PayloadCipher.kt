package com.fluentbuild.pcas.io

internal interface PayloadCipher {

    fun encrypt(payload: ByteArray): ByteArray

    fun decrypt(payload: ByteArray): ByteArray
}