package com.lanchonete.app.data.database

import androidx.room.*
import com.lanchonete.app.data.model.Sale
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface SaleDao {
    
    @Query("SELECT * FROM sales ORDER BY createdAt DESC")
    fun getAllSales(): Flow<List<Sale>>
    
    @Query("SELECT * FROM sales WHERE id = :id")
    suspend fun getSaleById(id: String): Sale?
    
    @Query("SELECT * FROM sales WHERE DATE(createdAt/1000, 'unixepoch') = DATE(:date/1000, 'unixepoch')")
    fun getSalesByDate(date: Date): Flow<List<Sale>>
    
    @Query("SELECT * FROM sales WHERE createdAt BETWEEN :startDate AND :endDate")
    fun getSalesByDateRange(startDate: Date, endDate: Date): Flow<List<Sale>>
    
    @Query("SELECT SUM(finalAmount) FROM sales WHERE DATE(createdAt/1000, 'unixepoch') = DATE(:date/1000, 'unixepoch')")
    suspend fun getTotalSalesByDate(date: Date): Double?
    
    @Query("SELECT SUM(finalAmount) FROM sales WHERE createdAt BETWEEN :startDate AND :endDate")
    suspend fun getTotalSalesByDateRange(startDate: Date, endDate: Date): Double?
    
    @Query("SELECT COUNT(*) FROM sales WHERE DATE(createdAt/1000, 'unixepoch') = DATE(:date/1000, 'unixepoch')")
    suspend fun getSalesCountByDate(date: Date): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: Sale)
    
    @Update
    suspend fun updateSale(sale: Sale)
    
    @Delete
    suspend fun deleteSale(sale: Sale)
}
