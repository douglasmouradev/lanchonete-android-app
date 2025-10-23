package com.lanchonete.app.ui.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lanchonete.app.R
import com.lanchonete.app.data.model.Product
import java.text.NumberFormat
import java.util.*

class ProductAdapter(
    private val onProductClick: (Product) -> Unit,
    private val onProductDelete: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textProductName: TextView = itemView.findViewById(R.id.textProductName)
        private val textProductCategory: TextView = itemView.findViewById(R.id.textProductCategory)
        private val textProductPrice: TextView = itemView.findViewById(R.id.textProductPrice)
        private val textProductStock: TextView = itemView.findViewById(R.id.textProductStock)
        private val textProductDescription: TextView = itemView.findViewById(R.id.textProductDescription)
        private val buttonEdit: ImageButton = itemView.findViewById(R.id.buttonEdit)
        private val buttonDelete: ImageButton = itemView.findViewById(R.id.buttonDelete)
        
        fun bind(product: Product) {
            textProductName.text = product.name
            textProductCategory.text = product.category
            textProductPrice.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(product.price)
            
            // Show description if available
            if (product.description.isNotEmpty()) {
                textProductDescription.text = product.description
                textProductDescription.visibility = View.VISIBLE
            } else {
                textProductDescription.visibility = View.GONE
            }
            
            // Show stock status
            textProductStock.text = if (product.isActive) "Ativo" else "Inativo"
            textProductStock.setTextColor(
                if (product.isActive) 
                    itemView.context.getColor(android.R.color.holo_green_dark)
                else 
                    itemView.context.getColor(android.R.color.holo_red_dark)
            )
            
            // Set click listeners
            itemView.setOnClickListener {
                onProductClick(product)
            }
            
            buttonEdit.setOnClickListener {
                onProductClick(product)
            }
            
            buttonDelete.setOnClickListener {
                onProductDelete(product)
            }
        }
    }
    
    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}
