package com.example.expensetracker.entity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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

   /* //Get expenses by day
    @Query("SELECT * FROM expense_table WHERE date = :date")
    suspend fun getExpensesByDay(date: String): List<Expense>*/




    //get expenses by date range and category
    @Query("SELECT * FROM expense_table WHERE date BETWEEN :startDate AND :endDate AND category = :category")
    suspend fun getExpensesByDateRangeAndCategory(startDate: Long, endDate: Long, category: String): List<Expense>




    // Get an expense by its ID
    @Query("SELECT * FROM expense_table WHERE id = :id")
    suspend fun getExpenseById(id: Int): Expense?

   /* // Get all expenses sorted by date
    @Query("SELECT * FROM expense_table ORDER BY date DESC")
    suspend fun getExpensesSortedByDate(): List<Expense>*/
}