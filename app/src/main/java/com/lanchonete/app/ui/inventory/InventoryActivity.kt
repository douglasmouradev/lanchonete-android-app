package com.lanchonete.app.ui.inventory

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lanchonete.app.R
import com.lanchonete.app.databinding.ActivityInventoryBinding

class InventoryActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityInventoryBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityInventoryBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            setupToolbar()
            setupClickListeners()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Erro ao inicializar tela de estoque: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }
    
    private fun setupToolbar() {
        try {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.title = "Controle de Estoque"
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun setupClickListeners() {
        // Simular funcionalidades básicas
        Toast.makeText(this, "Tela de Estoque - Em desenvolvimento", Toast.LENGTH_SHORT).show()
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        try {
            menuInflater.inflate(R.menu.inventory_menu, menu)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Botão de voltar - retorna para a tela anterior
                onBackPressed()
                true
            }
            R.id.action_stock_movements -> {
                Toast.makeText(this, "Movimentações de estoque em desenvolvimento", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_export_inventory -> {
                Toast.makeText(this, "Exportar estoque em desenvolvimento", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        // Adiciona animação de transição
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}
