package com.devsan.santiniketanmess

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CalculationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculation)

        // Retrieve data from the intent
        val totalMarketing = intent.getDoubleExtra("totalMarketing", 0.0)
        val rice = intent.getDoubleExtra("rice", 0.0)
        val gas = intent.getDoubleExtra("gas", 0.0)
        val guestMeal = intent.getDoubleExtra("guestMeal", 0.0)
        val cookCharge = intent.getDoubleExtra("cookCharge", 0.0)
        val paper = intent.getDoubleExtra("paper", 0.0)
        val others = intent.getDoubleExtra("others", 0.0)

        val totalMembers = 10  // This can be fetched from Firebase or user input
        val totalMeals = 300    // This can be fetched or calculated from meal records

        // Calculate Meal Rate and Establishment Charge
        val mealRate = ((totalMarketing + rice + gas) - guestMeal) / totalMeals
        val totalCookCharge = cookCharge * totalMembers
        val establishmentCharge = (totalCookCharge + paper + others) / totalMembers

        // Update the UI with the calculated values
        findViewById<TextView>(R.id.textViewMealRate).text = "Meal Rate: ₹$mealRate"
        findViewById<TextView>(R.id.textViewEstablishmentCharge).text = "Establishment Charge: ₹$establishmentCharge"
    }
}
