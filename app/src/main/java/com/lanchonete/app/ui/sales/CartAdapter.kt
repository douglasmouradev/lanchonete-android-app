package com.lanchonete.app.ui.sales

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lanchonete.app.R
import java.text.NumberFormat
import java.util.*

class CartAdapter(
    private val onQuantityChanged: (Int, Int) -> Unit,
    private val onItemRemoved: (Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    
    private var cartItems = mutableListOf<CartItem>()
    
    fun updateItems(newItems: List<CartItem>) {
        cartItems.clear()
        cartItems.addAll(newItems)
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position], position)
    }
    
    override fun getItemCount(): Int = cartItems.size
    
    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textProductName: TextView = itemView.findViewById(R.id.textProductName)
        private val textUnitPrice: TextView = itemView.findViewById(R.id.textUnitPrice)
        private val textQuantity: TextView = itemView.findViewById(R.id.textQuantity)
        private val textTotalPrice: TextView = itemView.findViewById(R.id.textTotalPrice)
        private val buttonDecrease: ImageButton = itemView.findViewById(R.id.buttonDecrease)
        private val buttonIncrease: ImageButton = itemView.findViewById(R.id.buttonIncrease)
        private val buttonRemove: ImageButton = itemView.findViewById(R.id.buttonRemove)
        
        fun bind(item: CartItem, position: Int) {
            textProductName.text = item.productName
            textUnitPrice.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(item.unitPrice)
            textQuantity.text = item.quantity.toString()
            textTotalPrice.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(item.totalPrice)
            
            buttonDecrease.setOnClickListener {
                onQuantityChanged(position, item.quantity - 1)
            }
            
            buttonIncrease.setOnClickListener {
                onQuantityChanged(position, item.quantity + 1)
            }
            
            buttonRemove.setOnClickListener {
                onItemRemoved(position)
            }
        }
    }
}
