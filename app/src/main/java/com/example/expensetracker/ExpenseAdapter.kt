package com.example.expensetracker

/*import com.example.expensetracker.entity.Expense


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(private var expenses: List<Expense>) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    // ViewHolder class that holds the views for each item
    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
    }

    // Called when RecyclerView needs a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(itemView)
    }

    // Binds the data to the views in each ViewHolder
    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val currentExpense = expenses[position]
        holder.titleTextView.text = currentExpense.title
        holder.amountTextView.text = currentExpense.amount.toString()
        holder.dateTextView.text = currentExpense.date
    }

    // Returns the total number of items
    override fun getItemCount(): Int {
        return expenses.size
    }

    // Method to update the list of expenses
    fun updateExpenses(newExpenses: List<Expense>) {
        this.expenses = newExpenses
        notifyDataSetChanged() // Notify the adapter to refresh the UI
    }
}*/
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.entity.Expense

// ExpenseAdapter class
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
        private val expenseCategoryTextView: TextView = itemView.findViewById(R.id.category_text)
        private val expenseAmountTextView: TextView = itemView.findViewById(R.id.amount_text)
        private val expenseDetailsTextView: TextView = itemView.findViewById(R.id.details_text)

        fun bind(expense: Expense) {
            expenseCategoryTextView.text = expense.category
            expenseAmountTextView.text = expense.amount.toString()
            expenseDetailsTextView.text = expense.details
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
