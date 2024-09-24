package com.devsan.santiniketanmess

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MemberDetailsActivity : AppCompatActivity() {

    private lateinit var totalMealsTextView: TextView
    private lateinit var totalDepositsTextView: TextView
    private lateinit var totalBillTextView: TextView
    private lateinit var dueAmountTextView: TextView
    private lateinit var shareButton: Button

    private var mealRate: Double = 0.0
    private var establishmentCharge: Double = 0.0
    private var totalMeals: Int = 0
    private var totalDeposits: Double = 0.0
    private var totalBill: Double = 0.0
    private var dueAmount: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_details)

        // Initialize UI elements
        totalMealsTextView = findViewById(R.id.tvTotalMeals)
        totalDepositsTextView = findViewById(R.id.tvTotalDeposits)
        totalBillTextView = findViewById(R.id.tvTotalBill)
        dueAmountTextView = findViewById(R.id.tvDueAmount)
        shareButton = findViewById(R.id.btnShareDetails)

        // Fetch member data from intent (or Firebase, if needed)
        mealRate = intent.getDoubleExtra("mealRate", 0.0)
        establishmentCharge = intent.getDoubleExtra("establishmentCharge", 0.0)
        totalMeals = intent.getIntExtra("totalMeals", 0)
        totalDeposits = intent.getDoubleExtra("totalDeposits", 0.0)

        // Calculate total bill and due amount
        calculateMemberDetails()

        // Share details via WhatsApp
        shareButton.setOnClickListener {
            shareMemberDetails()
        }
    }

    private fun calculateMemberDetails() {
        totalBill = (totalMeals * mealRate) + establishmentCharge
        dueAmount = totalBill - totalDeposits

        // Set the calculated values in the TextViews
        totalMealsTextView.text = "Total Meals: $totalMeals"
        totalDepositsTextView.text = "Total Deposits: ₹$totalDeposits"
        totalBillTextView.text = "Total Bill: ₹$totalBill"
        dueAmountTextView.text = "Due Amount: ₹$dueAmount"
    }

    private fun shareMemberDetails() {
        val shareText = "Member Details:\n" +
                "Total Meals: $totalMeals\n" +
                "Total Deposits: ₹$totalDeposits\n" +
                "Total Bill: ₹$totalBill\n" +
                "Due Amount: ₹$dueAmount"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(intent, "Share via"))
    }
}
