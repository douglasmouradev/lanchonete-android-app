package com.lanchonete.app.data.payment

import com.lanchonete.app.data.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.delay
import java.util.*

class PaymentService {
    
    companion object {
        private const val PIX_BANK_CODE = "001" // Banco do Brasil
        private const val MERCHANT_ID = "lanchonete_123"
    }
    
    /**
     * Processa pagamento em dinheiro
     */
    suspend fun processCashPayment(amount: Double, receivedAmount: Double): PaymentResult {
        delay(1000) // Simula processamento
        
        return if (receivedAmount >= amount) {
            val change = receivedAmount - amount
            PaymentResult(
                success = true,
                transactionId = generateTransactionId(),
                message = if (change > 0) "Pagamento aprovado. Troco: R$ ${String.format("%.2f", change)}" else "Pagamento aprovado",
                paymentMethod = PaymentMethodType.CASH,
                amount = amount
            )
        } else {
            PaymentResult(
                success = false,
                transactionId = null,
                message = "Valor insuficiente. Faltam R$ ${String.format("%.2f", amount - receivedAmount)}",
                paymentMethod = PaymentMethodType.CASH,
                amount = amount
            )
        }
    }
    
    /**
     * Processa pagamento via PIX
     */
    suspend fun processPixPayment(request: PaymentRequest): PaymentResponse {
        delay(2000) // Simula processamento
        
        val pixCode = generatePixCode(request.amount)
        val qrCode = generateQrCode(pixCode)
        
        return PaymentResponse(
            success = true,
            transactionId = generateTransactionId(),
            paymentMethod = PaymentMethodType.PIX,
            amount = request.amount,
            status = PaymentStatus.PENDING,
            message = "PIX gerado com sucesso. Escaneie o QR Code ou copie o código PIX",
            pixCode = pixCode,
            qrCode = qrCode
        )
    }
    
    /**
     * Processa pagamento com cartão de crédito
     */
    suspend fun processCreditCardPayment(
        request: PaymentRequest,
        cardNumber: String,
        expiryDate: String,
        cvv: String,
        cardholderName: String
    ): PaymentResult {
        delay(3000) // Simula processamento
        
        // Validações básicas
        if (!isValidCardNumber(cardNumber)) {
            return PaymentResult(
                success = false,
                transactionId = null,
                message = "Número do cartão inválido",
                paymentMethod = PaymentMethodType.CREDIT_CARD,
                amount = request.amount
            )
        }
        
        if (!isValidExpiryDate(expiryDate)) {
            return PaymentResult(
                success = false,
                transactionId = null,
                message = "Data de validade inválida",
                paymentMethod = PaymentMethodType.CREDIT_CARD,
                amount = request.amount
            )
        }
        
        // Simula aprovação/rejeição baseado no valor
        val isApproved = request.amount <= 1000.0 // Aprova valores até R$ 1000
        
        return PaymentResult(
            success = isApproved,
            transactionId = if (isApproved) generateTransactionId() else null,
            message = if (isApproved) "Pagamento aprovado" else "Pagamento rejeitado",
            paymentMethod = PaymentMethodType.CREDIT_CARD,
            amount = request.amount
        )
    }
    
    /**
     * Processa pagamento com cartão de débito
     */
    suspend fun processDebitCardPayment(
        request: PaymentRequest,
        cardNumber: String,
        pin: String
    ): PaymentResult {
        delay(2500) // Simula processamento
        
        // Validações básicas
        if (!isValidCardNumber(cardNumber)) {
            return PaymentResult(
                success = false,
                transactionId = null,
                message = "Número do cartão inválido",
                paymentMethod = PaymentMethodType.DEBIT_CARD,
                amount = request.amount
            )
        }
        
        if (pin.length != 4) {
            return PaymentResult(
                success = false,
                transactionId = null,
                message = "PIN inválido",
                paymentMethod = PaymentMethodType.DEBIT_CARD,
                amount = request.amount
            )
        }
        
        // Simula aprovação baseado no valor
        val isApproved = request.amount <= 500.0 // Aprova valores até R$ 500
        
        return PaymentResult(
            success = isApproved,
            transactionId = if (isApproved) generateTransactionId() else null,
            message = if (isApproved) "Pagamento aprovado" else "Saldo insuficiente",
            paymentMethod = PaymentMethodType.DEBIT_CARD,
            amount = request.amount
        )
    }
    
    /**
     * Processa pagamento com vale refeição
     */
    suspend fun processMealVoucherPayment(
        request: PaymentRequest,
        voucherNumber: String,
        pin: String
    ): PaymentResult {
        delay(1500) // Simula processamento
        
        // Validações básicas
        if (voucherNumber.length != 16) {
            return PaymentResult(
                success = false,
                transactionId = null,
                message = "Número do vale inválido",
                paymentMethod = PaymentMethodType.MEAL_VOUCHER,
                amount = request.amount
            )
        }
        
        if (pin.length != 4) {
            return PaymentResult(
                success = false,
                transactionId = null,
                message = "PIN inválido",
                paymentMethod = PaymentMethodType.MEAL_VOUCHER,
                amount = request.amount
            )
        }
        
        // Simula aprovação baseado no valor
        val isApproved = request.amount <= 50.0 // Aprova valores até R$ 50
        
        return PaymentResult(
            success = isApproved,
            transactionId = if (isApproved) generateTransactionId() else null,
            message = if (isApproved) "Pagamento aprovado" else "Saldo insuficiente no vale",
            paymentMethod = PaymentMethodType.MEAL_VOUCHER,
            amount = request.amount
        )
    }
    
    /**
     * Verifica status de um pagamento PIX
     */
    suspend fun checkPixPaymentStatus(transactionId: String): PaymentStatus {
        delay(1000) // Simula consulta
        
        // Simula diferentes status baseado no ID
        return when (transactionId.takeLast(1).toIntOrNull() ?: 0) {
            0, 1, 2 -> PaymentStatus.APPROVED
            3, 4, 5 -> PaymentStatus.PENDING
            else -> PaymentStatus.REJECTED
        }
    }
    
    // Métodos auxiliares
    private fun generateTransactionId(): String {
        return "TXN_${System.currentTimeMillis()}_${Random().nextInt(1000)}"
    }
    
    private fun generatePixCode(amount: Double): String {
        return "00020126580014BR.GOV.BCB.PIX0136${MERCHANT_ID}520400005303986540${String.format("%.2f", amount)}5802BR5913Lanchonete App6009Sao Paulo62070503***6304"
    }
    
    private fun generateQrCode(pixCode: String): String {
        return "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg=="
    }
    
    private fun isValidCardNumber(cardNumber: String): Boolean {
        val cleanNumber = cardNumber.replace("\\s".toRegex(), "")
        return cleanNumber.length >= 13 && cleanNumber.length <= 19 && cleanNumber.all { it.isDigit() }
    }
    
    private fun isValidExpiryDate(expiryDate: String): Boolean {
        val regex = "^(0[1-9]|1[0-2])/([0-9]{2})$".toRegex()
        if (!regex.matches(expiryDate)) return false
        
        val parts = expiryDate.split("/")
        val month = parts[0].toInt()
        val year = 2000 + parts[1].toInt()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
        
        return year > currentYear || (year == currentYear && month >= currentMonth)
    }
}
