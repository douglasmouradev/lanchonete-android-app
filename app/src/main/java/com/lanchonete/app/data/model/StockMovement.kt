package com.lanchonete.app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "stock_movements",
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["productId"])]
)
data class StockMovement(
    @PrimaryKey
    val id: String = "",
    val productId: String = "",
    val productName: String = "",
    val movementType: MovementType = MovementType.IN,
    val quantity: Int = 0,
    val previousStock: Int = 0,
    val newStock: Int = 0,
    val reason: String = "",
    val responsible: String = "",
    val createdAt: Date = Date(),
    val notes: String = ""
)

enum class MovementType {
    IN, OUT, ADJUSTMENT, LOSS, RETURN
}
