package com.devsan.santiniketanmess

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devsan.santiniketanmess.databinding.ActivityIndividualMealsBinding
import com.google.firebase.database.*

class IndividualMemberMealsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIndividualMealsBinding
    private lateinit var database: DatabaseReference
    private lateinit var membersList: MutableList<String>

    companion object {
        private const val TAG = "IndividualMemberMeals"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIndividualMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("members")
        membersList = mutableListOf()

        // Fetch members from Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                membersList.clear()
                for (memberSnapshot in snapshot.children) {
                    val memberName = memberSnapshot.child("name").getValue(String::class.java)
                    memberName?.let { membersList.add(it) }
                }

                // Setup Spinner after fetching members
                val adapter = ArrayAdapter(this@IndividualMemberMealsActivity, android.R.layout.simple_spinner_item, membersList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerMembers.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        binding.btnSaveMeals.setOnClickListener {
            saveMeals()
        }
    }

    private fun saveMeals() {
        val member = binding.spinnerMembers.selectedItem.toString()
        val meals = binding.etMeals.text.toString().toIntOrNull()

        if (meals != null) {
            val meal = Meal(member, meals)
            val mealsRef = FirebaseDatabase.getInstance().getReference("meals")
            mealsRef.child(member).push().setValue(meal)
                .addOnSuccessListener {
                    Toast.makeText(this, "Meals saved successfully!", Toast.LENGTH_SHORT).show()
                    binding.etMeals.text.clear()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to save meals.", Toast.LENGTH_SHORT).show()
                    Log.w(TAG, "Error saving meals", it)
                }
        } else {
            Toast.makeText(this, "Please enter a valid number of meals.", Toast.LENGTH_SHORT).show()
        }
    }
}