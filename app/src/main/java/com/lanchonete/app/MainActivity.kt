package com.lanchonete.app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lanchonete.app.databinding.ActivityMainBinding
import com.lanchonete.app.ui.inventory.InventoryActivity
import com.lanchonete.app.ui.products.ProductManagementActivity
import com.lanchonete.app.ui.reports.ReportsActivity
import com.lanchonete.app.ui.sales.SalesActivity
import com.lanchonete.app.viewmodel.MainViewModel
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupViewModel()
        setupClickListeners()
        observeViewModel()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Lanchonete App"
    }
    
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }
    
    private fun setupClickListeners() {
        binding.cardSales.setOnClickListener {
            startActivity(Intent(this, SalesActivity::class.java))
        }
        
        binding.cardInventory.setOnClickListener {
            startActivity(Intent(this, InventoryActivity::class.java))
        }
        
        binding.cardProducts.setOnClickListener {
            startActivity(Intent(this, ProductManagementActivity::class.java))
        }
        
        binding.cardReports.setOnClickListener {
            startActivity(Intent(this, ReportsActivity::class.java))
        }
    }
    
    private fun observeViewModel() {
        viewModel.todaySales.observe(this) { sales ->
            binding.textTodaySales.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                .format(sales)
        }
        
        viewModel.todayOrders.observe(this) { orders ->
            binding.textTodayOrders.text = orders.toString()
        }
        
        viewModel.lowStockCount.observe(this) { count ->
            binding.textLowStock.text = count.toString()
            if (count > 0) {
                binding.cardInventory.setCardBackgroundColor(
                    getColor(android.R.color.holo_orange_light)
                )
            }
        }
        
        viewModel.outOfStockCount.observe(this) { count ->
            if (count > 0) {
                binding.cardInventory.setCardBackgroundColor(
                    getColor(android.R.color.holo_red_light)
                )
            }
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                // TODO: Implementar configurações
                true
            }
            R.id.action_help -> {
                // TODO: Implementar ajuda
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    override fun onResume() {
        super.onResume()
        viewModel.loadDashboardData()
    }
}
