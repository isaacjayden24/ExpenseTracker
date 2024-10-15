package com.example.expensetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.expensetracker.entity.AppDatabase
import com.example.expensetracker.entity.Expense
import com.example.expensetracker.entity.ExpenseViewModel
import com.example.expensetracker.entity.ExpenseViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [ExpenseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExpenseFragment : Fragment() {

    private lateinit var database: AppDatabase // Initialize this with your database instance(object)

    //initialize the view model
    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory(database)
    }

    private lateinit var amountInput: EditText
    private lateinit var btnAdd: Button
    private lateinit var adapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the database
        database = AppDatabase.getDatabase(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_expense, container, false)

        amountInput = view.findViewById(R.id.amountInput)
        btnAdd = view.findViewById(R.id.btn_add)

        // Initialize the adapter
        adapter = ExpenseAdapter()





        btnAdd.setOnClickListener {
           /* val newExpense = Expense(
                id = 0, // Assuming id is auto-generated
                category = "Abacus",
                amount = 50.0,
                details = "Weekly abacus"
            )
            expenseViewModel.addExpense(newExpense)*/
            getAmountUser()

        }

        // Observe LiveData
        expenseViewModel.allExpenses.observe(viewLifecycleOwner) { expenses ->
            expenses?.let { adapter.submitList(it) }
        }


        return view
    }


    fun getAmountUser() {
        // val amount=expenseInput.text.toString().toIntOrNull()
        /* val amount=Expense(amount = 10.0, category = "genz", details = "test")
      expenseViewModel.addExpense(amount)
      Toast.makeText(context, "Expense added!", Toast.LENGTH_SHORT).show()*/

        val amount2 = Expense(amount = 1.0, category = "pring", details = "pring")
        expenseViewModel.addExpense(amount2)


        findNavController().navigate(R.id.action_expenseFragment_to_displayExpenseFragment)
    }


}






