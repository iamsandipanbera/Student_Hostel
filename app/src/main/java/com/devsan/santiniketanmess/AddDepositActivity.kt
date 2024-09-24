package com.devsan.santiniketanmess

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class AddDepositActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var firestore: FirebaseFirestore
    private lateinit var amountField: EditText
    private lateinit var memberSpinner: Spinner
    private lateinit var saveButton: Button
    private lateinit var membersList: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_deposit)

        firestore = FirebaseFirestore.getInstance()

        // Initialize Firebase Realtime Database for total deposit tracking
        database = FirebaseDatabase.getInstance().getReference("members")

        // Initialize UI components
        amountField = findViewById(R.id.editTextDepositAmount)
        memberSpinner = findViewById(R.id.spinnerMember)
        saveButton = findViewById(R.id.buttonSaveDeposit)

        // Load members list (dummy data for now, replace with actual Firestore data if needed)
        membersList = listOf("Member 1", "Member 2", "Member 3")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, membersList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        memberSpinner.adapter = adapter

        // Handle save deposit button click
        saveButton.setOnClickListener {
            val amount = amountField.text.toString().toDoubleOrNull()
            val selectedMember = memberSpinner.selectedItem.toString()

            if (amount != null) {
                saveDepositToFirebase(selectedMember, amount)
            } else {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveDepositToFirebase(memberId: String, amount: Double) {
        val depositRef = database.child("$memberId/deposits")
        val depositId = depositRef.push().key ?: return

        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val depositDate = dateFormat.format(currentTime)
        val depositTime = timeFormat.format(currentTime)

        val depositDetails = mapOf(
            "amount" to amount,
            "date" to depositDate,
            "time" to depositTime
        )

        // Save deposit details in Realtime Database
        depositRef.child(depositId).setValue(depositDetails).addOnSuccessListener {
            // Update total deposit in Realtime Database
            updateTotalDeposit(memberId, amount)
            Toast.makeText(this, "Deposit saved successfully", Toast.LENGTH_SHORT).show()
            finish() // Close activity after saving
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to save deposit", Toast.LENGTH_SHORT).show()
        }

        // Additionally, save deposit in Firestore under the member's document
        val depositData = hashMapOf(
            "amount" to amount,
            "date" to currentTime
        )
        firestore.collection("Members").document(memberId).collection("Deposits")
            .add(depositData)
            .addOnSuccessListener {
                Toast.makeText(this, "Deposit added to Firestore", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Firestore error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateTotalDeposit(memberId: String, amount: Double) {
        val memberRef = database.child(memberId)
        memberRef.child("totalDeposit").get().addOnSuccessListener {
            val currentTotal = it.value as? Double ?: 0.0
            val newTotal = currentTotal + amount

            // Update total deposit in Realtime Database
            memberRef.child("totalDeposit").setValue(newTotal)
        }
    }
}
