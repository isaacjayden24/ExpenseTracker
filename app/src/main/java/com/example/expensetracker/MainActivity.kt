package com.example.expensetracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.fragment_container, ExpenseFragment())
            }
        }


    }
}

/*import com.google.android.material.textfield.TextInputEditText

count characters

val editText: TextInputEditText = findViewById(R.id.editTextDescription)
val charCount: TextView = findViewById(R.id.charCount)

editText.addTextChangedListener(object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        charCount.text = "${s.length}/200 characters"
    }

    override fun afterTextChanged(s: Editable) {}
})
*/