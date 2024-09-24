package com.devsan.santiniketanmess

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CalculationSummaryActivity : AppCompatActivity() {

    private lateinit var mealRateTextView: TextView
    private lateinit var establishmentChargeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculation_summary)

        mealRateTextView = findViewById(R.id.tvMealRate)
        establishmentChargeTextView = findViewById(R.id.tvEstablishmentCharge)

        val mealRate = intent.getDoubleExtra("mealRate", 0.0)
        val establishmentCharge = intent.getDoubleExtra("establishmentCharge", 0.0)

        // Display calculated values
        mealRateTextView.text = "Meal Rate: ₹$mealRate"
        establishmentChargeTextView.text = "Establishment Charge: ₹$establishmentCharge"
    }
}
