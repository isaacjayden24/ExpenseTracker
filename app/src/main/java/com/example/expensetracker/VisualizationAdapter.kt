package com.example.expensetracker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.entity.Expense
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VisualizationAdapter
    : ListAdapter<Expense, VisualizationAdapter.VisualizationViewHolder>(VisualizationAdapter.ExpenseDiffCallback()) {




    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VisualizationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.visualization_item, parent, false)
        return VisualizationViewHolder(view)
    }

    override fun onBindViewHolder(holder: VisualizationViewHolder, position: Int) {
        val expense = getItem(position)
        holder.bind(expense)
    }

    // ViewHolder class
    class VisualizationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val expenseName: TextView = itemView.findViewById(R.id.expenseName)
        private val expenseDateTransaction:TextView=itemView.findViewById(R.id.expenseDateTransaction)
        private val expenseCost:TextView=itemView.findViewById(R.id.expenseCost)
        private val expenseLogo:ImageView=itemView.findViewById(R.id.expenseLogo)

        @SuppressLint("SetTextI18n")
        fun bind(expense: Expense?) {

            if (expense != null) {
                expenseName.text=expense.category


                // Format and set the date (Convert from milliseconds to readable date format)
                val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(expense.date))
                expenseDateTransaction.text= "Date: $formattedDate"

               // expenseCost.text=expense.amount.toString()
                expenseCost.text="-$ ${expense.amount}"

                // Check the type of transaction and load the relevant image
                val logoResId = when (expense.category) {
                    "Food" -> R.drawable.ic_food
                    "Bills"->R.drawable.ic_bill
                    "Subscription"->R.drawable.ic_subscription
                    "Rent"->R.drawable.ic_rent
                    "Electricity"->R.drawable.ic_plug
                    "Transport"->R.drawable.ic_bus
                    "Entertainment"->R.drawable.happy
                    "fuel" -> R.drawable.ic_fuel
                    "Savings"->R.drawable.ic_savings
                    "Friends"->R.drawable.ic_friends
                    "clothes"->R.drawable.ic_clothes
                    "Others"->R.drawable.ic_default


                    else -> R.drawable.ic_default // Fallback to a default image
                }

                // Set the image resource to expenseLogo
                expenseLogo.setImageResource(logoResId)

            }


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