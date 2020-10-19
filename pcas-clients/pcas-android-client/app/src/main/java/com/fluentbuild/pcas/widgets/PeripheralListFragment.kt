package com.fluentbuild.pcas.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.fluentbuild.pcas.R
import com.fluentbuild.pcas.adapters.PeripheralListAdapter
import com.fluentbuild.pcas.models.PeripheralListModel
import com.fluentbuild.pcas.services.ServiceClass
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.peripheral_list_fragment.*

class PeripheralListFragment: BottomSheetDialogFragment(), Widget<PeripheralListModel> {

    override val adapter by lazy {
        PeripheralListAdapter(
            requireContext(),
            requireArguments().getSerializable(ARG_SERVICE_CLASS) as ServiceClass
        )
    }

    override fun update(model: PeripheralListModel) {
        isCancelable = model.isCancellable
        peripheralListView.update(model.peripherals) {
            model.selectAction(it).perform(requireContext())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initFragment()
        return inflater.inflate(R.layout.peripheral_list_fragment, container, false)
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