package com.fluentbuild.pcas.io

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class Encryption(
    private val secretKey: SecretKey
) {

    fun encrypt(payload: ByteArray): ByteArray {

    }

    fun decrypt(payload: ByteArray): ByteArray {

    }
    fun ss() {
        val cipher: Cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val parameterSpec = GCMParameterSpec(128, iv)

        //Encryption mode on!

        //Encryption mode on!
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec)

        //Encrypt the data

        //Encrypt the data
        val encryptedData: ByteArray = cipher.doFinal(data)
    }
}