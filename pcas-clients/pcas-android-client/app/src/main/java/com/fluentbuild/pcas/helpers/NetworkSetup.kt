package com.fluentbuild.pcas.helpers

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import com.fluentbuild.pcas.R
import com.fluentbuild.pcas.appComponent
import com.fluentbuild.pcas.io.KeyTool
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class NetworkSetup(private val context: Context) {

    private val barcodeEncoder = BarcodeEncoder()
    private val qrCodeSize = context.resources.getDimensionPixelSize(R.dimen.qrCodeSize)
    private val hostConfigStore get() = context.appComponent.hostConfigStore

    @Throws(Exception::class)
    fun getSetupInfo(): Info {
        val hostConfig = hostConfigStore.getPartial()
        val bitmap = barcodeEncoder.encodeBitmap(
            Base64.encodeToString(hostConfig.networkKey, Base64.DEFAULT),
            BarcodeFormat.QR_CODE,
            qrCodeSize,
            qrCodeSize
        )

        return Info(hostConfig.name, bitmap)
    }

    fun setup(networkKey: String) {
        hostConfigStore.setNetworkKey(KeyTool.toSecretKey(networkKey).encoded)
    }

    data class Info(
        val name: String,
        val qrCodeBitmap: Bitmap
    )
}