package com.devsan.santiniketanmess

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devsan.santiniketanmess.models.Member
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class MembersListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var membersAdapter: MembersAdapter
    private lateinit var firestore: FirebaseFirestore
    private val membersList = mutableListOf<Member>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members_list)

        // Initialize AppCheck if you're using it
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with an empty list
        membersAdapter = MembersAdapter(membersList)
        recyclerView.adapter = membersAdapter

        // Initialize Firestore with offline persistence enabled
        firestore = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        firestore.firestoreSettings = settings

        // Enable Firestore logging for debugging
        FirebaseFirestore.setLoggingEnabled(true)

        // Fetch members from Firestore
        fetchMembers()
    }

    private fun fetchMembers() {
        firestore.collection("members")
            .get()
            .addOnSuccessListener { documents ->
                membersList.clear() // Clear the list to avoid duplicates
                for (document in documents) {
                    val member = document.toObject(Member::class.java)

                    // Ensure that the member object is valid before adding it to the list
                    if (member != null && member.name.isNotEmpty()) {
                        membersList.add(member)
                        Log.d("Firestore", "Document: ${document.id} => ${document.data}")
                    }
                }
                membersAdapter.notifyDataSetChanged() // Notify the adapter that data has changed
                Log.d("Firestore", "Members fetched: ${membersList.size}")
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Failed to load members", exception)
                Toast.makeText(
                    this@MembersListActivity,
                    "Failed to load members: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}
