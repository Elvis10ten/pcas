package com.fluentbuild.pcas.actions

import android.content.Context
import androidx.fragment.app.Fragment
import com.fluentbuild.pcas.R
import com.google.zxing.integration.android.IntentIntegrator

class EncryptionScanKeyAction(private val fragment: Fragment): Action {

    override fun perform(context: Context) {
        val integrator = IntentIntegrator.forSupportFragment(fragment)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt(context.getString(R.string.setupEncryptScanCodePrompt))
        integrator.initiateScan()
    }
}