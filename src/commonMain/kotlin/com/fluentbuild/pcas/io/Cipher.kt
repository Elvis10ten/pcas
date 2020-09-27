package com.fluentbuild.pcas.io

interface Cipher {

    fun encrypt(payload: ByteArray): ByteArray

    fun decrypt(payload: ByteArray): ByteArray
}