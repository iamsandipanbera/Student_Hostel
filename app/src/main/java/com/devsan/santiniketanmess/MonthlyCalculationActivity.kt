package com.devsan.santiniketanmess

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlin.math.round

class MonthlyCalculationActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    private var totalMarketing: Double = 0.0
    private var rice: Double = 0.0
    private var gas: Double = 0.0
    private var guestMeal: Double = 0.0
    private var cookCharge: Double = 0.0
    private var paper: Double = 0.0
    private var others: Double = 0.0
    private var totalMembers: Int = 1
    private var totalMeals: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_calculation)

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().reference

        // Fetch all required data
        fetchMonthlyData()
    }

    private fun fetchMonthlyData() {
        // Assuming all data are stored under "monthlyData/currentMonth"
        val currentMonth = "2024-09" // Replace with dynamic month

        database.child("monthlyData").child(currentMonth).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                totalMarketing = snapshot.child("totalMarketing").getValue(Double::class.java) ?: 0.0
                rice = snapshot.child("rice").getValue(Double::class.java) ?: 0.0
                gas = snapshot.child("gas").getValue(Double::class.java) ?: 0.0
                guestMeal = snapshot.child("guestMeal").getValue(Double::class.java) ?: 0.0
                cookCharge = snapshot.child("cookCharge").getValue(Double::class.java) ?: 0.0
                paper = snapshot.child("paper").getValue(Double::class.java) ?: 0.0
                others = snapshot.child("others").getValue(Double::class.java) ?: 0.0
                totalMembers = snapshot.child("totalMembers").getValue(Int::class.java) ?: 1
                totalMeals = snapshot.child("totalMeals").getValue(Int::class.java) ?: 1

                // Perform the calculations
                calculateBillsAndSave()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MonthlyCalculationActivity, "Failed to load data.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun calculateBillsAndSave() {
        // Calculate Meal Rate
        val mealRate = ((totalMarketing + rice + gas) - guestMeal) / totalMeals

        // Calculate Total Cook Charge and Establishment Charge
        val totalCookCharge = cookCharge * totalMembers
        val establishmentCharge = (totalCookCharge + paper + others) / totalMembers

        // Reference to members
        val membersRef = database.child("members")

        membersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (memberSnapshot in snapshot.children) {
                    val memberId = memberSnapshot.key ?: continue
                    val memberName = memberSnapshot.child("name").getValue(String::class.java) ?: "Unknown"
                    val memberMeals = memberSnapshot.child("meals").getValue(Int::class.java) ?: 0
                    val memberDeposit = memberSnapshot.child("totalDeposit").getValue(Double::class.java) ?: 0.0

                    // Calculate total bill and due amount for the member
                    val totalBill = round((memberMeals * mealRate) + establishmentCharge)
                    val dueAmount = round(totalBill - memberDeposit)

                    // Create a map to update member's summary
                    val memberSummary = mapOf(
                        "mealRate" to mealRate,
                        "establishmentCharge" to establishmentCharge,
                        "totalMeals" to memberMeals,
                        "totalBill" to totalBill,
                        "dueAmount" to dueAmount
                    )

                    // Update member's summary in Firebase
                    membersRef.child(memberId).child("summary").setValue(memberSummary)
                }

                Toast.makeText(this@MonthlyCalculationActivity, "Monthly calculations saved.", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MonthlyCalculationActivity, "Failed to calculate bills.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
