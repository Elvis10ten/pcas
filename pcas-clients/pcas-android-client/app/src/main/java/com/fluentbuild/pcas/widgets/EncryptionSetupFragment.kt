package com.fluentbuild.pcas.widgets

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import com.fluentbuild.pcas.R
import com.fluentbuild.pcas.adapters.EncryptionSetupAdapter
import com.fluentbuild.pcas.models.EncryptionSetupModel
import com.fluentbuild.pcas.widgets.foundation.Widget
import com.fluentbuild.pcas.widgets.foundation.init
import com.fluentbuild.pcas.widgets.views.setVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.setup_security_fragment.*

class EncryptionSetupFragment: BottomSheetDialogFragment(), Widget<EncryptionSetupModel> {

    private lateinit var currentModel: EncryptionSetupModel

    override val adapter by lazy { EncryptionSetupAdapter(this, requireContext()) }

    override fun update(model: EncryptionSetupModel) {
        currentModel = model
        setupTitleTextView.text = model.title
        setupInstructionTextView.text = model.instruction.parseAsHtml()
        setupInstructionTextView.setVisible(true)

        setupQrCodeView.setImageBitmap(model.qrCode)
        setupQrCodeView.setVisible(model.hasKey)

        setupCreateKeyButton.setOnClickListener { model.onCreateKeyClicked() }
        setupCreateKeyButton.setVisible(!model.hasKey)

        setupScanKeyButton.setOnClickListener { model.onScanCodeClicked() }

        setupDisableButton.setOnClickListener { model.onDisableClicked() }
        setupDisableButton.setVisible(model.hasKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.setup_security_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(requireContext(), viewLifecycleOwner.lifecycle)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK && data != null) {
            if(currentModel.onQrCodeScanned(data)) {
                dismiss()
            }
        }
    }

    companion object {

        fun newInstance(): EncryptionSetupFragment {
            return EncryptionSetupFragment()
        }
    }
}