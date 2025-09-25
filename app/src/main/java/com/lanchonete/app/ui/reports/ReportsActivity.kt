package com.lanchonete.app.ui.reports

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lanchonete.app.R
import com.lanchonete.app.databinding.ActivityReportsBinding
import java.text.NumberFormat
import java.util.*

class ReportsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityReportsBinding
    private lateinit var viewModel: ReportsViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupViewModel()
        setupClickListeners()
        observeViewModel()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Relatórios"
    }
    
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[ReportsViewModel::class.java]
    }
    
    private fun setupClickListeners() {
        binding.buttonDailyReport.setOnClickListener {
            viewModel.generateDailyReport()
        }
        
        binding.buttonWeeklyReport.setOnClickListener {
            viewModel.generateWeeklyReport()
        }
        
        binding.buttonMonthlyReport.setOnClickListener {
            viewModel.generateMonthlyReport()
        }
        
        binding.buttonSalesReport.setOnClickListener {
            viewModel.generateSalesReport()
        }
        
        binding.buttonInventoryReport.setOnClickListener {
            viewModel.generateInventoryReport()
        }
        
        binding.buttonTopProducts.setOnClickListener {
            viewModel.generateTopProductsReport()
        }
    }
    
    private fun observeViewModel() {
        viewModel.dailySales.observe(this) { sales ->
            binding.textDailySales.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(sales)
        }
        
        viewModel.weeklySales.observe(this) { sales ->
            binding.textWeeklySales.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(sales)
        }
        
        viewModel.monthlySales.observe(this) { sales ->
            binding.textMonthlySales.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(sales)
        }
        
        viewModel.totalProducts.observe(this) { count ->
            binding.textTotalProducts.text = count.toString()
        }
        
        viewModel.lowStockCount.observe(this) { count ->
            binding.textLowStockCount.text = count.toString()
        }
        
        viewModel.outOfStockCount.observe(this) { count ->
            binding.textOutOfStockCount.text = count.toString()
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.reports_menu, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_export_report -> {
                // TODO: Implement report export
                true
            }
            R.id.action_print_report -> {
                // TODO: Implement report printing
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    override fun onResume() {
        super.onResume()
        viewModel.loadReportData()
    }
}
