package com.lanchonete.app.ui.inventory

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.lanchonete.app.data.model.InventoryItem

class RestockDialogFragment : DialogFragment() {
    
    private var inventoryItem: InventoryItem? = null
    private var onRestockConfirmed: ((Int) -> Unit)? = null
    
    companion object {
        fun newInstance(inventoryItem: InventoryItem, onRestockConfirmed: (Int) -> Unit): RestockDialogFragment {
            return RestockDialogFragment().apply {
                this.inventoryItem = inventoryItem
                this.onRestockConfirmed = onRestockConfirmed
            }
        }
    }
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Repor Estoque")
        builder.setMessage("Funcionalidade em desenvolvimento")
        
        builder.setPositiveButton("OK") { _, _ ->
            onRestockConfirmed?.invoke(10) // Default quantity
        }
        
        builder.setNegativeButton("Cancelar", null)
        
        return builder.create()
    }
}
