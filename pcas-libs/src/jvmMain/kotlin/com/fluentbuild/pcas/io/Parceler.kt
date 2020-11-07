package com.fluentbuild.pcas.io

import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import com.fluentbuild.pcas.io.transport.TransportConfig.OFFSET_ZERO

internal class Parceler(
	private val networkKey: ByteArray?,
	private val random: SecureRandom,
	private val bufferPool: BufferObjectPool
) {

    private val secretKey by lazy { SecretKeySpec(networkKey!!, OFFSET_ZERO, networkKey.size, KEY_ALGORITHM) }
    private val cipherCache = ThreadLocal.withInitial { Cipher.getInstance(CIPHER_TRANSFORMATION) }
    private val cipher get() = cipherCache.get()!!

    private val ivCache = ThreadLocal.withInitial { ByteArray(IV_SIZE) }
    private val iv get() = ivCache.get()!!

    inline fun <T> parcel(message: ByteArray, messageSize: Int, parcelTransform: (Parcel, Int) -> T): T {
        if(networkKey == null) return parcelTransform(message, messageSize)

        val iv = init(Cipher.ENCRYPT_MODE) { random.nextBytes(it) }
        val parcel = bufferPool.allocate()
        val parcelSize = cipher.doFinal(message, OFFSET_ZERO, messageSize, parcel, IV_SIZE)
        System.arraycopy(iv, OFFSET_ZERO, parcel, OFFSET_ZERO, IV_SIZE)
        val parcelWithIvSize = parcelSize + IV_SIZE
        return parcelTransform(parcel, parcelWithIvSize)
    }

    inline fun <T> unparcel(parcel: ByteArray, parcelSize: Int, messageTransform: (Message, Int) -> T): T {
        if(networkKey == null) return messageTransform(parcel, parcelSize)

        init(Cipher.DECRYPT_MODE) {
            System.arraycopy(parcel, OFFSET_ZERO, it, OFFSET_ZERO, IV_SIZE)
        }

        val message = bufferPool.allocate()
        val parcelWithoutIvSize = parcelSize - IV_SIZE
        val messageSize = cipher.doFinal(parcel, IV_SIZE, parcelWithoutIvSize, message, OFFSET_ZERO)
        return messageTransform(message, messageSize)
    }

    private inline fun init(cipherMode: Int, setupIv: (ByteArray) -> Unit): ByteArray {
        return iv.also {
            setupIv(it)
            cipher.init(cipherMode, secretKey, GCMParameterSpec(AUTH_TAG_SIZE, it))
        }
    }

    companion object {

        private const val CIPHER_TRANSFORMATION = "AES/GCM/NoPadding"
        const val KEY_ALGORITHM = "AES"
        private const val IV_SIZE = 12
        private const val AUTH_TAG_SIZE = 128
    }
}

private typealias Parcel = ByteArray

private typealias Message = ByteArray