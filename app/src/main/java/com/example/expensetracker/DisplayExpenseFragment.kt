package com.example.expensetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.entity.AppDatabase
import com.example.expensetracker.entity.ExpenseViewModel
import com.example.expensetracker.entity.ExpenseViewModelFactory


/**
 * A simple [Fragment] subclass.
 * Use the [DisplayExpenseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DisplayExpenseFragment : Fragment() {

    private lateinit var database: AppDatabase

    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory(database)
    }

    private lateinit var expenseRecyclerView: RecyclerView
    private lateinit var adapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = AppDatabase.getDatabase(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_display_expense, container, false)

        // Initialize RecyclerView and Adapter
        expenseRecyclerView = view.findViewById(R.id.expenseRecyclerView)
        adapter = ExpenseAdapter()
        expenseRecyclerView.adapter = adapter
        expenseRecyclerView.layoutManager = LinearLayoutManager(context)

        // Observe LiveData
        expenseViewModel.allExpenses.observe(viewLifecycleOwner) { expenses ->
            expenses?.let { adapter.submitList(it) }
        }






        return view
    }


}