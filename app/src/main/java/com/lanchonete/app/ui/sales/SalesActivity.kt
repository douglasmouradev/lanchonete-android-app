package com.lanchonete.app.ui.sales

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lanchonete.app.R
import com.lanchonete.app.data.model.*
import com.lanchonete.app.databinding.ActivitySalesBinding
import com.lanchonete.app.ui.products.ProductSelectionActivity
import java.text.NumberFormat
import java.util.*

class SalesActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySalesBinding
    private lateinit var viewModel: SalesViewModel
    private lateinit var cartAdapter: CartAdapter
    
    private val cartItems = mutableListOf<CartItem>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalesBinding.inflate(layoutInflater)
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
        supportActionBar?.title = "Nova Venda"
    }
    
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[SalesViewModel::class.java]
    }
    
    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            onQuantityChanged = { position, newQuantity ->
                updateCartItemQuantity(position, newQuantity)
            },
            onItemRemoved = { position ->
                removeCartItem(position)
            }
        )
        
        binding.recyclerViewCart.apply {
            layoutManager = LinearLayoutManager(this@SalesActivity)
            adapter = cartAdapter
        }
    }
    
    private fun setupClickListeners() {
        binding.fabAddProduct.setOnClickListener {
            productSelectionLauncher.launch(
                Intent(this, ProductSelectionActivity::class.java)
            )
        }
        
        binding.buttonCompleteSale.setOnClickListener {
            completeSale()
        }
        
        binding.buttonClearCart.setOnClickListener {
            clearCart()
        }
    }
    
    private fun observeViewModel() {
        viewModel.products.observe(this) { products ->
            // Products loaded for selection
        }
        
        viewModel.saleCompleted.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Venda finalizada com sucesso!", Toast.LENGTH_SHORT).show()
                clearCart()
            } else {
                Toast.makeText(this, "Erro ao finalizar venda", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun updateCartItemQuantity(position: Int, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeCartItem(position)
        } else {
            cartItems[position].quantity = newQuantity
            cartItems[position].totalPrice = cartItems[position].unitPrice * newQuantity
            cartAdapter.notifyItemChanged(position)
            updateTotal()
        }
    }
    
    private fun removeCartItem(position: Int) {
        cartItems.removeAt(position)
        cartAdapter.notifyItemRemoved(position)
        updateTotal()
    }
    
    private fun addProductToCart(product: Product) {
        val existingItem = cartItems.find { it.productId == product.id }
        
        if (existingItem != null) {
            val index = cartItems.indexOf(existingItem)
            updateCartItemQuantity(index, existingItem.quantity + 1)
        } else {
            val cartItem = CartItem(
                productId = product.id,
                productName = product.name,
                unitPrice = product.price,
                quantity = 1,
                totalPrice = product.price
            )
            cartItems.add(cartItem)
            cartAdapter.notifyItemInserted(cartItems.size - 1)
            updateTotal()
        }
    }
    
    private fun updateTotal() {
        val subtotal = cartItems.sumOf { it.totalPrice }
        val discount = binding.editTextDiscount.text.toString().toDoubleOrNull() ?: 0.0
        val total = subtotal - discount
        
        binding.textSubtotal.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(subtotal)
        binding.textTotal.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(total)
        
        binding.buttonCompleteSale.isEnabled = cartItems.isNotEmpty() && total > 0
    }
    
    private fun clearCart() {
        cartItems.clear()
        cartAdapter.notifyDataSetChanged()
        binding.editTextCustomerName.setText("")
        binding.editTextCustomerPhone.setText("")
        binding.editTextDiscount.setText("")
        binding.radioGroupPaymentMethod.clearCheck()
        updateTotal()
    }
    
    private fun completeSale() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Adicione pelo menos um produto", Toast.LENGTH_SHORT).show()
            return
        }
        
        val customerName = binding.editTextCustomerName.text.toString().trim()
        val customerPhone = binding.editTextCustomerPhone.text.toString().trim()
        val discount = binding.editTextDiscount.text.toString().toDoubleOrNull() ?: 0.0
        
        val selectedPaymentMethod = when (binding.radioGroupPaymentMethod.checkedRadioButtonId) {
            R.id.radioCash -> PaymentMethod.CASH
            R.id.radioCreditCard -> PaymentMethod.CREDIT_CARD
            R.id.radioDebitCard -> PaymentMethod.DEBIT_CARD
            R.id.radioPix -> PaymentMethod.PIX
            R.id.radioMealVoucher -> PaymentMethod.MEAL_VOUCHER
            else -> PaymentMethod.CASH
        }
        
        val subtotal = cartItems.sumOf { it.totalPrice }
        val finalAmount = subtotal - discount
        
        val sale = Sale(
            id = UUID.randomUUID().toString(),
            customerName = customerName,
            customerPhone = customerPhone,
            totalAmount = subtotal,
            discount = discount,
            finalAmount = finalAmount,
            paymentMethod = selectedPaymentMethod,
            status = SaleStatus.COMPLETED,
            cashierId = "current_user", // TODO: Get from user session
            cashierName = "Funcionário", // TODO: Get from user session
            createdAt = Date(),
            notes = binding.editTextNotes.text.toString().trim()
        )
        
        val saleItems = cartItems.map { cartItem ->
            SaleItem(
                id = UUID.randomUUID().toString(),
                saleId = sale.id,
                productId = cartItem.productId,
                productName = cartItem.productName,
                quantity = cartItem.quantity,
                unitPrice = cartItem.unitPrice,
                totalPrice = cartItem.totalPrice,
                discount = 0.0,
                createdAt = Date()
            )
        }
        
        viewModel.processSale(sale, saleItems)
    }
    
    private val productSelectionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val product = result.data?.getSerializableExtra("product", Product::class.java)
            product?.let { addProductToCart(it) }
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sales_menu, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_sale_history -> {
                // TODO: Navigate to sale history
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    companion object {
        private const val REQUEST_SELECT_PRODUCT = 1001
    }
}

data class CartItem(
    val productId: String,
    val productName: String,
    val unitPrice: Double,
    var quantity: Int,
    var totalPrice: Double
)
