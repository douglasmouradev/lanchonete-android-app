package com.lanchonete.app.ui.products

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.lanchonete.app.R
import com.lanchonete.app.data.model.Product
import java.util.*

class ProductDialogFragment : DialogFragment() {
    
    private var product: Product? = null
    private var onProductSaved: ((Product) -> Unit)? = null
    
    companion object {
        fun newInstance(onProductSaved: (Product) -> Unit): ProductDialogFragment {
            return ProductDialogFragment().apply {
                this.onProductSaved = onProductSaved
            }
        }
        
        fun newInstance(product: Product, onProductSaved: (Product) -> Unit): ProductDialogFragment {
            return ProductDialogFragment().apply {
                this.product = product
                this.onProductSaved = onProductSaved
            }
        }
    }
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(if (product == null) "Adicionar Produto" else "Editar Produto")
        
        // Create a simple form
        val view = layoutInflater.inflate(R.layout.dialog_product, null)
        builder.setView(view)
        
        builder.setPositiveButton("Salvar") { _, _ ->
            // TODO: Implement product saving
            Toast.makeText(context, "Funcionalidade em desenvolvimento", Toast.LENGTH_SHORT).show()
        }
        
        builder.setNegativeButton("Cancelar", null)
        
        return builder.create()
    }
}
