package com.example.expensetracker.entity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ExpenseViewModel(private val database: AppDatabase) : ViewModel() {


    //Live data for all expenses
    private val _allExpenses = MutableLiveData<List<Expense>>()
    val allExpenses: LiveData<List<Expense>>
        get() = _allExpenses


    //live data for expenses by day

    private val _dayExpenses=MutableLiveData<List<Expense>>()
    val dayExpense:LiveData<List<Expense>> get() = _dayExpenses


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

    //function to fetch expense by date

        /*fun getExpenseByDate(){

            viewModelScope.launch (Dispatchers.IO){
               val dayExpense= database.expenseDao().getExpensesByDay("1729461257117")
                _dayExpenses.postValue(dayExpense)
            }

        }*/


    // function to get expense by date range and category
    fun getExpensesByDateRangeAndCategory(startDate: Long, endDate: Long, category: String) {
        viewModelScope.launch {
            val result = database.expenseDao().getExpensesByDateRangeAndCategory(startDate, endDate, category)
            _dayExpenses.postValue(result)
        }
    }


   /* fun getExpenseByDay(){
        viewModelScope.launch {
            val result=database.expenseDao().getExpensesByDay("1729342687072")
            _dayExpenses.postValue(result)
        }
    }*/


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






/*
class ExpenseViewModel(private val database: AppDatabase) : ViewModel() {

    // Live data for all expenses
    val allExpenses: LiveData<List<Expense>> = database.expenseDao()

    // Function to add an expense
    fun addExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                database.expenseDao().insertExpense(expense)
            } catch (e: Exception) {
                // Handle error (e.g., log it or notify the UI)
            }
        }
    }

    // Function to update an expense
    fun updateExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                database.expenseDao().updateExpense(expense)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    // Function to delete an expense
    fun deleteExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                database.expenseDao().deleteExpense(expense)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

// ViewModel factory
class ExpenseViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

 */
