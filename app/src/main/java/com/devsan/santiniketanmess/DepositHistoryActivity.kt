package com.devsan.santiniketanmess

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class DepositHistoryActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var memberId: String
    private lateinit var depositHistoryTextView: TextView
    private lateinit var finalTotalTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit_history)

        // Initialize UI components
        depositHistoryTextView = findViewById(R.id.textViewDepositHistory)
        finalTotalTextView = findViewById(R.id.textViewFinalTotal)

        // Fetch memberId from intent
        memberId = intent.getStringExtra("memberId").toString()

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().reference

        // Fetch member's deposit history from Firebase
        fetchDepositHistory()
    }

    private fun fetchDepositHistory() {
        database.child("members").child(memberId).child("deposits")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val depositHistory = StringBuilder()
                    var finalTotal = 0.0

                    for (depositSnapshot in snapshot.children) {
                        val amount = depositSnapshot.child("amount").getValue(Double::class.java) ?: 0.0
                        val date = depositSnapshot.child("date").getValue(String::class.java) ?: "Unknown"
                        val time = depositSnapshot.child("time").getValue(String::class.java) ?: "Unknown"

                        // Append each deposit entry to the history
                        depositHistory.append("Amount: ₹$amount\nDate: $date\nTime: $time\n\n")
                        finalTotal += amount
                    }

                    // Update the UI
                    depositHistoryTextView.text = depositHistory.toString()
                    finalTotalTextView.text = "Final Total: ₹$finalTotal"
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
    }
}
