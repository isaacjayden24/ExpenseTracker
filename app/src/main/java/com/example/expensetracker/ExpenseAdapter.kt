package com.example.expensetracker


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.entity.Expense
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ExpenseAdapter : ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(ExpenseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.expense_item, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = getItem(position)
        holder.bind(expense)
    }

    // ViewHolder class
    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val expenseAmountTextView: TextView = itemView.findViewById(R.id.amount_text)//amount
        private val expenseCategoryTextView: TextView = itemView.findViewById(R.id.category_text)//category
        private val expenseDetailsTextView: TextView = itemView.findViewById(R.id.details_text)//description
        private val expenseDateTextView:TextView=itemView.findViewById(R.id.date_text)//date
        private val expensePaymentMethodTextView:TextView=itemView.findViewById(R.id.payment_method_text)//payment

        @SuppressLint("SetTextI18n")
        fun bind(expense: Expense) {
            expenseCategoryTextView.text = "Category: ${expense.category}"
            expenseAmountTextView.text = "$${expense.amount}"
            expenseDetailsTextView.text = "Description: ${expense.details}"
            // Format and set the date (Convert from milliseconds to readable date format)
            val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(expense.date))
            expenseDateTextView.text= "Date: $formattedDate"
            expensePaymentMethodTextView.text="Payment Method: ${expense.paymentInfo}"
        }
    }

    // DiffUtil class to efficiently update the list
    class ExpenseDiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem.id == newItem.id // Assuming Expense has a unique id field
        }

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem == newItem // Compare all properties
        }
    }
}
