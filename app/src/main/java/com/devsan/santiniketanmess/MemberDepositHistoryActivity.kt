package com.devsan.santiniketanmess

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class MemberDepositHistoryActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var memberNameTextView: TextView
    private lateinit var depositHistoryRecyclerView: RecyclerView
    private lateinit var totalDepositTextView: TextView
    private var totalDeposit = 0.0

    companion object {
        private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_deposit_history)

        // Initialize Firestore and views
        db = FirebaseFirestore.getInstance()
        memberNameTextView = findViewById(R.id.memberNameTextView)
        depositHistoryRecyclerView = findViewById(R.id.depositHistoryRecyclerView) // Change ListView to RecyclerView
        totalDepositTextView = findViewById(R.id.totalDepositTextView)

        // Initialize RecyclerView
        depositHistoryRecyclerView.layoutManager = LinearLayoutManager(this)

        // Get memberId passed from previous activity
        val memberId = intent.getStringExtra("MEMBER_ID")

        // Fetch member name and deposit history
        if (memberId != null) {
            fetchMemberDetails(memberId)
        }
    }

    private fun fetchMemberDetails(memberId: String) {
        db.collection("members").document(memberId)
            .get()
            .addOnSuccessListener { document ->
                val memberName = document.getString("name") ?: "Member"
                memberNameTextView.text = memberName

                fetchDepositHistory(memberId)
            }
            .addOnFailureListener {
                // Handle failure, for example:
                Toast.makeText(this, "Failed to fetch member details", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchDepositHistory(memberId: String) {
        db.collection("members").document(memberId).collection("deposits")
            .orderBy("date")
            .get()
            .addOnSuccessListener { result ->
                val deposits = mutableListOf<Deposit>()
                totalDeposit = 0.0

                for (document in result) {
                    val amount = document.getDouble("amount") ?: 0.0
                    val date = document.getDate("date") ?: Date()
                    val dateString = dateFormat.format(date)

                    deposits.add(Deposit(amount, dateString))
                    totalDeposit += amount
                }

                totalDepositTextView.text = "Total Deposit: ₹$totalDeposit"

                // Set adapter to RecyclerView
                val adapter = DepositAdapter(deposits)
                depositHistoryRecyclerView.adapter = adapter
            }
            .addOnFailureListener {
                // Handle error, for example:
                Toast.makeText(this, "Failed to fetch deposit history", Toast.LENGTH_SHORT).show()
            }
    }
}
