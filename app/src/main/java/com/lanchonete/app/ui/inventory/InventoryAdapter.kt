package com.lanchonete.app.ui.inventory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lanchonete.app.R
import com.lanchonete.app.data.model.InventoryItem

class InventoryAdapter(
    private val onRestockClick: (InventoryItem) -> Unit,
    private val onAdjustStockClick: (InventoryItem) -> Unit
) : RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder>() {
    
    private var inventoryItems = mutableListOf<InventoryItem>()
    
    fun updateItems(newItems: List<InventoryItem>) {
        inventoryItems.clear()
        inventoryItems.addAll(newItems)
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inventory, parent, false)
        return InventoryViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        holder.bind(inventoryItems[position])
    }
    
    override fun getItemCount(): Int = inventoryItems.size
    
    inner class InventoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textProductName: TextView = itemView.findViewById(R.id.textProductName)
        private val textCurrentStock: TextView = itemView.findViewById(R.id.textCurrentStock)
        private val textMinStock: TextView = itemView.findViewById(R.id.textMinStock)
        private val textMaxStock: TextView = itemView.findViewById(R.id.textMaxStock)
        private val textStockStatus: TextView = itemView.findViewById(R.id.textStockStatus)
        private val buttonRestock: ImageButton = itemView.findViewById(R.id.buttonRestock)
        private val buttonAdjust: ImageButton = itemView.findViewById(R.id.buttonAdjust)
        
        fun bind(item: InventoryItem) {
            textProductName.text = item.productName
            textCurrentStock.text = "${item.currentStock} ${item.unit}"
            textMinStock.text = "Mín: ${item.minStock}"
            textMaxStock.text = "Máx: ${item.maxStock}"
            
            // Set stock status and color
            when {
                item.currentStock == 0 -> {
                    textStockStatus.text = "SEM ESTOQUE"
                    textStockStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.error))
                }
                item.currentStock <= item.minStock -> {
                    textStockStatus.text = "ESTOQUE BAIXO"
                    textStockStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.warning))
                }
                else -> {
                    textStockStatus.text = "OK"
                    textStockStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.success))
                }
            }
            
            buttonRestock.setOnClickListener {
                onRestockClick(item)
            }
            
            buttonAdjust.setOnClickListener {
                onAdjustStockClick(item)
            }
        }
    }
}
