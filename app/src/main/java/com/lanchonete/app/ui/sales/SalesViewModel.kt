package com.lanchonete.app.ui.sales

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lanchonete.app.data.database.AppDatabase
import com.lanchonete.app.data.model.Product
import com.lanchonete.app.data.model.Sale
import com.lanchonete.app.data.model.SaleItem
import com.lanchonete.app.data.repository.LanchoneteRepository
import kotlinx.coroutines.launch

class SalesViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = AppDatabase.getDatabase(application)
    private val repository = LanchoneteRepository(
        database.productDao(),
        database.inventoryDao(),
        database.saleDao(),
        database.saleItemDao(),
        database.stockMovementDao()
    )
    
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products
    
    private val _saleCompleted = MutableLiveData<Boolean>()
    val saleCompleted: LiveData<Boolean> = _saleCompleted
    
    fun loadProducts() {
        viewModelScope.launch {
            repository.getAllActiveProducts().collect { productList ->
                _products.value = productList
            }
        }
    }
    
    fun processSale(sale: Sale, saleItems: List<SaleItem>) {
        viewModelScope.launch {
            try {
                repository.processSale(sale, saleItems)
                _saleCompleted.value = true
            } catch (e: Exception) {
                _saleCompleted.value = false
            }
        }
    }
}
