package com.lanchonete.app.ui.inventory

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lanchonete.app.R
import com.lanchonete.app.databinding.ActivityInventoryBinding
import com.lanchonete.app.ui.products.ProductManagementActivity

class InventoryActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityInventoryBinding
    private lateinit var viewModel: InventoryViewModel
    private lateinit var inventoryAdapter: InventoryAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventoryBinding.inflate(layoutInflater)
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
        supportActionBar?.title = "Controle de Estoque"
    }
    
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[InventoryViewModel::class.java]
    }
    
    private fun setupRecyclerView() {
        inventoryAdapter = InventoryAdapter(
            onRestockClick = { inventoryItem ->
                showRestockDialog(inventoryItem)
            },
            onAdjustStockClick = { inventoryItem ->
                showAdjustStockDialog(inventoryItem)
            }
        )
        
        binding.recyclerViewInventory.apply {
            layoutManager = LinearLayoutManager(this@InventoryActivity)
            adapter = inventoryAdapter
        }
    }
    
    private fun setupClickListeners() {
        binding.fabAddProduct.setOnClickListener {
            startActivity(Intent(this, ProductManagementActivity::class.java))
        }
        
        binding.buttonLowStock.setOnClickListener {
            viewModel.filterLowStock()
        }
        
        binding.buttonOutOfStock.setOnClickListener {
            viewModel.filterOutOfStock()
        }
        
        binding.buttonAllItems.setOnClickListener {
            viewModel.showAllItems()
        }
    }
    
    private fun observeViewModel() {
        viewModel.inventoryItems.observe(this) { items ->
            inventoryAdapter.updateItems(items)
        }
        
        viewModel.lowStockItems.observe(this) { items ->
            binding.textLowStockCount.text = items.size.toString()
        }
        
        viewModel.outOfStockItems.observe(this) { items ->
            binding.textOutOfStockCount.text = items.size.toString()
        }
        
        viewModel.stockUpdated.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Estoque atualizado com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Erro ao atualizar estoque", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun showRestockDialog(inventoryItem: com.lanchonete.app.data.model.InventoryItem) {
        val dialog = RestockDialogFragment.newInstance(inventoryItem) { quantity ->
            viewModel.restockProduct(inventoryItem.productId, quantity, "Usuário Atual")
        }
        dialog.show(supportFragmentManager, "restock_dialog")
    }
    
    private fun showAdjustStockDialog(inventoryItem: com.lanchonete.app.data.model.InventoryItem) {
        val dialog = AdjustStockDialogFragment.newInstance(inventoryItem) { newStock ->
            viewModel.adjustStock(inventoryItem.productId, newStock, "Usuário Atual")
        }
        dialog.show(supportFragmentManager, "adjust_stock_dialog")
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.inventory_menu, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_stock_movements -> {
                // TODO: Navigate to stock movements
                true
            }
            R.id.action_export_inventory -> {
                // TODO: Export inventory
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    override fun onResume() {
        super.onResume()
        viewModel.loadInventoryData()
    }
}
