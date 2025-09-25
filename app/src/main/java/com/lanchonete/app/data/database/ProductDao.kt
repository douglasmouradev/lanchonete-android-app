package com.lanchonete.app.data.database

import androidx.room.*
import com.lanchonete.app.data.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    
    @Query("SELECT * FROM products WHERE isActive = 1 ORDER BY name ASC")
    fun getAllActiveProducts(): Flow<List<Product>>
    
    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: String): Product?
    
    @Query("SELECT * FROM products WHERE name LIKE '%' || :searchQuery || '%' AND isActive = 1")
    fun searchProducts(searchQuery: String): Flow<List<Product>>
    
    @Query("SELECT * FROM products WHERE category = :category AND isActive = 1")
    fun getProductsByCategory(category: String): Flow<List<Product>>
    
    @Query("SELECT DISTINCT category FROM products WHERE isActive = 1")
    fun getAllCategories(): Flow<List<String>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)
    
    @Update
    suspend fun updateProduct(product: Product)
    
    @Delete
    suspend fun deleteProduct(product: Product)
    
    @Query("UPDATE products SET isActive = 0 WHERE id = :id")
    suspend fun deactivateProduct(id: String)
}
