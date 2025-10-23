package com.lanchonete.app.ui.products

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.lanchonete.app.R
import com.lanchonete.app.data.model.Product
import java.util.*

class ProductDialogFragment : DialogFragment() {
    
    private var product: Product? = null
    private var onProductSaved: ((Product, Int) -> Unit)? = null
    
    // Views
    private lateinit var editTextProductName: TextInputEditText
    private lateinit var editTextProductDescription: TextInputEditText
    private lateinit var editTextProductCategory: TextInputEditText
    private lateinit var editTextProductPrice: TextInputEditText
    private lateinit var editTextProductCost: TextInputEditText
    private lateinit var editTextProductBarcode: TextInputEditText
    private lateinit var editTextInitialStock: TextInputEditText
    private lateinit var switchProductActive: com.google.android.material.switchmaterial.SwitchMaterial
    
    companion object {
        fun newInstance(onProductSaved: (Product, Int) -> Unit): ProductDialogFragment {
            return ProductDialogFragment().apply {
                this.onProductSaved = onProductSaved
            }
        }
        
        fun newInstance(product: Product, onProductSaved: (Product, Int) -> Unit): ProductDialogFragment {
            return ProductDialogFragment().apply {
                this.product = product
                this.onProductSaved = onProductSaved
            }
        }
    }
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(if (product == null) "Adicionar Produto" else "Editar Produto")
        
        // Create the form view
        val view = layoutInflater.inflate(R.layout.dialog_product, null)
        initializeViews(view)
        populateFields()
        
        builder.setView(view)
        
        builder.setPositiveButton("Salvar") { _, _ ->
            if (validateAndSaveProduct()) {
                dismiss()
            }
        }
        
        builder.setNegativeButton("Cancelar") { _, _ ->
            dismiss()
        }
        
        return builder.create()
    }
    
    private fun initializeViews(view: View) {
        editTextProductName = view.findViewById(R.id.editTextProductName)
        editTextProductDescription = view.findViewById(R.id.editTextProductDescription)
        editTextProductCategory = view.findViewById(R.id.editTextProductCategory)
        editTextProductPrice = view.findViewById(R.id.editTextProductPrice)
        editTextProductCost = view.findViewById(R.id.editTextProductCost)
        editTextProductBarcode = view.findViewById(R.id.editTextProductBarcode)
        editTextInitialStock = view.findViewById(R.id.editTextInitialStock)
        switchProductActive = view.findViewById(R.id.switchProductActive)
    }
    
    private fun populateFields() {
        product?.let { product ->
            editTextProductName.setText(product.name)
            editTextProductDescription.setText(product.description)
            editTextProductCategory.setText(product.category)
            editTextProductPrice.setText(product.price.toString())
            editTextProductCost.setText(product.cost.toString())
            editTextProductBarcode.setText(product.barcode)
            switchProductActive.isChecked = product.isActive
        }
    }
    
    private fun validateAndSaveProduct(): Boolean {
        // Clear previous errors
        clearErrors()
        
        var isValid = true
        
        // Validate required fields
        if (TextUtils.isEmpty(editTextProductName.text)) {
            editTextProductName.error = "Nome do produto é obrigatório"
            editTextProductName.requestFocus()
            isValid = false
        } else if (editTextProductName.text.toString().trim().length < 2) {
            editTextProductName.error = "Nome deve ter pelo menos 2 caracteres"
            editTextProductName.requestFocus()
            isValid = false
        }
        
        if (TextUtils.isEmpty(editTextProductCategory.text)) {
            editTextProductCategory.error = "Categoria é obrigatória"
            editTextProductCategory.requestFocus()
            isValid = false
        } else if (editTextProductCategory.text.toString().trim().length < 2) {
            editTextProductCategory.error = "Categoria deve ter pelo menos 2 caracteres"
            editTextProductCategory.requestFocus()
            isValid = false
        }
        
        if (TextUtils.isEmpty(editTextProductPrice.text)) {
            editTextProductPrice.error = "Preço é obrigatório"
            editTextProductPrice.requestFocus()
            isValid = false
        }
        
        // Validate price
        val priceText = editTextProductPrice.text.toString()
        val price = try {
            priceText.toDouble()
        } catch (e: NumberFormatException) {
            editTextProductPrice.error = "Preço inválido"
            editTextProductPrice.requestFocus()
            isValid = false
        }
        
        if (isValid && price <= 0) {
            editTextProductPrice.error = "Preço deve ser maior que zero"
            editTextProductPrice.requestFocus()
            isValid = false
        }
        
        if (isValid && price > 999999.99) {
            editTextProductPrice.error = "Preço muito alto (máximo: R$ 999.999,99)"
            editTextProductPrice.requestFocus()
            isValid = false
        }
        
        // Validate cost (optional)
        var cost = 0.0
        if (!TextUtils.isEmpty(editTextProductCost.text)) {
            cost = try {
                editTextProductCost.text.toString().toDouble()
            } catch (e: NumberFormatException) {
                editTextProductCost.error = "Custo inválido"
                editTextProductCost.requestFocus()
                isValid = false
            }
            
            if (isValid && cost < 0) {
                editTextProductCost.error = "Custo não pode ser negativo"
                editTextProductCost.requestFocus()
                isValid = false
            }
            
            if (isValid && cost > 999999.99) {
                editTextProductCost.error = "Custo muito alto (máximo: R$ 999.999,99)"
                editTextProductCost.requestFocus()
                isValid = false
            }
        }
        
        // Validate initial stock (optional)
        var initialStock = 0
        if (!TextUtils.isEmpty(editTextInitialStock.text)) {
            initialStock = try {
                editTextInitialStock.text.toString().toInt()
            } catch (e: NumberFormatException) {
                editTextInitialStock.error = "Estoque inválido"
                editTextInitialStock.requestFocus()
                isValid = false
            }
            
            if (isValid && initialStock < 0) {
                editTextInitialStock.error = "Estoque não pode ser negativo"
                editTextInitialStock.requestFocus()
                isValid = false
            }
            
            if (isValid && initialStock > 999999) {
                editTextInitialStock.error = "Estoque muito alto (máximo: 999.999)"
                editTextInitialStock.requestFocus()
                isValid = false
            }
        }
        
        // Validate barcode (optional)
        if (!TextUtils.isEmpty(editTextProductBarcode.text)) {
            val barcode = editTextProductBarcode.text.toString().trim()
            if (barcode.length < 8 || barcode.length > 13) {
                editTextProductBarcode.error = "Código de barras deve ter entre 8 e 13 dígitos"
                editTextProductBarcode.requestFocus()
                isValid = false
            }
        }
        
        if (!isValid) {
            return false
        }
        
        // Create or update product
        val productId = product?.id ?: UUID.randomUUID().toString()
        val currentDate = Date()
        
        val newProduct = Product(
            id = productId,
            name = editTextProductName.text.toString().trim(),
            description = editTextProductDescription.text.toString().trim(),
            category = editTextProductCategory.text.toString().trim(),
            price = price,
            cost = cost,
            barcode = editTextProductBarcode.text.toString().trim(),
            isActive = switchProductActive.isChecked,
            createdAt = product?.createdAt ?: currentDate,
            updatedAt = currentDate
        )
        
        // Call the callback with product and initial stock
        onProductSaved?.invoke(newProduct, initialStock)
        
        Toast.makeText(context, "Produto salvo com sucesso!", Toast.LENGTH_SHORT).show()
        return true
    }
    
    private fun clearErrors() {
        editTextProductName.error = null
        editTextProductDescription.error = null
        editTextProductCategory.error = null
        editTextProductPrice.error = null
        editTextProductCost.error = null
        editTextProductBarcode.error = null
        editTextInitialStock.error = null
    }
}
