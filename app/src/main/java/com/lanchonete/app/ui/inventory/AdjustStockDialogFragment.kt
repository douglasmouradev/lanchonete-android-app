package com.lanchonete.app.ui.inventory

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.lanchonete.app.data.model.InventoryItem

class AdjustStockDialogFragment : DialogFragment() {
    
    private var inventoryItem: InventoryItem? = null
    private var onStockAdjusted: ((Int) -> Unit)? = null
    
    companion object {
        fun newInstance(inventoryItem: InventoryItem, onStockAdjusted: (Int) -> Unit): AdjustStockDialogFragment {
            return AdjustStockDialogFragment().apply {
                this.inventoryItem = inventoryItem
                this.onStockAdjusted = onStockAdjusted
            }
        }
    }
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Ajustar Estoque")
        builder.setMessage("Funcionalidade em desenvolvimento")
        
        builder.setPositiveButton("OK") { _, _ ->
            onStockAdjusted?.invoke(inventoryItem?.currentStock ?: 0)
        }
        
        builder.setNegativeButton("Cancelar", null)
        
        return builder.create()
    }
}
