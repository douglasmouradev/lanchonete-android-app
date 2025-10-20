package com.lanchonete.app.data.model

import java.io.Serializable

enum class PaymentMethodType : Serializable {
    CASH,
    CREDIT_CARD,
    DEBIT_CARD,
    PIX,
    MEAL_VOUCHER
}

data class PaymentRequest(
    val amount: Double,
    val description: String,
    val customerName: String? = null,
    val customerEmail: String? = null,
    val customerPhone: String? = null,
    val orderId: String
) : Serializable

data class PaymentResponse(
    val success: Boolean,
    val transactionId: String? = null,
    val paymentMethod: PaymentMethodType,
    val amount: Double,
    val status: PaymentStatus,
    val message: String? = null,
    val pixCode: String? = null, // Para pagamentos PIX
    val qrCode: String? = null   // QR Code para PIX
) : Serializable

enum class PaymentStatus {
    PENDING,
    APPROVED,
    REJECTED,
    CANCELLED,
    REFUNDED
}

data class PaymentResult(
    val success: Boolean,
    val transactionId: String?,
    val message: String,
    val paymentMethod: PaymentMethodType,
    val amount: Double,
    val timestamp: Long = System.currentTimeMillis()
) : Serializable
