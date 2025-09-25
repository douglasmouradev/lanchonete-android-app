package com.lanchonete.app.ui.products

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lanchonete.app.R
import com.lanchonete.app.databinding.ActivityProductManagementBinding

class ProductManagementActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityProductManagementBinding
    private lateinit var viewModel: ProductManagementViewModel
    private lateinit var productAdapter: ProductAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupViewModel()
        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Gestão de Produtos"
    }
    
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[ProductManagementViewModel::class.java]
    }
    
    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(
            onEditClick = { product ->
                openEditProductDialog(product)
            },
            onDeleteClick = { product ->
                showDeleteConfirmation(product)
            }
        )
        
        binding.recyclerViewProducts.apply {
            layoutManager = LinearLayoutManager(this@ProductManagementActivity)
            adapter = productAdapter
        }
    }
    
    private fun setupClickListeners() {
        binding.fabAddProduct.setOnClickListener {
            openAddProductDialog()
        }
        
        binding.buttonSearch.setOnClickListener {
            val query = binding.editTextSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                viewModel.searchProducts(query)
            } else {
                viewModel.loadAllProducts()
            }
        }
    }
    
    private fun observeViewModel() {
        viewModel.products.observe(this) { products ->
            productAdapter.updateItems(products)
        }
        
        viewModel.categories.observe(this) { categories ->
            // Update category filter if needed
        }
        
        viewModel.productSaved.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Produto salvo com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Erro ao salvar produto", Toast.LENGTH_SHORT).show()
            }
        }
        
        viewModel.productDeleted.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Produto excluído com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Erro ao excluir produto", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun openAddProductDialog() {
        val dialog = ProductDialogFragment.newInstance { product ->
            viewModel.saveProduct(product)
        }
        dialog.show(supportFragmentManager, "add_product_dialog")
    }
    
    private fun openEditProductDialog(product: com.lanchonete.app.data.model.Product) {
        val dialog = ProductDialogFragment.newInstance(product) { updatedProduct ->
            viewModel.updateProduct(updatedProduct)
        }
        dialog.show(supportFragmentManager, "edit_product_dialog")
    }
    
    private fun showDeleteConfirmation(product: com.lanchonete.app.data.model.Product) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Confirmar Exclusão")
            .setMessage("Tem certeza que deseja excluir o produto \"${product.name}\"?")
            .setPositiveButton("Excluir") { _, _ ->
                viewModel.deleteProduct(product)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.product_management_menu, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_import_products -> {
                // TODO: Implement product import
                true
            }
            R.id.action_export_products -> {
                // TODO: Implement product export
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    override fun onResume() {
        super.onResume()
        viewModel.loadAllProducts()
    }
}
