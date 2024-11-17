package com.example.expensetracker.entity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.expensetracker.visualizationDataclass.CategoryTotal
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    // Insert a new expense
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExpense(expense: Expense)

    // Update an existing expense
    @Update
    suspend fun updateExpense(expense: Expense)

    // Delete an expense
    @Delete
    suspend fun deleteExpense(expense: Expense)

    // Get all expenses
    @Query("SELECT * FROM expense_table")
    suspend fun getAllExpenses(): List<Expense>

    // Get expenses by category
    @Query("SELECT * FROM expense_table WHERE category = :category ")
    suspend fun getExpensesByCategory(category: String): List<Expense>



    //get expenses by date range and category for recyclerView
    @Query("SELECT * FROM expense_table WHERE date BETWEEN :startDate AND :endDate AND category = :category")
    suspend fun getExpensesByDateRangeAndCategory(startDate: Long, endDate: Long, category: String): List<Expense>

    //get expenses by date range and category for visualization(bar chart)
    @Query("SELECT * FROM expense_table WHERE date BETWEEN :startDate AND :endDate AND category = :category")
    fun getCategoryTotals(startDate: Long,endDate: Long,category: String): Flow<List<CategoryTotal>>


    //get all expenses for pie chart visualization

    @Query("SELECT * FROM expense_table")
      fun getAllExpensesForPieChart():Flow<List<CategoryTotal>>



    // Get an expense by its ID
    @Query("SELECT * FROM expense_table WHERE id = :id")
    suspend fun getExpenseById(id: Int): Expense?


}