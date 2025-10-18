package com.lanchonete.app.ui.products

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lanchonete.app.R
import com.lanchonete.app.databinding.ActivityProductManagementBinding

class ProductManagementActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityProductManagementBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityProductManagementBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            setupToolbar()
            setupClickListeners()
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
    
    private fun setupClickListeners() {
        // Simular funcionalidades básicas
        Toast.makeText(this, "Tela de Produtos - Em desenvolvimento", Toast.LENGTH_SHORT).show()
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
                // Botão de voltar - retorna para a tela anterior
                onBackPressed()
                true
            }
            else -> {
                Toast.makeText(this, "Funcionalidade em desenvolvimento", Toast.LENGTH_SHORT).show()
                true
            }
        }
    }
    
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        // Adiciona animação de transição
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}