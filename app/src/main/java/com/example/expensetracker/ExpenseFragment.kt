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

    private lateinit var database: AppDatabase // Initialize this with your database instance
    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory(database)
    }

    private lateinit var expenseInput: EditText
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

        // Initialize the adapter
        adapter = ExpenseAdapter()



        expenseInput = view.findViewById(R.id.expenseInput)
        btnAdd = view.findViewById(R.id.btn_add)

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



    //this function should retrieve all expenses and display them as live data
   /* fun allUser():List<Expense>{

        return expenseViewModel.allExpenses()

    }*/


   /* fun updateUser(){

    }*/

   //pass the all user here after retrieval to be deleted
   /* fun deleteUser(exp:List<Expense>){
        expenseViewModel.deleteExpense(exp[0])

    }*/


