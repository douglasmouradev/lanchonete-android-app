package com.lanchonete.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "sales")
data class Sale(
    @PrimaryKey
    val id: String = "",
    val customerName: String = "",
    val customerPhone: String = "",
    val totalAmount: Double = 0.0,
    val discount: Double = 0.0,
    val finalAmount: Double = 0.0,
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val status: SaleStatus = SaleStatus.COMPLETED,
    val cashierId: String = "",
    val cashierName: String = "",
    val createdAt: Date = Date(),
    val notes: String = ""
)

enum class PaymentMethod {
    CASH, CREDIT_CARD, DEBIT_CARD, PIX, MEAL_VOUCHER
}

enum class SaleStatus {
    PENDING, COMPLETED, CANCELLED, REFUNDED
}
