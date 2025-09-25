package com.lanchonete.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date

@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val cost: Double = 0.0,
    val category: String = "",
    val barcode: String = "",
    val imageUrl: String = "",
    val isActive: Boolean = true,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
) : Serializable
