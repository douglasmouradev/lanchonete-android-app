package com.lanchonete.app.ui.products

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lanchonete.app.data.database.AppDatabase
import com.lanchonete.app.data.model.Product
import com.lanchonete.app.data.repository.LanchoneteRepository
import kotlinx.coroutines.launch

class ProductManagementViewModel(application: Application) : AndroidViewModel(application) {
    
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
    
    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>> = _categories
    
    private val _productSaved = MutableLiveData<Boolean>()
    val productSaved: LiveData<Boolean> = _productSaved
    
    private val _productDeleted = MutableLiveData<Boolean>()
    val productDeleted: LiveData<Boolean> = _productDeleted
    
    fun loadAllProducts() {
        viewModelScope.launch {
            repository.getAllActiveProducts().collect { productList ->
                _products.value = productList
            }
        }
    }
    
    fun searchProducts(query: String) {
        viewModelScope.launch {
            repository.searchProducts(query).collect { productList ->
                _products.value = productList
            }
        }
    }
    
    fun loadCategories() {
        viewModelScope.launch {
            repository.getAllCategories().collect { categoryList ->
                _categories.value = categoryList
            }
        }
    }
    
    fun saveProduct(product: Product) {
        viewModelScope.launch {
            try {
                repository.insertProduct(product)
                _productSaved.value = true
                loadAllProducts() // Refresh list
            } catch (e: Exception) {
                _productSaved.value = false
            }
        }
    }
    
    fun updateProduct(product: Product) {
        viewModelScope.launch {
            try {
                repository.updateProduct(product)
                _productSaved.value = true
                loadAllProducts() // Refresh list
            } catch (e: Exception) {
                _productSaved.value = false
            }
        }
    }
    
    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            try {
                repository.deleteProduct(product)
                _productDeleted.value = true
                loadAllProducts() // Refresh list
            } catch (e: Exception) {
                _productDeleted.value = false
            }
        }
    }
}
