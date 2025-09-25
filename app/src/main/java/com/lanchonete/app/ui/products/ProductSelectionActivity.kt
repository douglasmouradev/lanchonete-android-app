package com.lanchonete.app.ui.products

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lanchonete.app.R
import com.lanchonete.app.data.model.Product
import com.lanchonete.app.databinding.ActivityProductSelectionBinding

class ProductSelectionActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityProductSelectionBinding
    private lateinit var viewModel: ProductManagementViewModel
    private lateinit var adapter: ProductSelectionAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupViewModel()
        setupRecyclerView()
        observeViewModel()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Selecionar Produto"
    }
    
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[ProductManagementViewModel::class.java]
    }
    
    private fun setupRecyclerView() {
        adapter = ProductSelectionAdapter { product ->
            val resultIntent = Intent()
            resultIntent.putExtra("product", product)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        
        binding.recyclerViewProducts.apply {
            layoutManager = LinearLayoutManager(this@ProductSelectionActivity)
            adapter = this@ProductSelectionActivity.adapter
        }
    }
    
    private fun observeViewModel() {
        viewModel.products.observe(this) { products ->
            adapter.updateItems(products)
        }
    }
    
    override fun onResume() {
        super.onResume()
        viewModel.loadAllProducts()
    }
}

class ProductSelectionAdapter(
    private val onProductSelected: (Product) -> Unit
) : androidx.recyclerview.widget.RecyclerView.Adapter<ProductSelectionAdapter.ProductViewHolder>() {
    
    private var products = mutableListOf<Product>()
    
    fun updateItems(newProducts: List<Product>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ProductViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ProductViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }
    
    override fun getItemCount(): Int = products.size
    
    inner class ProductViewHolder(itemView: android.view.View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        private val text1: android.widget.TextView = itemView.findViewById(android.R.id.text1)
        private val text2: android.widget.TextView = itemView.findViewById(android.R.id.text2)
        
        fun bind(product: Product) {
            text1.text = product.name
            text2.text = "R$ ${String.format("%.2f", product.price)}"
            
            itemView.setOnClickListener {
                onProductSelected(product)
            }
        }
    }
}
