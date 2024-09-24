package com.devsan.santiniketanmess

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class MonthlyComparisonActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var comparisonTextView: TextView

    private var currentMonth = "2024-09" // Example current month
    private var previousMonth = "2024-08" // Example previous month

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_comparison)

        // Initialize UI components
        comparisonTextView = findViewById(R.id.textViewComparison)

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().reference

        // Fetch current and previous month data
        compareMonthlyData()
    }

    private fun compareMonthlyData() {
        database.child("monthlyData").child(currentMonth)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(currentSnapshot: DataSnapshot) {
                    val currentMarketing = currentSnapshot.child("totalMarketing").getValue(Double::class.java) ?: 0.0
                    val currentRice = currentSnapshot.child("rice").getValue(Double::class.java) ?: 0.0
                    val currentGas = currentSnapshot.child("gas").getValue(Double::class.java) ?: 0.0
                    val currentMeals = currentSnapshot.child("totalMeals").getValue(Int::class.java) ?: 0

                    database.child("monthlyData").child(previousMonth)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(previousSnapshot: DataSnapshot) {
                                val previousMarketing = previousSnapshot.child("totalMarketing").getValue(Double::class.java) ?: 0.0
                                val previousRice = previousSnapshot.child("rice").getValue(Double::class.java) ?: 0.0
                                val previousGas = previousSnapshot.child("gas").getValue(Double::class.java) ?: 0.0
                                val previousMeals = previousSnapshot.child("totalMeals").getValue(Int::class.java) ?: 0

                                // Compare and update UI
                                val comparisonText = """
                                    Current Month:
                                    Marketing: ₹$currentMarketing
                                    Rice: ₹$currentRice
                                    Gas: ₹$currentGas
                                    Total Meals: $currentMeals

                                    Previous Month:
                                    Marketing: ₹$previousMarketing
                                    Rice: ₹$previousRice
                                    Gas: ₹$previousGas
                                    Total Meals: $previousMeals
                                """.trimIndent()

                                comparisonTextView.text = comparisonText
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle error
                            }
                        })
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
    }
}
