package com.lanchonete.app.ui.sales

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.lanchonete.app.R
import com.lanchonete.app.data.model.PaymentRequest
import com.lanchonete.app.databinding.ActivitySalesBinding
import com.lanchonete.app.ui.payment.PaymentActivity
import java.util.*

class SalesActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySalesBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivitySalesBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            setupToolbar()
            setupClickListeners()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Erro ao inicializar tela de vendas: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }
    
    private fun setupToolbar() {
        try {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.title = "Nova Venda"
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun setupClickListeners() {
        // Simular carrinho com alguns produtos
        binding.buttonProcessPayment.setOnClickListener {
            processPayment()
        }
        
        binding.buttonAddProduct.setOnClickListener {
            addSampleProduct()
        }
        
        binding.buttonClearCart.setOnClickListener {
            clearCart()
        }
    }
    
    private fun addSampleProduct() {
        // Adicionar produto de exemplo
        Toast.makeText(this, "Produto adicionado ao carrinho", Toast.LENGTH_SHORT).show()
        updateCartTotal()
    }
    
    private fun clearCart() {
        Toast.makeText(this, "Carrinho limpo", Toast.LENGTH_SHORT).show()
        updateCartTotal()
    }
    
    private fun updateCartTotal() {
        // Simular total do carrinho
        val total = 25.50
        binding.textCartTotal.text = "Total: R$ ${String.format("%.2f", total)}"
    }
    
    private fun processPayment() {
        val total = 25.50 // Valor simulado
        
        val paymentRequest = PaymentRequest(
            amount = total,
            description = "Venda Lanchonete - ${Date()}",
            customerName = "Cliente",
            orderId = "ORD_${System.currentTimeMillis()}"
        )
        
        val intent = Intent(this, PaymentActivity::class.java).apply {
            putExtra(PaymentActivity.EXTRA_PAYMENT_REQUEST, paymentRequest)
        }
        
        paymentLauncher.launch(intent)
    }
    
    private val paymentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            PaymentActivity.RESULT_PAYMENT_SUCCESS -> {
                Toast.makeText(this, "Pagamento aprovado! Venda finalizada.", Toast.LENGTH_LONG).show()
                clearCart()
            }
            PaymentActivity.RESULT_PAYMENT_FAILED -> {
                Toast.makeText(this, "Pagamento cancelado ou falhou", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        try {
            menuInflater.inflate(R.menu.sales_menu, menu)
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
            R.id.action_sale_history -> {
                Toast.makeText(this, "Histórico de vendas em desenvolvimento", Toast.LENGTH_SHORT).show()
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
