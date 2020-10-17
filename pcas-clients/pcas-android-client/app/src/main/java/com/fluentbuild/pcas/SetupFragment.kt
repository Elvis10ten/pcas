package com.fluentbuild.pcas

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.parseAsHtml
import com.fluentbuild.pcas.helpers.NetworkSetup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.setup_fragment.*

class SetupFragment: BottomSheetDialogFragment() {

    private val networkSetup by lazy { NetworkSetup(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.setup_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupScanButton.setOnClickListener {
            val integrator = IntentIntegrator.forSupportFragment(this)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            integrator.setPrompt(getString(R.string.setupScanCodePrompt))
            integrator.initiateScan()
        }

        try {
            val setupInfo = networkSetup.getSetupInfo()
            val instruction = getString(R.string.setupInstruction, setupInfo.name).parseAsHtml()
            setupInstructionTextView.text = instruction
            setupImageView.setImageBitmap(setupInfo.qrCodeBitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), R.string.setupQrCodeError, Toast.LENGTH_LONG).show()
            dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if(result == null || result.contents == null) {
            return
        }

        try {
            networkSetup.setup(result.contents)
            dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), R.string.setupInvalidQrCodeError, Toast.LENGTH_LONG).show()
        }
    }
}