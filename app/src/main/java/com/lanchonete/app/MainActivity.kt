package com.lanchonete.app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lanchonete.app.databinding.ActivityMainBinding
import com.lanchonete.app.ui.inventory.InventoryActivity
import com.lanchonete.app.ui.products.ProductManagementActivity
import com.lanchonete.app.ui.reports.ReportsActivity
import com.lanchonete.app.ui.sales.SalesActivity

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            setupToolbar()
            setupClickListeners()
            setupDefaultValues()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Erro ao inicializar o aplicativo: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }
    
    private fun setupToolbar() {
        try {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.title = "Lanchonete App"
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun setupDefaultValues() {
        try {
            binding.textTodaySales.text = "R$ 0,00"
            binding.textTodayOrders.text = "0"
            binding.textLowStock.text = "0"
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun setupClickListeners() {
        binding.cardSales.setOnClickListener {
            try {
                startActivity(Intent(this, SalesActivity::class.java))
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Erro ao abrir Vendas", Toast.LENGTH_SHORT).show()
            }
        }
        
        binding.cardInventory.setOnClickListener {
            try {
                startActivity(Intent(this, InventoryActivity::class.java))
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Erro ao abrir Estoque", Toast.LENGTH_SHORT).show()
            }
        }
        
        binding.cardProducts.setOnClickListener {
            try {
                startActivity(Intent(this, ProductManagementActivity::class.java))
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Erro ao abrir Produtos", Toast.LENGTH_SHORT).show()
            }
        }
        
        binding.cardReports.setOnClickListener {
            try {
                startActivity(Intent(this, ReportsActivity::class.java))
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Erro ao abrir Relatórios", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        try {
            menuInflater.inflate(R.menu.main_menu, menu)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(this, "Configurações em desenvolvimento", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_help -> {
                Toast.makeText(this, "Ajuda em desenvolvimento", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
