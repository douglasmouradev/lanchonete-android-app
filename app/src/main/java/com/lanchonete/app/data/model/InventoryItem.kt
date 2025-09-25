package com.lanchonete.app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "inventory_items",
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
data class InventoryItem(
    @PrimaryKey
    val id: String = "",
    val productId: String = "",
    val productName: String = "",
    val currentStock: Int = 0,
    val minStock: Int = 0,
    val maxStock: Int = 0,
    val lastRestockDate: Date = Date(),
    val lastRestockQuantity: Int = 0,
    val unit: String = "unidade", // unidade, kg, litro, etc.
    val location: String = "", // prateleira, geladeira, etc.
    val supplier: String = "",
    val notes: String = "",
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)
