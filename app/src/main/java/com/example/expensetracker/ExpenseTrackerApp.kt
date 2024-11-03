package com.example.expensetracker

import android.app.Application
import com.example.expensetracker.entity.AppDatabase


class ExpenseTrackerApp : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}
