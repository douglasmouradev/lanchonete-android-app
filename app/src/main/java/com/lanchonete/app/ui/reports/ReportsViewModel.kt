package com.lanchonete.app.ui.reports

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lanchonete.app.data.database.AppDatabase
import com.lanchonete.app.data.repository.LanchoneteRepository
import kotlinx.coroutines.launch
import java.util.*

class ReportsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = AppDatabase.getDatabase(application)
    private val repository = LanchoneteRepository(
        database.productDao(),
        database.inventoryDao(),
        database.saleDao(),
        database.saleItemDao(),
        database.stockMovementDao()
    )
    
    private val _dailySales = MutableLiveData<Double>()
    val dailySales: LiveData<Double> = _dailySales
    
    private val _weeklySales = MutableLiveData<Double>()
    val weeklySales: LiveData<Double> = _weeklySales
    
    private val _monthlySales = MutableLiveData<Double>()
    val monthlySales: LiveData<Double> = _monthlySales
    
    private val _totalProducts = MutableLiveData<Int>()
    val totalProducts: LiveData<Int> = _totalProducts
    
    private val _lowStockCount = MutableLiveData<Int>()
    val lowStockCount: LiveData<Int> = _lowStockCount
    
    private val _outOfStockCount = MutableLiveData<Int>()
    val outOfStockCount: LiveData<Int> = _outOfStockCount
    
    fun loadReportData() {
        viewModelScope.launch {
            val today = Date()
            val calendar = Calendar.getInstance()
            
            // Daily sales
            val daily = repository.getTotalSalesByDate(today)
            _dailySales.value = daily
            
            // Weekly sales (last 7 days)
            calendar.time = today
            calendar.add(Calendar.DAY_OF_YEAR, -7)
            val weekStart = calendar.time
            val weekly = repository.getTotalSalesByDateRange(weekStart, today)
            _weeklySales.value = weekly
            
            // Monthly sales (last 30 days)
            calendar.time = today
            calendar.add(Calendar.DAY_OF_YEAR, -30)
            val monthStart = calendar.time
            val monthly = repository.getTotalSalesByDateRange(monthStart, today)
            _monthlySales.value = monthly
            
            // Product counts
            repository.getAllActiveProducts().collect { products ->
                _totalProducts.value = products.size
            }
            
            repository.getLowStockItems().collect { items ->
                _lowStockCount.value = items.size
            }
            
            repository.getOutOfStockItems().collect { items ->
                _outOfStockCount.value = items.size
            }
        }
    }
    
    fun generateDailyReport() {
        // TODO: Implement daily report generation
    }
    
    fun generateWeeklyReport() {
        // TODO: Implement weekly report generation
    }
    
    fun generateMonthlyReport() {
        // TODO: Implement monthly report generation
    }
    
    fun generateSalesReport() {
        // TODO: Implement sales report generation
    }
    
    fun generateInventoryReport() {
        // TODO: Implement inventory report generation
    }
    
    fun generateTopProductsReport() {
        // TODO: Implement top products report generation
    }
}
