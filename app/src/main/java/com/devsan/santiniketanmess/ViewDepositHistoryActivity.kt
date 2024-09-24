package com.devsan.santiniketanmess

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*

class ViewMemberDepositHistoryActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var depositsRecyclerView: RecyclerView
    private lateinit var totalAmountTextView: TextView
    private lateinit var depositAdapter: DepositAdapter
    private var totalAmount: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_member_deposit_history)

        firestore = FirebaseFirestore.getInstance()

        depositsRecyclerView = findViewById(R.id.depositsRecyclerView)
        totalAmountTextView = findViewById(R.id.totalAmountTextView)

        depositsRecyclerView.layoutManager = LinearLayoutManager(this)
        depositAdapter = DepositAdapter(mutableListOf())
        depositsRecyclerView.adapter = depositAdapter

        val memberId = intent.getStringExtra("memberId") ?: return
        fetchDepositHistory(memberId)
    }

    private fun fetchDepositHistory(memberId: String) {
        firestore.collection("members")
            .document(memberId)
            .collection("deposits")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val amount = document.getDouble("amount") ?: 0.0
                    val timestamp = document.getTimestamp("date")?.toDate() ?: Date()
                    val formattedDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(timestamp)

                    depositAdapter.addDeposit(Deposit(amount, formattedDate))

                    totalAmount += amount
                }

                totalAmountTextView.text = "Total Amount: ₹$totalAmount"
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch deposit history", Toast.LENGTH_SHORT).show()
            }
    }
}
