package com.fluentbuild.pcas.io

import javax.crypto.Cipher
import javax.crypto.SecretKey

class JvmPayloadCipher(
    private val key: SecretKey
): PayloadCipher {

    private val encryptCipher = ThreadLocal.withInitial { createCipher(Cipher.ENCRYPT_MODE) }
    private val decryptCipher = ThreadLocal.withInitial { createCipher(Cipher.DECRYPT_MODE) }

    override fun encrypt(payload: ByteArray): ByteArray =
        encryptCipher.get()!!.doFinal(payload)

    override fun decrypt(payload: ByteArray): ByteArray =
        decryptCipher.get()!!.doFinal(payload)

    private fun createCipher(mode: Int): Cipher {
        return Cipher.getInstance("AES/ECB/PKCS5Padding").apply {
            init(mode, key)
        }
    }
}