package com.example.expensetracker.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.ExpenseTrackerApp
import com.example.expensetracker.R
import com.example.expensetracker.VisualizationAdapter
import com.example.expensetracker.entity.ExpenseViewModel
import com.example.expensetracker.entity.ExpenseViewModelFactory
import com.example.expensetracker.visualizationDataclass.CategoryTotal
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class VisualizationFragment : Fragment() {



    private lateinit var adapterVisualization:VisualizationAdapter//initialize the adapter of type VisualizationAdapter recycler view
    private lateinit var visualizationRecyclerView: RecyclerView

    private lateinit var dateInput:TextInputEditText // for date input selection
    private lateinit var dateOutPut:TextInputEditText //for date output selection


    private lateinit var barChart:BarChart
    private lateinit var pieChart:PieChart


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

        // Set up toolbar
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        // Enable back button in the toolbar
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Handle the navigation icon click
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack() // Go back to the previous fragment
        }

        dateInput=view.findViewById(R.id.dateInput)
        dateOutPut=view.findViewById(R.id.dateOutput)

        barChart=view.findViewById(R.id.barChart)
        pieChart=view.findViewById(R.id.pieChart)











        ////category dropdown initialization for visualization

        categoryDropdownVisualization=view.findViewById(R.id.categoryDropdownVisualization)

        //adapter to hold the category items
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        categoryDropdownVisualization.setAdapter(adapter)



        ///function initialization for category dropdown visualization

        categoryDropdownVisualization.setOnItemClickListener { _, _, _, _ ->
            if (startDate != null && endDate != null) {
                fetchExpensesStartEndDateWithCategories()

            } else {
                Toast.makeText(context, "Please select a start and end date first", Toast.LENGTH_SHORT).show()
            }
        }










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





        //observe the live data for  bar chart data for visualization and pass params
        expenseViewModel.categoryTotals.observe(viewLifecycleOwner) { categoryTotals ->
            if (categoryTotals != null) {
                setupBarChart(categoryTotals)

            }
        }

        //observe the live data for pie chart for visualization and pass params
        expenseViewModel.pieChartExpenses.observe(viewLifecycleOwner){pieChartExpenses ->
            if (pieChartExpenses != null) {
                setupPieChart(pieChartExpenses)
            }

        }













        setupDatePickers()









        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchPiechart()


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

               //initialize function for bar chart passing params to the view model
               expenseViewModel.getCategoryTotals(startDate!!,endDate!!,selectedCategory)


           }
       }







   }


    //function to fetch the pie chart function from view model
    private fun fetchPiechart(){
        //initialize function for pie chart passing params to the view model
        expenseViewModel.getPieChartExpenses()
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










    private fun setupBarChart(categoryTotals: List<CategoryTotal>) {
        // Create entries for each category and amount
        val entries = categoryTotals.mapIndexed { index, categoryTotal ->
            BarEntry(index.toFloat(), categoryTotal.amount.toFloat())
        }

        // DataSet with label and color customization
        val dataSet = BarDataSet(entries, "Expenses by Category").apply {
            setColors(*ColorTemplate.MATERIAL_COLORS)  // Apply multiple colors from the template
            valueTextColor = Color.BLACK  // Text color for the values
            valueTextSize = 12f           // Size for the value text
        }

        // BarData to hold the DataSet
        val barData = BarData(dataSet).apply {
            barWidth = 0.7f // Width of each bar for better spacing
        }

        // Configure the BarChart
        barChart.apply {
            data = barData
            setFitBars(true) // Make bars fit into the chart

            // Configure X-axis with category labels
            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(categoryTotals.map { it.category })
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                isGranularityEnabled = true
                setDrawGridLines(false) // Hide grid lines for cleaner look
                textSize = 12f // Text size for labels
            }

            // Customize other axes and UI options
            axisRight.isEnabled = false // Disable right Y-axis
            axisLeft.apply {
                granularity = 1f
                axisMinimum = 0f // Start Y-axis from zero
            }
            description.isEnabled = false // Disable description text
            legend.isEnabled = false // Disable legend if not needed

            invalidate() // Refresh the chart
        }
    }





    //function to set up pie chart

    private fun setupPieChart(categoryTotals: List<CategoryTotal>) {
        // Create entries for each category and amount
        val entries = categoryTotals.map { categoryTotal ->
            PieEntry(categoryTotal.amount.toFloat(), categoryTotal.category)
        }

        // Create the DataSet and customize colors
        val dataSet = PieDataSet(entries, null).apply { // Set dataset label to null to avoid a single legend entry
            setColors(*ColorTemplate.MATERIAL_COLORS)  // Multiple colors for each slice
            valueTextColor = Color.WHITE               // Text color for values inside the pie
            valueTextSize = 12f                        // Text size for values
            sliceSpace = 2f                            // Space between slices
        }

        // PieData to hold the DataSet
        val pieData = PieData(dataSet).apply {
            setValueFormatter(object : ValueFormatter() {
                @SuppressLint("DefaultLocale")
                override fun getFormattedValue(value: Float): String {
                    return "${String.format("%.1f", value)}%"  // Show values as percentages
                }
            })
        }

        // Configure the PieChart
        pieChart.apply {
            data = pieData
            isDrawHoleEnabled = true
            holeRadius = 45f
            transparentCircleRadius = 50f
            centerText = "Expenses Breakdown"
            setCenterTextSize(18f)
            setEntryLabelTextSize(12f)
            setEntryLabelColor(Color.BLACK)
            setUsePercentValues(true)
            description.isEnabled = false

            // Enable legend and configure it
            legend.apply {
                isEnabled = true
                form = Legend.LegendForm.CIRCLE
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                orientation = Legend.LegendOrientation.HORIZONTAL
                textSize = 12f
            }

             // Disable the legend
            legend.isEnabled = false

            invalidate() // Refresh the chart
        }
    }











}

















