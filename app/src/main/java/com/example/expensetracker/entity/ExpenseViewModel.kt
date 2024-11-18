package com.example.expensetracker.entity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.visualizationDataclass.CategoryTotal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ExpenseViewModel(private val database: AppDatabase) : ViewModel() {



    //Live data for all expenses
    private val _allExpenses = MutableLiveData<List<Expense>>()
    val allExpenses: LiveData<List<Expense>>
        get() = _allExpenses


    //live data for expenses by day

    private val _dayExpenses = MutableLiveData<List<Expense>>()
    val dayExpense: LiveData<List<Expense>> get() = _dayExpenses


    //live data to hold the visualizations items {category,total} for bar chart

    private val _categoryTotals = MutableLiveData<List<CategoryTotal>>()
    val categoryTotals: LiveData<List<CategoryTotal>> = _categoryTotals


    //live data to hold the visualizations items {category,total} for pie chart
    val _pieChartExpenses = MutableLiveData<List<CategoryTotal>>()
    val pieChartExpenses: LiveData<List<CategoryTotal>> = _pieChartExpenses


    init {
        fetchAllExpenses()
    }

    // Function to fetch all expenses
    private fun fetchAllExpenses() {
        viewModelScope.launch(Dispatchers.IO) {

            val expenseFetch = database.expenseDao().getAllExpenses()
            _allExpenses.postValue(expenseFetch)

        }
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            database.expenseDao().insertExpense(expense)
            fetchAllExpenses()

        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            database.expenseDao().updateExpense(expense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            database.expenseDao().deleteExpense(expense)
            fetchAllExpenses()
        }
    }


    // function to get expense by date range and category(recycler view)
    fun getExpensesByDateRangeAndCategory(startDate: Long, endDate: Long, category: String) {
        viewModelScope.launch {
            val result = database.expenseDao()
                .getExpensesByDateRangeAndCategory(startDate, endDate, category)
            _dayExpenses.postValue(result)
        }
    }

    //function to get expense by date range and category(bar chart visualization)
    fun getCategoryTotals(startDate: Long, endDate: Long, category: String) {
        viewModelScope.launch {
            database.expenseDao().getCategoryTotals(startDate, endDate, category)
                .collect { data ->
                    _categoryTotals.value = data
                }
        }
    }


    //function to get all expenses for pie chart visualization
    fun getPieChartExpenses() {
        viewModelScope.launch {
            database.expenseDao().getAllExpensesForPieChart()
                .collect { data ->
                    _pieChartExpenses.value = data
                }
        }
    }



}

class ExpenseViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



