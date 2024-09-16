package com.example.expensetracker.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="Expense_table")
data class Expense( @PrimaryKey(autoGenerate = true) val id: Int = 0,
                   @ColumnInfo(name = "amount") val amount: Double,
                   @ColumnInfo(name = "category") val category: String,
                   @ColumnInfo(name = "details") val details: String?)