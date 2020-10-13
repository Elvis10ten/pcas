package com.fluentbuild.pcas.views

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fluentbuild.pcas.R
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.utils.inflate
import com.fluentbuild.pcas.utils.setVisible
import kotlinx.android.synthetic.main.item_peripheral.view.*
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

@SuppressLint("ViewConstructor")
class PeripheralListView(
    context: Context,
    private val peripherals: List<Peripheral>,
    selectedPeripheral: Peripheral?,
    private val onClick: (Peripheral) -> Unit
): RecyclerView(context) {

    private var clickedPeripheral by observable(
        selectedPeripheral,
        { _: KProperty<*>, oldValue: Peripheral?, newValue: Peripheral? ->
            adapter!!.notifyItemChanged(peripherals.indexOf(oldValue))
            adapter!!.notifyItemChanged(peripherals.indexOf(newValue))
        }
    )

    init {
        adapter = PeripheralAdapter()
        layoutManager = LinearLayoutManager(context)
        addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    inner class PeripheralAdapter: RecyclerView.Adapter<PeripheralViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            PeripheralViewHolder(context.inflate(R.layout.item_peripheral, parent))

        override fun onBindViewHolder(holder: PeripheralViewHolder, position: Int) {
            holder.bind(peripherals[position])
        }

        override fun getItemCount() = peripherals.size
    }

    inner class PeripheralViewHolder(itemView: View): ViewHolder(itemView) {

        fun bind(peripheral: Peripheral) {
            itemView.peripheralNameTextView.text = peripheral.name
            itemView.peripheralIconView.setVisible(clickedPeripheral == peripheral)

            itemView.setOnClickListener {
                clickedPeripheral = peripheral
                onClick(peripheral)
            }
        }
    }
}