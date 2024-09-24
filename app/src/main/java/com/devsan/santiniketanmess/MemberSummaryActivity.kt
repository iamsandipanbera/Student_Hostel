package com.devsan.santiniketanmess

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.devsan.santiniketanmess.repository.FirestoreRepository
import com.devsan.santiniketanmess.utils.CalculationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemberSummaryActivity : AppCompatActivity() {

    private lateinit var memberNameTextView: TextView
    private lateinit var totalMealsTextView: TextView
    private lateinit var totalDepositTextView: TextView
    private lateinit var totalBillTextView: TextView
    private lateinit var dueAmountTextView: TextView
    private lateinit var shareButton: Button

    private val calculationHelper = CalculationHelper()
    private val firestoreRepository = FirestoreRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_summary)

        memberNameTextView = findViewById(R.id.memberNameTextView)
        totalMealsTextView = findViewById(R.id.totalMealsTextView)
        totalDepositTextView = findViewById(R.id.totalDepositTextView)
        totalBillTextView = findViewById(R.id.totalBillTextView)
        dueAmountTextView = findViewById(R.id.dueAmountTextView)
        shareButton = findViewById(R.id.shareButton)

        val memberId = intent.getStringExtra("MEMBER_ID")

        if (memberId != null) {
            fetchMemberDetails(memberId)
        }

        shareButton.setOnClickListener {
            shareMemberSummary()
        }
    }

    private fun fetchMemberDetails(memberId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val member = firestoreRepository.getAllMembers().find { it.id == memberId }
            val monthlyExpense = firestoreRepository.getMonthlyExpenses("September") // Example month

            if (member != null && monthlyExpense != null) {
                val mealRate = calculationHelper.calculateMealRate(
                    monthlyExpense.totalMarketing,
                    monthlyExpense.rice,
                    monthlyExpense.gas,
                    monthlyExpense.guestMeal,
                    member.totalMeals
                )

                val establishmentCharge = calculationHelper.calculateEstablishmentCharge(
                    monthlyExpense.cookCharge,
                    monthlyExpense.paperCharge,
                    monthlyExpense.otherCharge,
                    10 // Assuming 10 members for calculation
                )

                val totalMonthlyBill = calculationHelper.calculateTotalMonthlyBill(
                    member.totalMeals, mealRate, establishmentCharge
                )

                val dueAmount = calculationHelper.calculateMonthlyDue(totalMonthlyBill, member.totalDeposit)

                runOnUiThread {
                    memberNameTextView.text = member.name
                    totalMealsTextView.text = "Total Meals: ${member.totalMeals}"
                    totalDepositTextView.text = "Total Deposit: ₹${member.totalDeposit}"
                    totalBillTextView.text = "Total Bill: ₹$totalMonthlyBill"
                    dueAmountTextView.text = "Due Amount: ₹$dueAmount"
                }
            }
        }
    }

    private fun shareMemberSummary() {
        val shareMessage = "${memberNameTextView.text}\n${totalMealsTextView.text}\n${totalDepositTextView.text}\n${totalBillTextView.text}\n${dueAmountTextView.text}"

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(intent, "Share via"))
    }
}
