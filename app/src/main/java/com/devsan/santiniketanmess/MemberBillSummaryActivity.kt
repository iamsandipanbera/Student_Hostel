package com.devsan.santiniketanmess

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class MemberBillSummaryActivity : AppCompatActivity() {

    private lateinit var totalMealsView: TextView
    private lateinit var totalDepositView: TextView
    private lateinit var totalBillView: TextView
    private lateinit var dueAmountView: TextView
    private lateinit var shareButton: Button

    private lateinit var memberId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_bill_summary)

        // Initialize views
        totalMealsView = findViewById(R.id.textViewTotalMeals)
        totalDepositView = findViewById(R.id.textViewTotalDeposit)
        totalBillView = findViewById(R.id.textViewTotalBill)
        dueAmountView = findViewById(R.id.textViewDueAmount)
        shareButton = findViewById(R.id.buttonShare)

        // Get memberId passed via Intent
        memberId = intent.getStringExtra("MEMBER_ID") ?: return

        // Load and display the member's bill summary
        loadMemberBillSummary()

        // Share button click listener
        shareButton.setOnClickListener {
            shareBillDetails()
        }
    }

    private fun loadMemberBillSummary() {
        val billCalculation = MonthlyBillCalculation()

        billCalculation.calculateMonthlyBill(memberId) { totalBill, dueAmount ->
            // Update UI with the calculated values
            totalBillView.text = "Total Bill: ₹$totalBill"
            dueAmountView.text = "Due Amount: ₹$dueAmount"

            // Fetch meals and deposit from Firebase for display
            FirebaseDatabase.getInstance().getReference("monthlyData/2024-09/members/$memberId").get()
                .addOnSuccessListener { snapshot ->
                    val totalMeals = snapshot.child("meals").getValue(Int::class.java) ?: 0
                    val totalDeposit = snapshot.child("totalDeposit").getValue(Double::class.java) ?: 0.0

                    totalMealsView.text = "Total Meals: $totalMeals"
                    totalDepositView.text = "Total Deposit: ₹$totalDeposit"
                }
        }
    }

    private fun shareBillDetails() {
        // Fetch current values from the UI
        val totalMeals = totalMealsView.text.toString()
        val totalDeposit = totalDepositView.text.toString()
        val totalBill = totalBillView.text.toString()
        val dueAmount = dueAmountView.text.toString()

        // Create the share message
        val shareText = """
            Member Bill Summary:
            $totalMeals
            $totalDeposit
            $totalBill
            $dueAmount
        """.trimIndent()

        // Create an intent to share the details via WhatsApp
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareText)
        intent.setPackage("com.whatsapp")

        // Start the intent to open WhatsApp
        startActivity(Intent.createChooser(intent, "Share via"))
    }
}
