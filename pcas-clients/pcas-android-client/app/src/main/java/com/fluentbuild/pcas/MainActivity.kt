package com.fluentbuild.pcas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.di.AppComponent
import com.fluentbuild.pcas.io.Address
import com.fluentbuild.pcas.peripheral.Peripheral
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

class MainActivity : AppCompatActivity() {

    private val secretKey = "ZbSJbpGCFi54VqzRvzDLqw=="
    lateinit var appComponent: AppComponent
    lateinit var cancellable: Cancellable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val audioPeripheral = Peripheral("sony", Address.Mac("38:18:4C:3E:AB:47"))

        val encodedKey: ByteArray = Base64.decode(secretKey, Base64.DEFAULT)
        val originalKey = SecretKeySpec(encodedKey, 0, encodedKey.size, "AES")

        appComponent = AppComponent(
                applicationContext,
                audioPeripheral,
                "host1",
                "Samsung",
                originalKey
        )
        appComponent.init()
        appComponent.middlewareModule.serviceRegistry.init()
        cancellable = appComponent.audioServiceModule.audioStateUpdater.start()
        Log.i("akon", "onCreated")
    }

    override fun onDestroy() {
        super.onDestroy()
        cancellable.cancel()
        appComponent.middlewareModule.serviceRegistry.close()
        appComponent.release()
    }

    private fun ss(): String {
        val secretKey = KeyGenerator.getInstance("AES").generateKey()
        return Base64.encodeToString(secretKey.encoded, Base64.DEFAULT)
    }
}