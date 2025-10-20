package com.lanchonete.app.ui.payment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lanchonete.app.R
import com.lanchonete.app.data.model.*
import kotlinx.coroutines.delay
import com.lanchonete.app.data.payment.PaymentService
import com.lanchonete.app.databinding.ActivityPaymentBinding
import kotlinx.coroutines.launch

class PaymentActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var paymentService: PaymentService
    
    private var paymentRequest: PaymentRequest? = null
    private var selectedPaymentMethodType: PaymentMethodType = PaymentMethodType.CASH
    
    companion object {
        const val EXTRA_PAYMENT_REQUEST = "payment_request"
        const val RESULT_PAYMENT_SUCCESS = 1
        const val RESULT_PAYMENT_FAILED = 0
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityPaymentBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            paymentService = PaymentService()
            paymentRequest = intent.getSerializableExtra(EXTRA_PAYMENT_REQUEST) as? PaymentRequest
            
            setupToolbar()
            setupPaymentMethodTypes()
            setupClickListeners()
            displayPaymentInfo()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Erro ao inicializar pagamento: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }
    
    private fun setupToolbar() {
        try {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.title = "Pagamento"
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun setupPaymentMethodTypes() {
        // Configurar radio buttons para métodos de pagamento
        binding.radioCash.setOnClickListener { selectPaymentMethodType(PaymentMethodType.CASH) }
        binding.radioCreditCard.setOnClickListener { selectPaymentMethodType(PaymentMethodType.CREDIT_CARD) }
        binding.radioDebitCard.setOnClickListener { selectPaymentMethodType(PaymentMethodType.DEBIT_CARD) }
        binding.radioPix.setOnClickListener { selectPaymentMethodType(PaymentMethodType.PIX) }
        binding.radioMealVoucher.setOnClickListener { selectPaymentMethodType(PaymentMethodType.MEAL_VOUCHER) }
        
        // Selecionar dinheiro por padrão
        selectPaymentMethodType(PaymentMethodType.CASH)
    }
    
    private fun setupClickListeners() {
        binding.buttonProcessPayment.setOnClickListener {
            processPayment()
        }
        
        binding.buttonCancelPayment.setOnClickListener {
            setResult(RESULT_PAYMENT_FAILED)
            finish()
        }
    }
    
    private fun displayPaymentInfo() {
        paymentRequest?.let { request ->
            binding.textAmount.text = "R$ ${String.format("%.2f", request.amount)}"
            binding.textDescription.text = request.description
            binding.textOrderId.text = "Pedido: ${request.orderId}"
        }
    }
    
    private fun selectPaymentMethodType(method: PaymentMethodType) {
        selectedPaymentMethodType = method
        
        // Mostrar/ocultar campos específicos baseado no método selecionado
        when (method) {
            PaymentMethodType.CASH -> {
                binding.layoutCash.visibility = View.VISIBLE
                binding.layoutCard.visibility = View.GONE
                binding.layoutPix.visibility = View.GONE
                binding.layoutMealVoucher.visibility = View.GONE
            }
            PaymentMethodType.CREDIT_CARD, PaymentMethodType.DEBIT_CARD -> {
                binding.layoutCash.visibility = View.GONE
                binding.layoutCard.visibility = View.VISIBLE
                binding.layoutPix.visibility = View.GONE
                binding.layoutMealVoucher.visibility = View.GONE
            }
            PaymentMethodType.PIX -> {
                binding.layoutCash.visibility = View.GONE
                binding.layoutCard.visibility = View.GONE
                binding.layoutPix.visibility = View.VISIBLE
                binding.layoutMealVoucher.visibility = View.GONE
            }
            PaymentMethodType.MEAL_VOUCHER -> {
                binding.layoutCash.visibility = View.GONE
                binding.layoutCard.visibility = View.GONE
                binding.layoutPix.visibility = View.GONE
                binding.layoutMealVoucher.visibility = View.VISIBLE
            }
        }
    }
    
    private fun processPayment() {
        val request = paymentRequest ?: return
        
        binding.buttonProcessPayment.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val result = when (selectedPaymentMethodType) {
                    PaymentMethodType.CASH -> {
                        val receivedAmount = binding.editTextReceivedAmount.text.toString().toDoubleOrNull() ?: 0.0
                        paymentService.processCashPayment(request.amount, receivedAmount)
                    }
                    PaymentMethodType.CREDIT_CARD -> {
                        val cardNumber = binding.editTextCardNumber.text.toString()
                        val expiryDate = binding.editTextExpiryDate.text.toString()
                        val cvv = binding.editTextCvv.text.toString()
                        val cardholderName = binding.editTextCardholderName.text.toString()
                        paymentService.processCreditCardPayment(request, cardNumber, expiryDate, cvv, cardholderName)
                    }
                    PaymentMethodType.DEBIT_CARD -> {
                        val cardNumber = binding.editTextCardNumber.text.toString()
                        val pin = binding.editTextPin.text.toString()
                        paymentService.processDebitCardPayment(request, cardNumber, pin)
                    }
                    PaymentMethodType.PIX -> {
                        val response = paymentService.processPixPayment(request)
                        showPixPayment(response)
                        return@launch
                    }
                    PaymentMethodType.MEAL_VOUCHER -> {
                        val voucherNumber = binding.editTextVoucherNumber.text.toString()
                        val pin = binding.editTextVoucherPin.text.toString()
                        paymentService.processMealVoucherPayment(request, voucherNumber, pin)
                    }
                }
                
                handlePaymentResult(result)
                
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@PaymentActivity, "Erro no processamento: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                binding.buttonProcessPayment.isEnabled = true
                binding.progressBar.visibility = View.GONE
            }
        }
    }
    
    private fun showPixPayment(response: PaymentResponse) {
        // Mostrar QR Code e código PIX
        binding.layoutPixResult.visibility = View.VISIBLE
        binding.textPixCode.text = response.pixCode
        binding.textPixInstructions.text = "Escaneie o QR Code com seu app de pagamento ou copie o código PIX"
        
        // Simular verificação de pagamento
        lifecycleScope.launch {
            delay(5000) // Aguarda 5 segundos
            val status = paymentService.checkPixPaymentStatus(response.transactionId ?: "")
            
            if (status == PaymentStatus.APPROVED) {
                val result = PaymentResult(
                    success = true,
                    transactionId = response.transactionId,
                    message = "Pagamento PIX aprovado",
                    paymentMethod = PaymentMethodType.PIX,
                    amount = response.amount
                )
                handlePaymentResult(result)
            } else {
                Toast.makeText(this@PaymentActivity, "Pagamento PIX ainda não foi confirmado", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun handlePaymentResult(result: PaymentResult) {
        if (result.success) {
            Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
            
            val resultIntent = Intent().apply {
                putExtra("payment_result", result)
            }
            setResult(RESULT_PAYMENT_SUCCESS, resultIntent)
            finish()
        } else {
            Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
            binding.buttonProcessPayment.isEnabled = true
            binding.progressBar.visibility = View.GONE
        }
    }
    
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
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
