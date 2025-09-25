package com.lanchonete.app.data.repository

import com.lanchonete.app.data.database.*
import com.lanchonete.app.data.model.*
import kotlinx.coroutines.flow.Flow
import java.util.Date
import java.util.UUID

class LanchoneteRepository(
    private val productDao: ProductDao,
    private val inventoryDao: InventoryDao,
    private val saleDao: SaleDao,
    private val saleItemDao: SaleItemDao,
    private val stockMovementDao: StockMovementDao
) {
    
    // Product operations
    fun getAllActiveProducts(): Flow<List<Product>> = productDao.getAllActiveProducts()
    
    suspend fun getProductById(id: String): Product? = productDao.getProductById(id)
    
    fun searchProducts(query: String): Flow<List<Product>> = productDao.searchProducts(query)
    
    fun getProductsByCategory(category: String): Flow<List<Product>> = productDao.getProductsByCategory(category)
    
    fun getAllCategories(): Flow<List<String>> = productDao.getAllCategories()
    
    suspend fun insertProduct(product: Product) = productDao.insertProduct(product)
    
    suspend fun updateProduct(product: Product) = productDao.updateProduct(product)
    
    suspend fun deleteProduct(product: Product) = productDao.deleteProduct(product)
    
    // Inventory operations
    fun getAllInventoryItems(): Flow<List<InventoryItem>> = inventoryDao.getAllInventoryItems()
    
    suspend fun getInventoryItemByProductId(productId: String): InventoryItem? = 
        inventoryDao.getInventoryItemByProductId(productId)
    
    fun getLowStockItems(): Flow<List<InventoryItem>> = inventoryDao.getLowStockItems()
    
    fun getOutOfStockItems(): Flow<List<InventoryItem>> = inventoryDao.getOutOfStockItems()
    
    suspend fun insertInventoryItem(item: InventoryItem) = inventoryDao.insertInventoryItem(item)
    
    suspend fun updateInventoryItem(item: InventoryItem) = inventoryDao.updateInventoryItem(item)
    
    suspend fun updateStock(productId: String, newStock: Int) = inventoryDao.updateStock(productId, newStock)
    
    // Sales operations
    fun getAllSales(): Flow<List<Sale>> = saleDao.getAllSales()
    
    suspend fun getSaleById(id: String): Sale? = saleDao.getSaleById(id)
    
    fun getSalesByDate(date: Date): Flow<List<Sale>> = saleDao.getSalesByDate(date)
    
    fun getSalesByDateRange(startDate: Date, endDate: Date): Flow<List<Sale>> = 
        saleDao.getSalesByDateRange(startDate, endDate)
    
    suspend fun getTotalSalesByDate(date: Date): Double = saleDao.getTotalSalesByDate(date) ?: 0.0
    
    suspend fun getTotalSalesByDateRange(startDate: Date, endDate: Date): Double = 
        saleDao.getTotalSalesByDateRange(startDate, endDate) ?: 0.0
    
    suspend fun getSalesCountByDate(date: Date): Int = saleDao.getSalesCountByDate(date)
    
    suspend fun insertSale(sale: Sale) = saleDao.insertSale(sale)
    
    suspend fun updateSale(sale: Sale) = saleDao.updateSale(sale)
    
    // Sale Items operations
    fun getSaleItemsBySaleId(saleId: String): Flow<List<SaleItem>> = 
        saleItemDao.getSaleItemsBySaleId(saleId)
    
    suspend fun insertSaleItems(saleItems: List<SaleItem>) = saleItemDao.insertSaleItems(saleItems)
    
    // Stock Movement operations
    fun getAllStockMovements(): Flow<List<StockMovement>> = stockMovementDao.getAllStockMovements()
    
    fun getStockMovementsByProductId(productId: String): Flow<List<StockMovement>> = 
        stockMovementDao.getStockMovementsByProductId(productId)
    
    suspend fun insertStockMovement(movement: StockMovement) = stockMovementDao.insertStockMovement(movement)
    
    // Complex operations
    suspend fun processSale(sale: Sale, saleItems: List<SaleItem>) {
        // Insert sale
        insertSale(sale)
        
        // Insert sale items
        insertSaleItems(saleItems)
        
        // Update stock for each item
        saleItems.forEach { item ->
            val inventoryItem = getInventoryItemByProductId(item.productId)
            inventoryItem?.let {
                val newStock = it.currentStock - item.quantity
                updateStock(item.productId, newStock)
                
                // Record stock movement
                val movement = StockMovement(
                    id = UUID.randomUUID().toString(),
                    productId = item.productId,
                    productName = item.productName,
                    movementType = MovementType.OUT,
                    quantity = item.quantity,
                    previousStock = it.currentStock,
                    newStock = newStock,
                    reason = "Venda - ${sale.id}",
                    responsible = sale.cashierName,
                    createdAt = Date()
                )
                insertStockMovement(movement)
            }
        }
    }
    
    suspend fun restockProduct(productId: String, quantity: Int, responsible: String) {
        val inventoryItem = getInventoryItemByProductId(productId)
        inventoryItem?.let {
            val newStock = it.currentStock + quantity
            updateStock(productId, newStock)
            
            // Record stock movement
            val movement = StockMovement(
                id = UUID.randomUUID().toString(),
                productId = productId,
                productName = it.productName,
                movementType = MovementType.IN,
                quantity = quantity,
                previousStock = it.currentStock,
                newStock = newStock,
                reason = "Reposição de estoque",
                responsible = responsible,
                createdAt = Date()
            )
            insertStockMovement(movement)
        }
    }
}
