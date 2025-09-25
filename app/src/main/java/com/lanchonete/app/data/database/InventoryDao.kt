package com.lanchonete.app.data.database

import androidx.room.*
import com.lanchonete.app.data.model.InventoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDao {
    
    @Query("SELECT * FROM inventory_items ORDER BY productId ASC")
    fun getAllInventoryItems(): Flow<List<InventoryItem>>
    
    @Query("SELECT * FROM inventory_items WHERE productId = :productId")
    suspend fun getInventoryItemByProductId(productId: String): InventoryItem?
    
    @Query("SELECT * FROM inventory_items WHERE currentStock <= minStock")
    fun getLowStockItems(): Flow<List<InventoryItem>>
    
    @Query("SELECT * FROM inventory_items WHERE currentStock = 0")
    fun getOutOfStockItems(): Flow<List<InventoryItem>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventoryItem(item: InventoryItem)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventoryItems(items: List<InventoryItem>)
    
    @Update
    suspend fun updateInventoryItem(item: InventoryItem)
    
    @Delete
    suspend fun deleteInventoryItem(item: InventoryItem)
    
    @Query("UPDATE inventory_items SET currentStock = :newStock WHERE productId = :productId")
    suspend fun updateStock(productId: String, newStock: Int)
}
