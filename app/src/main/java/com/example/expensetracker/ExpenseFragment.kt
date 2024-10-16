package com.example.expensetracker

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.expensetracker.entity.AppDatabase
import com.example.expensetracker.entity.Expense
import com.example.expensetracker.entity.ExpenseViewModel
import com.example.expensetracker.entity.ExpenseViewModelFactory
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * A simple [Fragment] subclass.
 * Use the [ExpenseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExpenseFragment : Fragment() {

    private lateinit var database: AppDatabase // Initialize this with your database instance(object)
    val categories = listOf("Category 1", "Category 2", "Category 3", "Category 4")//categories for expenses dropdown

    private lateinit var dateInput: TextInputEditText//for the date selection
    private var selectedDateInMillis: Long? = null//for the date selection


    //initialize the view model
    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory(database)
    }

    private lateinit var amountInput: EditText
    private lateinit var btnAdd: Button
    private lateinit var adapterExpense: ExpenseAdapter
    private lateinit var categoryDropdown: AutoCompleteTextView
    private lateinit var descriptionInput:EditText
    private lateinit var chipGroup:ChipGroup

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
        categoryDropdown=view.findViewById(R.id.categoryDropdown)
        dateInput=view.findViewById(R.id.dateInput)
        descriptionInput=view.findViewById(R.id.descriptionInput)
        chipGroup=view.findViewById(R.id.tagChipGroup)

        //adapter to hold the category items
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        categoryDropdown.setAdapter(adapter)









        // Initialize the adapter for the recycler view
        adapterExpense = ExpenseAdapter()




        // Disable manual text input for the date field
        dateInput.setOnClickListener {
            showDatePickerDialog()
        }

        btnAdd.setOnClickListener {

            getAmountUser()

        }

        // Observe LiveData
        expenseViewModel.allExpenses.observe(viewLifecycleOwner) { expenses ->
            expenses?.let { adapterExpense.submitList(it) }
        }


        return view
    }

//function to input the date
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)

                selectedDateInMillis = selectedCalendar.timeInMillis // Get the timestamp

                // Format the date and set it in the TextInputEditText
                val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(Date(selectedDateInMillis!!))
                dateInput.setText(formattedDate)
            },
            year, month, day
        )

        datePickerDialog.show()
    }



    private fun getAmountUser() {
        val amount = amountInput.text.toString()
        if (amount.isEmpty()) {
            Toast.makeText(context, "Please enter an amount", Toast.LENGTH_SHORT).show()
            return
        }

        val amountDouble: Double
        try {
            amountDouble = amount.toDouble()
        } catch (e: NumberFormatException) {
            Toast.makeText(context, "Invalid amount entered", Toast.LENGTH_SHORT).show()
            return
        }

        val description = descriptionInput.text.toString()
        if (description.isEmpty()) {
            Toast.makeText(context, "Please enter a description", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedCategory = categoryDropdown.text.toString()
        if (selectedCategory.isEmpty()) {
            Toast.makeText(context, "Please select a category", Toast.LENGTH_SHORT).show()
            return
        }

       /* val selectedChipId = chipGroup.checkedChipId
        val selectedChipText = if (selectedChipId != View.NO_ID) {
            val selectedChip = chipGroup.findViewById<Chip>(selectedChipId)
            selectedChip.text.toString()
        } else {
            null
        }

        if (selectedChipText == null) {
            Toast.makeText(context, "Please select a payment option", Toast.LENGTH_SHORT).show()
            return
        }*/
        // Get the selected chip from the ChipGroup
        val selectedChipId = chipGroup.checkedChipId
        if (selectedChipId == View.NO_ID) {
            Toast.makeText(context, "Please select a payment option", Toast.LENGTH_SHORT).show()
            return
        }

        // Find the selected chip and retrieve its text
        val selectedChip = chipGroup.findViewById<Chip>(selectedChipId)
        val selectedChipText = selectedChip.text.toString()

        if (selectedDateInMillis == null) {
            Toast.makeText(context, "Please select a date", Toast.LENGTH_SHORT).show()
            return
        }


        val newExpense = Expense(
            amount = amountDouble,
            category = selectedCategory,
            details = description,
            date = selectedDateInMillis!!,
            paymentInfo = selectedChipText!!
        )
        expenseViewModel.addExpense(newExpense)

        findNavController().navigate(R.id.action_expenseFragment_to_displayExpenseFragment)
    }






}






