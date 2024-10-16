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
    private val _allExpenses= MutableLiveData<List<Expense>>()
    val allExpenses: LiveData<List<Expense>>
        get() = _allExpenses


    init {
        fetchAllExpenses()
    }

    // Function to fetch all expenses
    private fun fetchAllExpenses() {
        viewModelScope.launch(Dispatchers.IO) {
            _allExpenses.value = database.expenseDao().getAllExpenses()
        }
    }





    fun addExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            database.expenseDao().insertExpense(expense)
            fetchAllExpenses()

        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch (Dispatchers.IO){
            database.expenseDao().updateExpense(expense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            database.expenseDao().deleteExpense(expense)
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
