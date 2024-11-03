package com.example.expensetracker.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.ExpenseTrackerApp
import com.example.expensetracker.R
import com.example.expensetracker.VisualizationAdapter
import com.example.expensetracker.entity.ExpenseViewModel
import com.example.expensetracker.entity.ExpenseViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


/**
 * A simple [Fragment] subclass.
 * Use the [VisualizationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VisualizationFragment : Fragment() {



    private lateinit var adapterVisualization:VisualizationAdapter//initialize the adapter of type VisualizationAdapter recycler view
    private lateinit var visualizationRecyclerView: RecyclerView

    private lateinit var dateInput:TextInputEditText // for date input selection
    private lateinit var dateOutPut:TextInputEditText //for date output selection



    private  var  startDate: Long?=null
    private var endDate:Long?=null


    private lateinit var categoryDropdownVisualization: AutoCompleteTextView
    val categories = listOf("Food", "Bills", "Subscription", "Rent", "Electricity","Transport","Entertainment","Fuel","Savings","Friends","clothes","Others")//categories for expenses dropdown

    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory((requireActivity().application as ExpenseTrackerApp).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_visualization, container, false)

        dateInput=view.findViewById(R.id.dateInput)
        dateOutPut=view.findViewById(R.id.dateOutput)

        categoryDropdownVisualization=view.findViewById(R.id.categoryDropdownVisualization)

        //adapter to hold the category items
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        categoryDropdownVisualization.setAdapter(adapter)










        visualizationRecyclerView=view.findViewById(R.id.visualizationRecylerView)

        adapterVisualization=VisualizationAdapter()
        visualizationRecyclerView.adapter=adapterVisualization

        visualizationRecyclerView.layoutManager = LinearLayoutManager(context)


        // Observe LiveData and pass it to the Adapter for display using recycler view
        // Observe the dayExpenses LiveData
        expenseViewModel.dayExpense.observe(viewLifecycleOwner) { expenses ->
            // Update the UI with the list of expenses
            if (expenses != null) {
                // Update the RecyclerView adapter
                adapterVisualization.submitList(expenses)
            }
        }




        ///function initialization

        categoryDropdownVisualization.setOnItemClickListener { _, _, _, _ ->
            if (startDate != null && endDate != null) {
                fetchExpensesStartEndDateWithCategories()
            } else {
                Toast.makeText(context, "Please select a start and end date first", Toast.LENGTH_SHORT).show()
            }
        }




        setupDatePickers()



        return view
    }




    //function to fetch expenses by date range and  categories

   private fun fetchExpensesStartEndDateWithCategories(){

        val selectedCategory = categoryDropdownVisualization.text.toString()

       when {
           selectedCategory.isEmpty() -> {
               Toast.makeText(context, "Please select a category", Toast.LENGTH_SHORT).show()
               return
           }
           startDate == null -> {
               Toast.makeText(context, "Please select a start date", Toast.LENGTH_SHORT).show()
               return
           }
           endDate == null -> {
               Toast.makeText(context, "Please select an end date", Toast.LENGTH_SHORT).show()
               return
           }
           else -> {
               // Show loading indicator
               expenseViewModel.getExpensesByDateRangeAndCategory(
                   startDate!!,
                   endDate!!,
                   selectedCategory
               )
           }
       }





    }



    private fun setupDatePickers() {
        dateInput.setOnClickListener {
            showDatePicker(true)
        }

        dateOutPut.setOnClickListener {
            showDatePicker(false)
        }
    }




    private fun showDatePicker(isStartDate: Boolean) {
        val calendar = Calendar.getInstance()
        val currentDate = if (isStartDate) startDate else endDate

        if (currentDate != null) {
            calendar.timeInMillis = currentDate
        }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                // Set time to start/end of day
                if (isStartDate) {
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                } else {
                    calendar.set(Calendar.HOUR_OF_DAY, 23)
                    calendar.set(Calendar.MINUTE, 59)
                    calendar.set(Calendar.SECOND, 59)
                }

                val timestamp = calendar.timeInMillis

                if (isStartDate) {
                    startDate = timestamp
                    dateInput.setText(formatDate(timestamp))
                } else {
                    // Validate that end date is not before start date
                    if (startDate != null && timestamp < startDate!!) {
                        Toast.makeText(context, "End date cannot be before start date", Toast.LENGTH_SHORT).show()
                        return@DatePickerDialog
                    }
                    endDate = timestamp
                    dateOutPut.setText(formatDate(timestamp))
                }

                // Automatically fetch data if both dates and category are selected
                if (startDate != null && endDate != null && categoryDropdownVisualization.text.isNotEmpty()) {
                    fetchExpensesStartEndDateWithCategories()
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Set min/max dates
        if (!isStartDate && startDate != null) {
            datePickerDialog.datePicker.minDate = startDate!!
        }

        datePickerDialog.show()
    }

    private fun formatDate(timestamp: Long): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(timestamp))
    }


}