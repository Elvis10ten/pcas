package com.fluentbuild.pcas.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.fluentbuild.pcas.EngineState
import com.fluentbuild.pcas.R
import com.fluentbuild.pcas.actions.EncryptionSetKeyAction
import com.fluentbuild.pcas.actions.EncryptionScanKeyAction
import com.fluentbuild.pcas.io.KeyTool
import com.fluentbuild.pcas.models.EncryptionSetupModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.BarcodeEncoder

class EncryptionSetupAdapter(
    private val fragment: Fragment,
    private val context: Context
): Adapter<EncryptionSetupModel> {

    private val barcodeEncoder = BarcodeEncoder()
    private val qrCodeSize = context.resources.getDimensionPixelSize(R.dimen.qrCodeSize)

    override fun toModel(state: EngineState): EncryptionSetupModel {
        val hasNetworkKey = state.hostConfig.networkKey != null
        return EncryptionSetupModel(
            title = context.getString(R.string.setupEncryptSecurityTitle),
            instruction = state.getDescription(hasNetworkKey),
            qrCode = state.hostConfig.networkKey?.toQrCode,
            hasKey = hasNetworkKey,
            onDisableClicked = { EncryptionSetKeyAction(null).perform(context) },
            onCreateKeyClicked = { EncryptionSetKeyAction(KeyTool.generate().encoded).perform(context) },
            onScanCodeClicked = { EncryptionScanKeyAction(fragment).perform(context) },
            onQrCodeScanned = onQrCodeScanned
        )
    }

    private val onQrCodeScanned = { data: Intent ->
        try {
            val scannedResult = data.getStringExtra(Intents.Scan.RESULT)!!
            val networkKey = KeyTool.toSecretKey(scannedResult).encoded
            EncryptionSetKeyAction(networkKey).perform(context)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, R.string.setupEncryptInvalidKey, Toast.LENGTH_LONG).show()
            false
        }
    }
    
    private fun EngineState.getDescription(hasNetworkKey: Boolean): String {
        return if(hasNetworkKey) {
            context.getString(R.string.setupEncryptShareInstruction, hostConfig.name)
        } else {
            context.getString(R.string.setupEncryptInitialInstruction)
        }
    }

    private inline val ByteArray.toQrCode: Bitmap get() {
        return barcodeEncoder.encodeBitmap(
            KeyTool.toString(this),
            BarcodeFormat.QR_CODE,
            qrCodeSize,
            qrCodeSize
        )
    }
}