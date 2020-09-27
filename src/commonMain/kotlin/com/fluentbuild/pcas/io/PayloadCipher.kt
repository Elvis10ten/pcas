package com.fluentbuild.pcas.io

interface PayloadCipher {

    fun encrypt(payload: ByteArray): ByteArray

    fun decrypt(payload: ByteArray): ByteArray
}