package com.lanchonete.app.ui.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lanchonete.app.R
import com.lanchonete.app.data.model.Product
import java.text.NumberFormat
import java.util.*

class ProductAdapter(
    private val onEditClick: (Product) -> Unit,
    private val onDeleteClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    
    private var products = mutableListOf<Product>()
    
    fun updateItems(newProducts: List<Product>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }
    
    override fun getItemCount(): Int = products.size
    
    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textProductName: TextView = itemView.findViewById(R.id.textProductName)
        private val textProductCategory: TextView = itemView.findViewById(R.id.textProductCategory)
        private val textProductPrice: TextView = itemView.findViewById(R.id.textProductPrice)
        private val textProductStock: TextView = itemView.findViewById(R.id.textProductStock)
        private val buttonEdit: ImageButton = itemView.findViewById(R.id.buttonEdit)
        private val buttonDelete: ImageButton = itemView.findViewById(R.id.buttonDelete)
        
        fun bind(product: Product) {
            textProductName.text = product.name
            textProductCategory.text = product.category
            textProductPrice.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(product.price)
            textProductStock.text = "Em estoque" // TODO: Get actual stock info
            
            buttonEdit.setOnClickListener {
                onEditClick(product)
            }
            
            buttonDelete.setOnClickListener {
                onDeleteClick(product)
            }
        }
    }
}
