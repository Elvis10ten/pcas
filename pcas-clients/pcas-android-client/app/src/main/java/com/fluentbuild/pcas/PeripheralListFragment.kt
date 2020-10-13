package com.fluentbuild.pcas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.services.ServiceClass
import com.fluentbuild.pcas.utils.Delegates.observable
import com.fluentbuild.pcas.views.PeripheralListView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.peripheral_list_fragment.*
import kotlinx.android.synthetic.main.peripheral_list_fragment.view.*

class PeripheralListFragment: BottomSheetDialogFragment() {

    private val hostConfigStore get() = requireContext().appComponent.hostConfigStore
    private val peripheralRepository get() = requireContext().appComponent.peripheralRepository

    private var currentSelectedPeripheral: Peripheral? by observable(null) { newValue: Peripheral? ->
        if(newValue == null) {
            peripheralSelectButtonView.isEnabled = false
            peripheralSelectButtonView.setText(R.string.selectPeripheralActionDisabled)
            peripheralSelectButtonView.setOnClickListener(null)
        } else {
            peripheralSelectButtonView.isEnabled = true
            peripheralSelectButtonView.setText(R.string.selectPeripheralActionEnabled)
            peripheralSelectButtonView.setOnClickListener {
                hostConfigStore.setAudioPeripheral(newValue)
                dismiss()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = hostConfigStore.isPeripheralsSetup()
        return inflater.inflate(R.layout.peripheral_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val peripherals = peripheralRepository.getAudioPeripherals().toList()
        val selectedPeripheral = hostConfigStore.getPartial().audioPeripheral

        val peripheralList = PeripheralListView(
            requireContext(),
            peripherals,
            selectedPeripheral
        ) {
            currentSelectedPeripheral = it
        }

        view.peripheralListContainer.addView(peripheralList)
        currentSelectedPeripheral = null
    }

    companion object {

        private const val ARG_SERVICE_CLASS = "ARG_SERVICE_CLASS"

        fun newInstance(serviceClass: ServiceClass): PeripheralListFragment {
            return PeripheralListFragment().apply {
                arguments = bundleOf(ARG_SERVICE_CLASS to serviceClass)
            }
        }
    }
}