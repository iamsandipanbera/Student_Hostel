package com.devsan.santiniketanmess

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MealCalculationActivity : AppCompatActivity() {

    private var totalMarketing: Double = 0.0
    private var rice: Double = 0.0
    private var gas: Double = 0.0
    private var paper: Double = 0.0
    private var guestMeal: Double = 0.0
    private var cookCharge: Double = 0.0
    private var otherExpenses: Double = 0.0

    private val totalMembers = 10  // Example value, this should be dynamically fetched
    private val totalMeals = 300   // Example value, replace with actual total meals

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_calculation)

        // Retrieve data passed from previous activity
        totalMarketing = intent.getDoubleExtra("totalMarketing", 0.0)
        rice = intent.getDoubleExtra("rice", 0.0)
        gas = intent.getDoubleExtra("gas", 0.0)
        paper = intent.getDoubleExtra("paper", 0.0)
        guestMeal = intent.getDoubleExtra("guestMeal", 0.0)
        cookCharge = intent.getDoubleExtra("cookCharge", 0.0)
        otherExpenses = intent.getDoubleExtra("otherExpenses", 0.0)

        calculateMealRateAndCharges()
    }

    private fun calculateMealRateAndCharges() {
        val totalCookCharge = cookCharge * totalMembers

        // Meal Rate = [(Total Marketing + Rice + Gas) - Guest Meal] / Total Meals
        val mealRate = ((totalMarketing + rice + gas) - guestMeal) / totalMeals

        // Establishment Charge = (Total Cook Charge + Paper + Other Expenses) / Total Members
        val establishmentCharge = (totalCookCharge + paper + otherExpenses) / totalMembers

        // Display the calculated values
        findViewById<TextView>(R.id.tvMealRate).text = String.format("Meal Rate: %.2f", mealRate)
        findViewById<TextView>(R.id.tvEstablishmentCharge).text =
            String.format("Establishment Charge: %.2f", establishmentCharge)
    }
}
