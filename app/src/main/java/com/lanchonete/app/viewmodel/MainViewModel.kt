package com.lanchonete.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lanchonete.app.data.database.AppDatabase
import com.lanchonete.app.data.repository.LanchoneteRepository
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = AppDatabase.getDatabase(application)
    private val repository = LanchoneteRepository(
        database.productDao(),
        database.inventoryDao(),
        database.saleDao(),
        database.saleItemDao(),
        database.stockMovementDao()
    )
    
    private val _todaySales = MutableLiveData<Double>()
    val todaySales: LiveData<Double> = _todaySales
    
    private val _todayOrders = MutableLiveData<Int>()
    val todayOrders: LiveData<Int> = _todayOrders
    
    private val _lowStockCount = MutableLiveData<Int>()
    val lowStockCount: LiveData<Int> = _lowStockCount
    
    private val _outOfStockCount = MutableLiveData<Int>()
    val outOfStockCount: LiveData<Int> = _outOfStockCount
    
    fun loadDashboardData() {
        viewModelScope.launch {
            try {
                val today = Date()
                
                // Load today's sales
                val sales = repository.getTotalSalesByDate(today)
                _todaySales.value = sales
                
                // Load today's orders count
                val orders = repository.getSalesCountByDate(today)
                _todayOrders.value = orders
                
                // Load low stock count
                repository.getLowStockItems().collect { items ->
                    _lowStockCount.value = items.size
                }
                
                // Load out of stock count
                repository.getOutOfStockItems().collect { items ->
                    _outOfStockCount.value = items.size
                }
            } catch (e: Exception) {
                // Set default values if database is not ready
                _todaySales.value = 0.0
                _todayOrders.value = 0
                _lowStockCount.value = 0
                _outOfStockCount.value = 0
            }
        }
    }
}
