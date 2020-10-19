package com.fluentbuild.pcas.widgets.views

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fluentbuild.pcas.R
import com.fluentbuild.pcas.models.PeripheralModel
import kotlinx.android.synthetic.main.item_peripheral.view.*

class PeripheralListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): RecyclerView(context, attrs, defStyle) {

    private var peripherals = listOf<PeripheralModel>()
    private lateinit var onClick: (PeripheralModel) -> Unit

    init {
        adapter = PeripheralAdapter()
        layoutManager = LinearLayoutManager(context)

        val contextWrapper = ContextThemeWrapper(context, R.style.AppTheme)
        addItemDecoration(DividerItemDecoration(contextWrapper, DividerItemDecoration.VERTICAL))
    }

    fun update(newPeripherals: List<PeripheralModel>, newOnClick: (PeripheralModel) -> Unit) {
        val diffResult = DiffUtil.calculateDiff(PeripheralDiffCallback(peripherals, newPeripherals))
        peripherals = newPeripherals
        onClick = newOnClick
        diffResult.dispatchUpdatesTo(adapter!!)
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

        fun bind(peripheral: PeripheralModel) {
            itemView.peripheralNameTextView.text = peripheral.name
            itemView.peripheralIconView.setVisible(peripheral.isSelected)
            itemView.setOnClickListener { onClick(peripheral) }
        }
    }

    inner class PeripheralDiffCallback(
        private val oldList: List<PeripheralModel>,
        private val newList: List<PeripheralModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].address == newList[newItemPosition].address
        }

        override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
            val oldPeripheral = oldList[oldPosition]
            val newPeripheral = newList[newPosition]

            return oldPeripheral.name == newPeripheral.name &&
                    oldPeripheral.isSelected == newPeripheral.isSelected
        }
    }
}