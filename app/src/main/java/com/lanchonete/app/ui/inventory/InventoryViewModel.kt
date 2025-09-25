package com.lanchonete.app.ui.inventory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lanchonete.app.data.database.AppDatabase
import com.lanchonete.app.data.model.InventoryItem
import com.lanchonete.app.data.repository.LanchoneteRepository
import kotlinx.coroutines.launch

class InventoryViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = AppDatabase.getDatabase(application)
    private val repository = LanchoneteRepository(
        database.productDao(),
        database.inventoryDao(),
        database.saleDao(),
        database.saleItemDao(),
        database.stockMovementDao()
    )
    
    private val _inventoryItems = MutableLiveData<List<InventoryItem>>()
    val inventoryItems: LiveData<List<InventoryItem>> = _inventoryItems
    
    private val _lowStockItems = MutableLiveData<List<InventoryItem>>()
    val lowStockItems: LiveData<List<InventoryItem>> = _lowStockItems
    
    private val _outOfStockItems = MutableLiveData<List<InventoryItem>>()
    val outOfStockItems: LiveData<List<InventoryItem>> = _outOfStockItems
    
    private val _stockUpdated = MutableLiveData<Boolean>()
    val stockUpdated: LiveData<Boolean> = _stockUpdated
    
    fun loadInventoryData() {
        viewModelScope.launch {
            repository.getAllInventoryItems().collect { items ->
                _inventoryItems.value = items
            }
            
            repository.getLowStockItems().collect { items ->
                _lowStockItems.value = items
            }
            
            repository.getOutOfStockItems().collect { items ->
                _outOfStockItems.value = items
            }
        }
    }
    
    fun filterLowStock() {
        viewModelScope.launch {
            repository.getLowStockItems().collect { items ->
                _inventoryItems.value = items
            }
        }
    }
    
    fun filterOutOfStock() {
        viewModelScope.launch {
            repository.getOutOfStockItems().collect { items ->
                _inventoryItems.value = items
            }
        }
    }
    
    fun showAllItems() {
        viewModelScope.launch {
            repository.getAllInventoryItems().collect { items ->
                _inventoryItems.value = items
            }
        }
    }
    
    fun restockProduct(productId: String, quantity: Int, responsible: String) {
        viewModelScope.launch {
            try {
                repository.restockProduct(productId, quantity, responsible)
                _stockUpdated.value = true
                loadInventoryData() // Refresh data
            } catch (e: Exception) {
                _stockUpdated.value = false
            }
        }
    }
    
    fun adjustStock(productId: String, newStock: Int, responsible: String) {
        viewModelScope.launch {
            try {
                repository.updateStock(productId, newStock)
                _stockUpdated.value = true
                loadInventoryData() // Refresh data
            } catch (e: Exception) {
                _stockUpdated.value = false
            }
        }
    }
}
