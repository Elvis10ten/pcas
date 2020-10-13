package com.fluentbuild.pcas.io

import java.util.*
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

object KeyTool {

	fun toSecretKey(text: String): SecretKey {
		val encodedKey = Base64.getDecoder().decode(text)
		return SecretKeySpec(encodedKey, 0, encodedKey.size, Parceler.KEY_ALGORITHM)
	}

	fun generate(): SecretKey {
		return KeyGenerator.getInstance(Parceler.KEY_ALGORITHM).generateKey()
	}
}