package com.fluentbuild.pcas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.console.ConsoleItemProvider
import com.fluentbuild.pcas.console.ConsoleRenderer
import com.fluentbuild.pcas.di.AppComponent
import com.fluentbuild.pcas.io.Address
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.services.ServicesProvider
import com.fluentbuild.pcas.services.ServicesRenderer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_service.*
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

class MainActivity : AppCompatActivity() {

    private val secretKey = "ZbSJbpGCFi54VqzRvzDLqw=="
    lateinit var appComponent: AppComponent
    lateinit var cancellable: Cancellable
    private lateinit var servicesProvider: ServicesProvider
    private lateinit var consoleItemProvider: ConsoleItemProvider
    private lateinit var consoleRenderer: ConsoleRenderer
    private lateinit var servicesRenderer: ServicesRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        servicesProvider = ServicesProvider(
            audioServiceId = 1,
            mouseServiceId = 2,
            keypadServiceId = 3,
            healthServiceId = 4
        )

        servicesRenderer = ServicesRenderer(
            servicesProvider,
            servicesListContainer
        ) {}
        servicesRenderer.update()

        consoleItemProvider = ConsoleItemProvider()
        consoleRenderer = ConsoleRenderer(consoleItemProvider, consoleTextView)
        consoleRenderer.update()

        val audioPeripheral = Peripheral("sony", Address.Mac("38:18:4C:3E:AB:47"))

        val encodedKey: ByteArray = Base64.decode(secretKey, Base64.DEFAULT)
        val originalKey = SecretKeySpec(encodedKey, 0, encodedKey.size, "AES")

        /*appComponent = AppComponent(
                applicationContext,
                audioPeripheral,
                "host1",
                "Samsung",
                originalKey
        )
        appComponent.init()
        appComponent.middlewareModule.serviceRegistry.init()
        cancellable = appComponent.audioServiceModule.audioStateUpdater.start()*/
        Log.i("akon", "onCreated")
    }

    override fun onDestroy() {
        super.onDestroy()
        /*cancellable.cancel()
        appComponent.middlewareModule.serviceRegistry.close()
        appComponent.release()*/
    }

    private fun ss(): String {
        val secretKey = KeyGenerator.getInstance("AES").generateKey()
        return Base64.encodeToString(secretKey.encoded, Base64.DEFAULT)
    }
}