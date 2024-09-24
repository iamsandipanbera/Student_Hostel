package com.devsan.santiniketanmess

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class MonthlyExpensesActivity : AppCompatActivity() {

    private lateinit var totalMarketingEditText: EditText
    private lateinit var riceEditText: EditText
    private lateinit var gasEditText: EditText
    private lateinit var guestMealEditText: EditText
    private lateinit var cookChargeEditText: EditText
    private lateinit var paperEditText: EditText
    private lateinit var othersEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_expenses)

        // Initialize the input fields
        totalMarketingEditText = findViewById(R.id.editTextTotalMarketing)
        riceEditText = findViewById(R.id.editTextRice)
        gasEditText = findViewById(R.id.editTextGas)
        guestMealEditText = findViewById(R.id.editTextGuestMeal)
        cookChargeEditText = findViewById(R.id.editTextCookCharge)
        paperEditText = findViewById(R.id.editTextPaper)
        othersEditText = findViewById(R.id.editTextOthers)

        // Button to go to the next page for calculations
        val nextButton = findViewById<Button>(R.id.buttonNext)
        nextButton.setOnClickListener {
            saveExpensesAndGoToNextPage()
        }
    }

    private fun saveExpensesAndGoToNextPage() {
        val totalMarketing = totalMarketingEditText.text.toString().toDoubleOrNull() ?: 0.0
        val rice = riceEditText.text.toString().toDoubleOrNull() ?: 0.0
        val gas = gasEditText.text.toString().toDoubleOrNull() ?: 0.0
        val guestMeal = guestMealEditText.text.toString().toDoubleOrNull() ?: 0.0
        val cookCharge = cookChargeEditText.text.toString().toDoubleOrNull() ?: 0.0
        val paper = paperEditText.text.toString().toDoubleOrNull() ?: 0.0
        val others = othersEditText.text.toString().toDoubleOrNull() ?: 0.0

        // Save the data to Firebase (or locally if needed)
        val database = FirebaseDatabase.getInstance().getReference("monthly_expenses")
        val expenses = MonthlyExpenses(totalMarketing, rice, gas, guestMeal, cookCharge, paper, others)
        database.child("current_month").setValue(expenses)

        // Navigate to CalculationActivity with the necessary details
        val intent = Intent(this, CalculationActivity::class.java)
        intent.putExtra("totalMarketing", totalMarketing)
        intent.putExtra("rice", rice)
        intent.putExtra("gas", gas)
        intent.putExtra("guestMeal", guestMeal)
        intent.putExtra("cookCharge", cookCharge)
        intent.putExtra("paper", paper)
        intent.putExtra("others", others)
        startActivity(intent)
    }
}
