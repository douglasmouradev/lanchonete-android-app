package com.lanchonete.app.data.database

import androidx.room.*
import com.lanchonete.app.data.model.StockMovement
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface StockMovementDao {
    
    @Query("SELECT * FROM stock_movements ORDER BY createdAt DESC")
    fun getAllStockMovements(): Flow<List<StockMovement>>
    
    @Query("SELECT * FROM stock_movements WHERE productId = :productId ORDER BY createdAt DESC")
    fun getStockMovementsByProductId(productId: String): Flow<List<StockMovement>>
    
    @Query("SELECT * FROM stock_movements WHERE movementType = :movementType ORDER BY createdAt DESC")
    fun getStockMovementsByType(movementType: String): Flow<List<StockMovement>>
    
    @Query("SELECT * FROM stock_movements WHERE createdAt BETWEEN :startDate AND :endDate ORDER BY createdAt DESC")
    fun getStockMovementsByDateRange(startDate: Date, endDate: Date): Flow<List<StockMovement>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStockMovement(movement: StockMovement)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStockMovements(movements: List<StockMovement>)
    
    @Update
    suspend fun updateStockMovement(movement: StockMovement)
    
    @Delete
    suspend fun deleteStockMovement(movement: StockMovement)
}
