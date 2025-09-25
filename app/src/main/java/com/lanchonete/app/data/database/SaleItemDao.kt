package com.lanchonete.app.data.database

import androidx.room.*
import com.lanchonete.app.data.model.SaleItem
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleItemDao {
    
    @Query("SELECT * FROM sale_items WHERE saleId = :saleId")
    fun getSaleItemsBySaleId(saleId: String): Flow<List<SaleItem>>
    
    @Query("SELECT * FROM sale_items WHERE productId = :productId")
    fun getSaleItemsByProductId(productId: String): Flow<List<SaleItem>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaleItem(saleItem: SaleItem)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaleItems(saleItems: List<SaleItem>)
    
    @Update
    suspend fun updateSaleItem(saleItem: SaleItem)
    
    @Delete
    suspend fun deleteSaleItem(saleItem: SaleItem)
    
    @Query("DELETE FROM sale_items WHERE saleId = :saleId")
    suspend fun deleteSaleItemsBySaleId(saleId: String)
}
