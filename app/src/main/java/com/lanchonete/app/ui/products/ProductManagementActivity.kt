package com.lanchonete.app.ui.products

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lanchonete.app.R
import com.lanchonete.app.data.model.Product
import com.lanchonete.app.databinding.ActivityProductManagementBinding

class ProductManagementActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityProductManagementBinding
    private lateinit var viewModel: ProductManagementViewModel
    private lateinit var productAdapter: ProductAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityProductManagementBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            setupToolbar()
            setupViewModel()
            setupRecyclerView()
            setupClickListeners()
            observeViewModel()
            
            // Load initial data
            viewModel.loadAllProducts()
            viewModel.loadCategories()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Erro ao inicializar tela de produtos: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }
    
    private fun setupToolbar() {
        try {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.title = "Gestão de Produtos"
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[ProductManagementViewModel::class.java]
    }
    
    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(
            onProductClick = { product -> showEditProductDialog(product) },
            onProductDelete = { product -> showDeleteProductDialog(product) }
        )
        
        binding.recyclerViewProducts.apply {
            layoutManager = LinearLayoutManager(this@ProductManagementActivity)
            adapter = productAdapter
        }
    }
    
    private fun setupClickListeners() {
        // FAB - Add Product
        binding.fabAddProduct.setOnClickListener {
            showAddProductDialog()
        }
        
        // Search functionality
        binding.buttonSearch.setOnClickListener {
            val query = binding.editTextSearch.text.toString()
            if (TextUtils.isEmpty(query)) {
                viewModel.loadAllProducts()
            } else {
                viewModel.searchProducts(query)
            }
        }
        
        // Search on text change
        binding.editTextSearch.setOnEditorActionListener { _, _, _ ->
            val query = binding.editTextSearch.text.toString()
            if (TextUtils.isEmpty(query)) {
                viewModel.loadAllProducts()
            } else {
                viewModel.searchProducts(query)
            }
            true
        }
    }
    
    private fun observeViewModel() {
        viewModel.products.observe(this) { products ->
            productAdapter.submitList(products)
            updateProductCount(products.size)
            updateEmptyState(products.isEmpty())
        }
        
        viewModel.productSaved.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Produto salvo com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                showErrorDialog("Erro ao salvar produto", "Não foi possível salvar o produto. Verifique os dados e tente novamente.")
            }
        }
        
        viewModel.productDeleted.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Produto excluído com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                showErrorDialog("Erro ao excluir produto", "Não foi possível excluir o produto. Tente novamente.")
            }
        }
    }
    
    private fun showAddProductDialog() {
        val dialog = ProductDialogFragment.newInstance { product, initialStock ->
            viewModel.saveProduct(product, initialStock)
        }
        dialog.show(supportFragmentManager, "AddProductDialog")
    }
    
    private fun showEditProductDialog(product: Product) {
        val dialog = ProductDialogFragment.newInstance(product) { updatedProduct, _ ->
            viewModel.updateProduct(updatedProduct)
        }
        dialog.show(supportFragmentManager, "EditProductDialog")
    }
    
    private fun showDeleteProductDialog(product: Product) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Excluir Produto")
            .setMessage("Tem certeza que deseja excluir o produto '${product.name}'?")
            .setPositiveButton("Excluir") { _, _ ->
                viewModel.deleteProduct(product)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    
    private fun updateProductCount(count: Int) {
        binding.textProductCount.text = "$count ${if (count == 1) "produto" else "produtos"}"
    }
    
    private fun updateEmptyState(isEmpty: Boolean) {
        binding.layoutEmptyState.visibility = if (isEmpty) android.view.View.VISIBLE else android.view.View.GONE
    }
    
    private fun showErrorDialog(title: String, message: String) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        try {
            menuInflater.inflate(R.menu.product_management_menu, menu)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_refresh -> {
                viewModel.loadAllProducts()
                Toast.makeText(this, "Lista atualizada", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_export -> {
                Toast.makeText(this, "Funcionalidade de exportação em desenvolvimento", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}