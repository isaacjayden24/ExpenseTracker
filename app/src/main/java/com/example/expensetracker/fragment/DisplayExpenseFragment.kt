package com.example.expensetracker.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.ExpenseAdapter
import com.example.expensetracker.ExpenseTrackerApp
import com.example.expensetracker.R
import com.example.expensetracker.entity.ExpenseViewModel
import com.example.expensetracker.entity.ExpenseViewModelFactory



class DisplayExpenseFragment : Fragment() {



    private val expenseViewModel: ExpenseViewModel by viewModels {
      ExpenseViewModelFactory((requireActivity().application as ExpenseTrackerApp).database)
   }


    private lateinit var expenseRecyclerView: RecyclerView
    private lateinit var adapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_display_expense, container, false)

        // Set up toolbar
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        // Enable back button in the toolbar
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Handle the navigation icon click
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack() // Go back to the previous fragment
        }


        // Initialize RecyclerView and Adapter
        expenseRecyclerView = view.findViewById(R.id.expenseRecyclerView)
        // Pass the delete lambda to the adapter
        adapter = ExpenseAdapter { expense ->
            expenseViewModel.deleteExpense(expense)
        }

        expenseRecyclerView.adapter = adapter
        expenseRecyclerView.layoutManager = LinearLayoutManager(context)

        // Observe LiveData and pass it to the Adapter for display using recycler view
        expenseViewModel.allExpenses.observe(viewLifecycleOwner) { expenses ->
            expenses?.let { adapter.submitList(it) }
        }








        return view
    }


}